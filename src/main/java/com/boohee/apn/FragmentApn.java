package com.boohee.apn;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.boohee.api.MessengerApi;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.utils.PhotoPickerHelper;
import com.boohee.utils.TextUtil;
import com.boohee.utils.WheelUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentApn extends BaseFragment {
    protected static final int MAX_IMAGES    = 3;
    private static final   int SELECT_PHOTOS = 23333;
    @InjectView(2131427818)
    View     btSend;
    @InjectView(2131428882)
    EditText etQuestion;
    private ContentAdapter mAdapter;
    private List<CategoryModel> mCategories  = new ArrayList();
    private List<ApnContent>    mDataList    = new ArrayList();
    private OnClickListener     mDeleteClick = new OnClickListener() {
        public void onClick(View v) {
            String path = (String) v.getTag();
            Helper.showLog(path);
            if (!TextUtils.isEmpty(path)) {
                for (int i = 0; i < FragmentApn.this.viewImages.getChildCount(); i++) {
                    View view = FragmentApn.this.viewImages.getChildAt(i);
                    if (((String) view.getTag()).equalsIgnoreCase(path)) {
                        FragmentApn.this.viewImages.removeView(view);
                    }
                }
                FragmentApn.this.mPathList.remove(path);
            }
        }
    };
    private long          mLastId;
    private LayoutManager mLayoutManager;
    private ArrayList<String> mPathList  = new ArrayList();
    private List<QiniuModel>  mQiniuList = new ArrayList();
    @InjectView(2131428173)
    RecyclerView       rvMain;
    @InjectView(2131428172)
    SwipeRefreshLayout srlRefresh;
    @InjectView(2131428410)
    LinearLayout       viewImages;
    @InjectView(2131428879)
    RelativeLayout     viewPopupWindow;
    @InjectView(2131428881)
    TextView           viewSelectCategory;

    @OnClick({2131428174, 2131428881, 2131427818, 2131428880, 2131428884})
    public void onClick(View view) {
        if (!isDetached() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.bt_send:
                    this.btSend.setClickable(false);
                    preSendMessage();
                    return;
                case R.id.view_ask:
                    showPopWindow(true);
                    return;
                case R.id.view_shade:
                    showPopWindow(false);
                    return;
                case R.id.view_select_category:
                    if (this.mCategories.size() == 0) {
                        requestCategory();
                    } else {
                        showSelectCategory();
                    }
                    KeyBoardUtils.closeAll(getActivity());
                    return;
                case R.id.view_default:
                    if (this.mPathList.size() >= 3) {
                        Helper.showLog(getString(R.string.bv));
                        return;
                    } else {
                        PhotoPickerHelper.show(getActivity(), true, 3, this.mPathList, (int)
                                SELECT_PHOTOS);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fe, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        this.rvMain.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.rvMain.setLayoutManager(this.mLayoutManager);
        this.mAdapter = new ContentAdapter(getActivity(), this.mDataList);
        this.rvMain.setAdapter(this.mAdapter);
        this.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                FragmentApn.this.requestMessage(false);
            }
        });
        showLoading();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestMessage(true);
    }

    private void requestCategory() {
        showLoading();
        MessengerApi.v2GetCategories(getActivity(), new JsonCallback(getActivity()) {
            public void ok(String response) {
                List<CategoryModel> tmpData = JSON.parseArray(JSON.parseObject(response)
                        .getString("categories"), CategoryModel.class);
                if (tmpData != null && tmpData.size() > 0) {
                    FragmentApn.this.mCategories.addAll(tmpData);
                    tmpData.clear();
                }
                FragmentApn.this.showSelectCategory();
            }

            public void onFinish() {
                FragmentApn.this.dismissLoading();
            }
        });
    }

    private void requestMessage(final boolean isFirst) {
        if (isFirst) {
            this.mLastId = 0;
            this.mDataList.clear();
        }
        MessengerApi.v2GetMessage(getActivity(), this.mLastId, new JsonCallback(getActivity()) {
            public void ok(String response) {
                List<ApnContent> tmpData = JSON.parseArray(JSON.parseObject(response).getString
                        ("messages"), ApnContent.class);
                if (tmpData == null || tmpData.size() <= 0) {
                    Helper.showToast((int) R.string.by);
                    return;
                }
                FragmentApn.this.mDataList.addAll(0, tmpData);
                FragmentApn.this.mLastId = ((ApnContent) FragmentApn.this.mDataList.get(0)).id;
                FragmentApn.this.mAdapter.notifyDataSetChanged();
                if (isFirst) {
                    FragmentApn.this.rvMain.scrollToPosition(FragmentApn.this.mDataList.size() - 1);
                } else {
                    FragmentApn.this.rvMain.scrollToPosition(tmpData.size() - 1);
                }
                tmpData.clear();
            }

            public void onFinish() {
                FragmentApn.this.srlRefresh.setRefreshing(false);
                FragmentApn.this.dismissLoading();
            }
        });
    }

    private void preSendMessage() {
        String tmpObj = this.viewSelectCategory.getTag();
        if (tmpObj == null) {
            this.btSend.setClickable(true);
            Helper.showLong(getString(R.string.bw));
            return;
        }
        final String category = tmpObj;
        final String content = this.etQuestion.getText().toString();
        if (TextUtil.isEmpty(content)) {
            this.btSend.setClickable(true);
            Helper.showLong(getString(R.string.bx));
        } else if (this.mPathList.size() > 0) {
            showLoading();
            QiniuUploader.upload(Prefix.messenger, new UploadHandler() {
                public void onSuccess(List<QiniuModel> infos) {
                    FragmentApn.this.mQiniuList.clear();
                    FragmentApn.this.mQiniuList.addAll(infos);
                    FragmentApn.this.sendMessage(category, content);
                }

                public void onError(String msg) {
                    Helper.showToast((CharSequence) msg);
                    FragmentApn.this.btSend.setClickable(true);
                }

                public void onFinish() {
                    FragmentApn.this.dismissLoading();
                }
            }, this.mPathList);
        } else {
            sendMessage(category, content);
        }
    }

    private void sendMessage(String category, String content) {
        JSONArray photos = null;
        if (this.mQiniuList.size() > 0) {
            photos = new JSONArray();
            try {
                for (QiniuModel model : this.mQiniuList) {
                    JSONObject photo = new JSONObject();
                    photo.put("photo_key", model.key);
                    photo.put("photo_hash", model.hash);
                    photos.put(photo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                this.btSend.setClickable(true);
                return;
            }
        }
        showLoading();
        MessengerApi.v2SendMessage(getActivity(), content, category, photos, new JsonCallback
                (getActivity()) {
            public void ok(String response) {
                FragmentApn.this.showPopWindow(false);
                FragmentApn.this.requestMessage(true);
                FragmentApn.this.etQuestion.setText("");
                FragmentApn.this.mPathList.clear();
                FragmentApn.this.mQiniuList.clear();
                FragmentApn.this.viewImages.removeAllViews();
            }

            public void onFinish() {
                FragmentApn.this.dismissLoading();
                FragmentApn.this.btSend.setClickable(true);
            }
        });
    }

    private void showPopWindow(boolean isShow) {
        this.viewPopupWindow.setAnimation(AnimationUtils.loadAnimation(getActivity(), isShow ? R
                .anim.s : R.anim.al));
        this.viewPopupWindow.setVisibility(isShow ? 0 : 8);
    }

    private void showSelectCategory() {
        int size = this.mCategories.size();
        if (size != 0) {
            CharSequence[] items = new CharSequence[size];
            for (int i = 0; i < size; i++) {
                items[i] = ((CategoryModel) this.mCategories.get(i)).content;
            }
            new Builder(getActivity()).setTitle(R.string.c1).setItems(items, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    CategoryModel category = (CategoryModel) FragmentApn.this.mCategories.get
                            (which);
                    FragmentApn.this.viewSelectCategory.setTag(category.key);
                    FragmentApn.this.viewSelectCategory.setText(category.content);
                    FragmentApn.this.viewSelectCategory.setTextColor(-16777216);
                }
            }).create().show();
        }
    }

    private void updateSelect() {
        this.viewImages.removeAllViews();
        if (this.mPathList.size() > 0) {
            Iterator it = this.mPathList.iterator();
            while (it.hasNext()) {
                String path = (String) it.next();
                RelativeLayout view = (RelativeLayout) LayoutInflater.from(getActivity()).inflate
                        (R.layout.me, null);
                ImageView view_delete_image = (ImageView) view.findViewById(R.id.view_delete_image);
                ImageView view_image = (ImageView) view.findViewById(R.id.view_image);
                view_delete_image.setTag(path);
                view_image.setTag(path);
                view_delete_image.setOnClickListener(this.mDeleteClick);
                view.setTag(path);
                this.imageLoader.displayImage("file://" + path, view_image, ImageLoaderOptions
                        .avatar());
                float dimen = getResources().getDimension(R.dimen.ba);
                this.viewImages.addView(view, new LayoutParams((int) dimen, (int) dimen));
                this.viewImages.invalidate();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mPathList != null) {
            this.mPathList.clear();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTOS /*23333*/:
                getActivity();
                if (resultCode == -1) {
                    List<String> pathList = data.getStringArrayListExtra
                            (MultiImageSelectorActivity.EXTRA_RESULT);
                    if (pathList != null && pathList.size() > 0) {
                        this.mPathList.clear();
                        this.mPathList.addAll(pathList);
                        updateSelect();
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
