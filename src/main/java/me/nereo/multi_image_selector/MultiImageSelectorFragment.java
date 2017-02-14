package me.nereo.multi_image_selector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.l.b;
import com.umeng.socialize.common.SocializeConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.nereo.multi_image_selector.adapter.FolderAdapter;
import me.nereo.multi_image_selector.adapter.ImageGridAdapter;
import me.nereo.multi_image_selector.bean.Folder;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.utils.FileUtils;
import me.nereo.multi_image_selector.utils.MultiImageSelector;
import me.nereo.multi_image_selector.utils.TimeUtils;

public class MultiImageSelectorFragment extends Fragment {
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    public static final int MODE_MULTI = 1;
    public static final int MODE_SINGLE = 0;
    private static final int REQUEST_CAMERA = 100;
    public static final String TAG = "MultiImageSelector";
    private static ArrayList<String> resultList = new ArrayList();
    private int folderPopWindowWidth;
    private int folderPopWindwoHeight;
    private boolean hasFolderGened = false;
    private Callback mCallback;
    private TextView mCategoryText;
    private int mDesireImageCount;
    private FolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;
    private GridView mGridView;
    private ImageGridAdapter mImageAdapter;
    private List<Image> mImageList = new ArrayList();
    private LoaderCallbacks<Cursor> mLoaderCallback = new LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = new String[]{"_data", "_display_name", "date_added", "_size", "_id"};

        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                return new CursorLoader(MultiImageSelectorFragment.this.getActivity(), Media.EXTERNAL_CONTENT_URI, this.IMAGE_PROJECTION, null, null, this.IMAGE_PROJECTION[2] + " DESC");
            } else if (id != 1) {
                return null;
            } else {
                return new CursorLoader(MultiImageSelectorFragment.this.getActivity(), Media.EXTERNAL_CONTENT_URI, this.IMAGE_PROJECTION, this.IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, this.IMAGE_PROJECTION[2] + " DESC");
            }
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.getCount() > 0) {
                data.moveToFirst();
                MultiImageSelectorFragment.this.mImageList.clear();
                do {
                    String path = data.getString(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[0]));
                    if (!TextUtils.isEmpty(path)) {
                        Image image = new Image(path, data.getString(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[1])), data.getLong(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[2])), data.getLong(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[3])), data.getString(data.getColumnIndexOrThrow(this.IMAGE_PROJECTION[4])));
                        MultiImageSelectorFragment.this.mImageList.add(image);
                        if (!MultiImageSelectorFragment.this.hasFolderGened) {
                            File imageFile = new File(path);
                            if (imageFile.exists()) {
                                File folderFile = imageFile.getParentFile();
                                if (folderFile != null && folderFile.exists()) {
                                    Folder folder = new Folder();
                                    folder.name = folderFile.getName();
                                    folder.path = folderFile.getAbsolutePath();
                                    folder.cover = image;
                                    if (MultiImageSelectorFragment.this.mResultFolder.contains(folder)) {
                                        ((Folder) MultiImageSelectorFragment.this.mResultFolder.get(MultiImageSelectorFragment.this.mResultFolder.indexOf(folder))).images.add(image);
                                    } else {
                                        List<Image> imageList = new ArrayList();
                                        imageList.add(image);
                                        folder.images = imageList;
                                        MultiImageSelectorFragment.this.mResultFolder.add(folder);
                                    }
                                }
                            }
                        }
                    }
                } while (data.moveToNext());
                MultiImageSelectorFragment.this.mImageAdapter.setData(MultiImageSelectorFragment.this.mImageList);
                MultiImageSelectorFragment.this.refreshSelectState();
                MultiImageSelectorFragment.this.mFolderAdapter.setData(MultiImageSelectorFragment.this.mResultFolder);
                MultiImageSelectorFragment.this.hasFolderGened = true;
            }
        }

        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };
    private View mPopupAnchorView;
    private Button mPreviewBtn;
    private ArrayList<Folder> mResultFolder = new ArrayList();
    private boolean mShowCamera = true;
    private TextView mTimeLineText;
    private File mTmpFile;

    public static MultiImageSelectorFragment newInstance(Bundle args, ArrayList<String> selecList) {
        resultList = selecList;
        MultiImageSelectorFragment fragment = new MultiImageSelectorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.folderPopWindowWidth = metrics.widthPixels;
        this.folderPopWindwoHeight = (int) (((double) metrics.heightPixels) * b.if);
        return inflater.inflate(R.layout.fragment_multi_image, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        boolean z = true;
        super.onViewCreated(view, savedInstanceState);
        this.mDesireImageCount = getArguments().getInt("max_select_count");
        final int mode = getArguments().getInt("select_count_mode");
        if (mode == 1) {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
        }
        this.mShowCamera = getArguments().getBoolean("show_camera", true);
        this.mImageAdapter = new ImageGridAdapter(getActivity(), this.mShowCamera, this.mDesireImageCount);
        ImageGridAdapter imageGridAdapter = this.mImageAdapter;
        if (mode != 1) {
            z = false;
        }
        imageGridAdapter.showSelectIndicator(z);
        if (this.mShowCamera) {
            this.mTmpFile = FileUtils.createTmpFile(getActivity());
        }
        this.mPopupAnchorView = view.findViewById(R.id.footer);
        this.mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        this.mTimeLineText.setVisibility(8);
        this.mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        this.mCategoryText.setText(R.string.folder_all);
        this.mCategoryText.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MultiImageSelectorFragment.this.mFolderPopupWindow.isShowing()) {
                    MultiImageSelectorFragment.this.mFolderPopupWindow.dismiss();
                    return;
                }
                MultiImageSelectorFragment.this.mFolderPopupWindow.show();
                int index = MultiImageSelectorFragment.this.mFolderAdapter.getSelectIndex();
                if (index != 0) {
                    index--;
                }
                MultiImageSelectorFragment.this.mFolderPopupWindow.getListView().setSelection(index);
            }
        });
        this.mPreviewBtn = (Button) view.findViewById(R.id.preview);
        this.mPreviewBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                List<Image> tempImageList = new ArrayList();
                Iterator i$ = MultiImageSelectorFragment.resultList.iterator();
                while (i$.hasNext()) {
                    String s = (String) i$.next();
                    if (!TextUtils.isEmpty(s)) {
                        tempImageList.add(new Image(s, null, 0, 0, null));
                    }
                }
                MultiImagePreviewActivity.startMe(MultiImageSelectorFragment.this.getActivity(), tempImageList, MultiImageSelectorFragment.resultList, MultiImageSelectorFragment.this.mDesireImageCount, 0);
            }
        });
        this.mGridView = (GridView) view.findViewById(R.id.grid);
        this.mGridView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int state) {
                if (state == 0 || state == 1) {
                    MultiImageSelector.getImageLoader().resume(MultiImageSelectorFragment.this.getActivity());
                } else {
                    MultiImageSelector.getImageLoader().pause(MultiImageSelectorFragment.this.getActivity());
                }
                if (state == 0) {
                    MultiImageSelectorFragment.this.mTimeLineText.setVisibility(8);
                } else if (state == 2) {
                    MultiImageSelectorFragment.this.mTimeLineText.setVisibility(0);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (MultiImageSelectorFragment.this.mTimeLineText.getVisibility() == 0) {
                    Image image = (Image) ((ListAdapter) view.getAdapter()).getItem(firstVisibleItem + 1 == ((ListAdapter) view.getAdapter()).getCount() ? ((ListAdapter) view.getAdapter()).getCount() - 1 : firstVisibleItem + 1);
                    if (image != null) {
                        MultiImageSelectorFragment.this.mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
                    }
                }
            }
        });
        this.mGridView.setAdapter(this.mImageAdapter);
        this.mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @TargetApi(16)
            public void onGlobalLayout() {
                int width = MultiImageSelectorFragment.this.mGridView.getWidth();
                int numCount = width / MultiImageSelectorFragment.this.getResources().getDimensionPixelOffset(R.dimen.image_size);
                MultiImageSelectorFragment.this.mImageAdapter.setItemSize((width - ((numCount - 1) * MultiImageSelectorFragment.this.getResources().getDimensionPixelOffset(R.dimen.space_size))) / numCount);
                if (MultiImageSelectorFragment.this.mFolderPopupWindow == null) {
                    MultiImageSelectorFragment.this.createPopupFolderList(width);
                }
                if (VERSION.SDK_INT >= 16) {
                    MultiImageSelectorFragment.this.mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    MultiImageSelectorFragment.this.mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        this.mGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MultiImageSelectorFragment.this.mImageAdapter.isShowCamera()) {
                    clickPictureItem(i);
                } else if (i == 0) {
                    MultiImageSelectorFragment.this.showCameraAction();
                } else {
                    clickPictureItem(i);
                }
            }

            private void clickPictureItem(int position) {
                if (mode == 1) {
                    Activity activity = MultiImageSelectorFragment.this.getActivity();
                    List access$900 = MultiImageSelectorFragment.this.mImageList;
                    ArrayList access$200 = MultiImageSelectorFragment.resultList;
                    int access$300 = MultiImageSelectorFragment.this.mDesireImageCount;
                    if (MultiImageSelectorFragment.this.mShowCamera) {
                        position--;
                    }
                    MultiImagePreviewActivity.startMe(activity, access$900, access$200, access$300, position);
                    return;
                }
                Image image = MultiImageSelectorFragment.this.mImageAdapter.getItem(position);
                if (MultiImageSelectorFragment.this.mCallback != null && image != null) {
                    MultiImageSelectorFragment.this.mCallback.onSingleImageSelected(image.path);
                }
            }
        });
        this.mFolderAdapter = new FolderAdapter(getActivity());
    }

    public void onResume() {
        super.onResume();
        refreshSelectState();
    }

    private void createPopupFolderList(int width) {
        this.mFolderPopupWindow = new ListPopupWindow(getActivity());
        this.mFolderPopupWindow.setAdapter(this.mFolderAdapter);
        this.mFolderPopupWindow.setContentWidth(width);
        setFolderPopupWindowSize();
        this.mFolderPopupWindow.setAnchorView(this.mPopupAnchorView);
        this.mFolderPopupWindow.setModal(true);
        this.mFolderPopupWindow.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    MultiImageSelectorFragment.this.getActivity().getSupportLoaderManager().restartLoader(0, null, MultiImageSelectorFragment.this.mLoaderCallback);
                    MultiImageSelectorFragment.this.mCategoryText.setText(R.string.folder_all);
                    MultiImageSelectorFragment.this.mImageAdapter.setShowCamera(true);
                    MultiImageSelectorFragment.this.mShowCamera = true;
                } else {
                    Folder folder = (Folder) adapterView.getAdapter().getItem(i);
                    if (folder != null) {
                        Bundle args = new Bundle();
                        args.putString("path", folder.path);
                        MultiImageSelectorFragment.this.getActivity().getSupportLoaderManager().restartLoader(1, args, MultiImageSelectorFragment.this.mLoaderCallback);
                        MultiImageSelectorFragment.this.mCategoryText.setText(folder.name);
                    }
                    MultiImageSelectorFragment.this.mImageAdapter.setShowCamera(false);
                    MultiImageSelectorFragment.this.mShowCamera = false;
                }
                MultiImageSelectorFragment.this.mFolderAdapter.setSelectIndex(i);
                MultiImageSelectorFragment.this.mFolderPopupWindow.dismiss();
                MultiImageSelectorFragment.this.mGridView.smoothScrollToPosition(0);
            }
        });
    }

    private void setFolderPopupWindowSize() {
        this.mFolderPopupWindow.setHeight(this.folderPopWindwoHeight);
        this.mFolderPopupWindow.setWidth(this.folderPopWindowWidth);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(0, null, this.mLoaderCallback);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 100 && this.mTmpFile != null && this.mCallback != null) {
            this.mCallback.onCameraShot(this.mTmpFile);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");
        int orientation = newConfig.orientation;
        if (this.mFolderPopupWindow != null && this.mFolderPopupWindow.isShowing()) {
            this.mFolderPopupWindow.dismiss();
        }
        this.mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @TargetApi(16)
            public void onGlobalLayout() {
                int desireSize = MultiImageSelectorFragment.this.getResources().getDimensionPixelOffset(R.dimen.image_size);
                Log.d(MultiImageSelectorFragment.TAG, "Desire Size = " + desireSize);
                int numCount = MultiImageSelectorFragment.this.mGridView.getWidth() / desireSize;
                Log.d(MultiImageSelectorFragment.TAG, "Grid Size = " + MultiImageSelectorFragment.this.mGridView.getWidth());
                Log.d(MultiImageSelectorFragment.TAG, "num count = " + numCount);
                MultiImageSelectorFragment.this.mImageAdapter.setItemSize((MultiImageSelectorFragment.this.mGridView.getWidth() - ((numCount - 1) * MultiImageSelectorFragment.this.getResources().getDimensionPixelOffset(R.dimen.space_size))) / numCount);
                if (MultiImageSelectorFragment.this.mFolderPopupWindow != null) {
                    MultiImageSelectorFragment.this.setFolderPopupWindowSize();
                }
                if (VERSION.SDK_INT >= 16) {
                    MultiImageSelectorFragment.this.mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    MultiImageSelectorFragment.this.mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.folderPopWindwoHeight = (int) (((double) metrics.heightPixels) * 0.6d);
        this.folderPopWindowWidth = metrics.widthPixels;
        super.onConfigurationChanged(newConfig);
    }

    private void showCameraAction() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            cameraIntent.putExtra("output", Uri.fromFile(this.mTmpFile));
            startActivityForResult(cameraIntent, 100);
            return;
        }
        Toast.makeText(getActivity(), R.string.msg_no_camera, 0).show();
    }

    public void refreshSelectState() {
        refreshPreviewBtn();
        ((MultiImageSelectorActivity) getActivity()).refreshSubmitBtnState();
        if (resultList != null) {
            this.mImageAdapter.setDefaultSelected(resultList);
            this.mImageAdapter.notifyDataSetChanged();
        }
    }

    public void refreshPreviewBtn() {
        if (resultList == null || resultList.size() <= 0) {
            this.mPreviewBtn.setText(R.string.preview);
            this.mPreviewBtn.setEnabled(false);
            return;
        }
        this.mPreviewBtn.setEnabled(true);
        this.mPreviewBtn.setText(getResources().getString(R.string.preview) + SocializeConstants.OP_OPEN_PAREN + resultList.size() + SocializeConstants.OP_CLOSE_PAREN);
    }
}
