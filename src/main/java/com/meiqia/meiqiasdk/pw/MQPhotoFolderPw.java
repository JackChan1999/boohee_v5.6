package com.meiqia.meiqiasdk.pw;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.model.ImageFolderModel;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.meiqia.meiqiasdk.widget.MQImageView;

import java.util.ArrayList;
import java.util.List;

public class MQPhotoFolderPw extends MQBasePopupWindow implements OnItemClickListener {
    public static final int ANIM_DURATION = 300;
    private Callback      mCallback;
    private ListView      mContentLv;
    private int           mCurrentPosition;
    private FolderAdapter mFolderAdapter;
    private LinearLayout  mRootLl;

    public interface Callback {
        void executeDismissAnim();

        void onSelectedFolder(int i);
    }

    private class FolderAdapter extends BaseAdapter {
        private List<ImageFolderModel> mDatas = new ArrayList();
        private int mImageHeight;
        private int mImageWidth;

        public FolderAdapter() {
            this.mImageWidth = MQUtils.getScreenWidth(MQPhotoFolderPw.this.mActivity) / 10;
            this.mImageHeight = this.mImageWidth;
        }

        public int getCount() {
            return this.mDatas.size();
        }

        public ImageFolderModel getItem(int position) {
            return (ImageFolderModel) this.mDatas.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            FolderViewHolder folderViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .mq_item_photo_folder, parent, false);
                folderViewHolder = new FolderViewHolder();
                folderViewHolder.photoIv = (MQImageView) convertView.findViewById(R.id.photo_iv);
                folderViewHolder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
                folderViewHolder.countTv = (TextView) convertView.findViewById(R.id.count_tv);
                convertView.setTag(folderViewHolder);
            } else {
                folderViewHolder = (FolderViewHolder) convertView.getTag();
            }
            ImageFolderModel imageFolderModel = getItem(position);
            folderViewHolder.nameTv.setText(imageFolderModel.name);
            folderViewHolder.countTv.setText(String.valueOf(imageFolderModel.getCount()));
            MQConfig.getImageLoader(MQPhotoFolderPw.this.mActivity).displayImage(folderViewHolder
                    .photoIv, imageFolderModel.coverPath, R.drawable.mq_ic_holder_light, R
                    .drawable.mq_ic_holder_light, this.mImageWidth, this.mImageHeight, null);
            return convertView;
        }

        public void setDatas(ArrayList<ImageFolderModel> datas) {
            if (datas != null) {
                this.mDatas = datas;
            } else {
                this.mDatas.clear();
            }
            notifyDataSetChanged();
        }
    }

    private class FolderViewHolder {
        public TextView    countTv;
        public TextView    nameTv;
        public MQImageView photoIv;

        private FolderViewHolder() {
        }
    }

    public MQPhotoFolderPw(Activity activity, View anchorView, Callback callback) {
        super(activity, R.layout.mq_pw_photo_folder, anchorView, -1, -1);
        this.mCallback = callback;
    }

    protected void initView() {
        this.mRootLl = (LinearLayout) getViewById(R.id.root_ll);
        this.mContentLv = (ListView) getViewById(R.id.content_lv);
    }

    protected void setListener() {
        this.mRootLl.setOnClickListener(this);
        this.mContentLv.setOnItemClickListener(this);
    }

    protected void processLogic() {
        setAnimationStyle(16973824);
        setBackgroundDrawable(new ColorDrawable(-1879048192));
        this.mFolderAdapter = new FolderAdapter();
        this.mContentLv.setAdapter(this.mFolderAdapter);
    }

    public void setDatas(ArrayList<ImageFolderModel> datas) {
        this.mFolderAdapter.setDatas(datas);
    }

    public void show() {
        showAsDropDown(this.mAnchorView);
        ViewCompat.animate(this.mContentLv).translationY((float) (-this.mWindowRootView.getHeight
                ())).setDuration(0).start();
        ViewCompat.animate(this.mContentLv).translationY(0.0f).setDuration(300).start();
        ViewCompat.animate(this.mRootLl).alpha(0.0f).setDuration(0).start();
        ViewCompat.animate(this.mRootLl).alpha(1.0f).setDuration(300).start();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (!(this.mCallback == null || this.mCurrentPosition == position)) {
            this.mCallback.onSelectedFolder(position);
        }
        this.mCurrentPosition = position;
        dismiss();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.root_ll) {
            dismiss();
        }
    }

    public void dismiss() {
        ViewCompat.animate(this.mContentLv).translationY((float) (-this.mWindowRootView.getHeight
                ())).setDuration(300).start();
        ViewCompat.animate(this.mRootLl).alpha(1.0f).setDuration(0).start();
        ViewCompat.animate(this.mRootLl).alpha(0.0f).setDuration(300).start();
        if (this.mCallback != null) {
            this.mCallback.executeDismissAnim();
        }
        this.mContentLv.postDelayed(new Runnable() {
            public void run() {
                super.dismiss();
            }
        }, 300);
    }

    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }
}
