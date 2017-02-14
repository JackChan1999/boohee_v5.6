package com.boohee.one.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.myview.DragScaleImageView;
import com.boohee.myview.DragScaleImageView.OnDeleteListener;
import com.boohee.myview.DragScaleImageView.OnSingleTapListener;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter.DataSet;
import com.boohee.utility.DensityUtil;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.HorizontalListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ChartletFragment extends BaseFragment {
    public static final String KEY_DRAWABLE_IDS           = "key_drawale_ids";
    public static final String KEY_STRINGS                = "key_strings";
    public static final String KEY_THUMBNAIL_DRAWABLE_IDS = "key_thumbnail_drawale_ids";
    public static final String KEY_URI_STR                = "key_uri_str";
    private int[]    CHARTLET_RES_IDS;
    private String[] CHARTLET_TEXTS;
    private ArrayList<DragScaleImageView> dragViewList = new ArrayList();
    @InjectView(2131428213)
    HorizontalListView iconListView;
    private Context mContext;
    private Handler mHandler;
    private int[]   mThumbailIds;
    private Uri     mUri;
    @InjectView(2131428211)
    FrameLayout parentLayout;
    @InjectView(2131428212)
    ImageView   preImage;

    private class ItemClickListener implements OnItemClickListener {
        private ItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
            final DragScaleImageView dragView = new DragScaleImageView(ChartletFragment.this
                    .mContext);
            dragView.setImageResource(ChartletFragment.this.CHARTLET_RES_IDS[position]);
            LayoutParams lp = new LayoutParams(DensityUtil.dip2px(ChartletFragment.this.mContext,
                    108.0f), DensityUtil.dip2px(ChartletFragment.this.mContext, 108.0f));
            lp.gravity = 17;
            dragView.setOnDeleteListener(new OnDeleteListener() {
                public void onDelete() {
                    ChartletFragment.this.dragViewList.remove(dragView);
                }
            });
            dragView.setOnSingleTabListener(new OnSingleTapListener() {
                public void onSingleTab() {
                    Iterator it = ChartletFragment.this.dragViewList.iterator();
                    while (it.hasNext()) {
                        ((DragScaleImageView) it.next()).setFocusable(false);
                    }
                }
            });
            ChartletFragment.this.dragViewList.add(dragView);
            ChartletFragment.this.parentLayout.addView(dragView, lp);
        }
    }

    public static ChartletFragment newInstance(int[] thumbnailIds, int[] drawableIds, String
            imageUriStr) {
        return newInstance(thumbnailIds, drawableIds, null, imageUriStr);
    }

    public static ChartletFragment newInstance(int[] thumbnailIds, int[] drawableIds, String[]
            strings, String imageUriStr) {
        if (thumbnailIds == null || thumbnailIds.length == 0 || drawableIds == null ||
                drawableIds.length == 0 || TextUtils.isEmpty(imageUriStr)) {
            return null;
        }
        ChartletFragment fragment = new ChartletFragment();
        Bundle args = new Bundle();
        args.putIntArray(KEY_DRAWABLE_IDS, drawableIds);
        args.putIntArray(KEY_THUMBNAIL_DRAWABLE_IDS, thumbnailIds);
        if (strings != null && strings.length > 0) {
            args.putStringArray(KEY_STRINGS, strings);
        }
        args.putString(KEY_URI_STR, imageUriStr);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        if (args != null) {
            this.CHARTLET_RES_IDS = args.getIntArray(KEY_DRAWABLE_IDS);
            this.mThumbailIds = args.getIntArray(KEY_THUMBNAIL_DRAWABLE_IDS);
            this.CHARTLET_TEXTS = args.getStringArray(KEY_STRINGS);
            this.mUri = Uri.parse(args.getString(KEY_URI_STR));
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.CHARTLET_RES_IDS = savedInstanceState.getIntArray(KEY_DRAWABLE_IDS);
            this.mThumbailIds = savedInstanceState.getIntArray(KEY_THUMBNAIL_DRAWABLE_IDS);
            this.CHARTLET_TEXTS = savedInstanceState.getStringArray(KEY_STRINGS);
            this.mUri = Uri.parse(savedInstanceState.getString(KEY_URI_STR));
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fn, container, false);
        ButterKnife.inject((Object) this, rootView);
        return rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState == null) {
            outState = new Bundle();
        }
        outState.putIntArray(KEY_DRAWABLE_IDS, this.CHARTLET_RES_IDS);
        outState.putIntArray(KEY_THUMBNAIL_DRAWABLE_IDS, this.mThumbailIds);
        if (this.CHARTLET_TEXTS != null && this.CHARTLET_TEXTS.length > 0) {
            outState.putStringArray(KEY_STRINGS, this.CHARTLET_TEXTS);
        }
        outState.putString(KEY_URI_STR, this.mUri == null ? null : this.mUri.toString());
        super.onSaveInstanceState(outState);
    }

    private void init() {
        this.mContext = getActivity();
        this.mHandler = new Handler(Looper.getMainLooper());
        initPreImage(this.mUri);
        initChartletListView();
    }

    private void initPreImage(Uri mUri) {
        ViewUtils.initImageView(this.mContext, mUri, this.preImage);
    }

    private void initChartletListView() {
        this.iconListView.setAdapter(new HorizontalIconListAdapter(this.mContext, new DataSet
                (this.mThumbailIds, this.CHARTLET_TEXTS)));
        this.iconListView.setOnItemClickListener(new ItemClickListener());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.a2n).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        postAction();
        return true;
    }

    public void postAction() {
        Iterator it = this.dragViewList.iterator();
        while (it.hasNext()) {
            ((DragScaleImageView) it.next()).setFocusable(false);
        }
        final Bitmap bmp = BitmapUtil.viewToBitmap(this.parentLayout);
        if (bmp != null) {
            showLoading();
            new Thread() {
                public void run() {
                    if (ChartletFragment.this.getActivity() != null) {
                        File dir = ChartletFragment.this.getActivity().getExternalCacheDir();
                        if (dir != null) {
                            String fileName = dir.toString() + File.separatorChar + System
                                    .currentTimeMillis() + ".png";
                            BitmapUtil.saveBitmap(fileName, bmp);
                            ChartletFragment.this.mUri = Uri.fromFile(new File(fileName));
                            ChartletFragment.this.mHandler.post(new Runnable() {
                                public void run() {
                                    if (ChartletFragment.this.getActivity() != null) {
                                        ChartletFragment.this.dismissLoading();
                                        ChartletFragment.this.getActivity().setResult(-1, new
                                                Intent().setData(ChartletFragment.this.mUri));
                                        ChartletFragment.this.getActivity().finish();
                                    }
                                }
                            });
                        }
                    }
                }
            }.start();
        }
    }
}
