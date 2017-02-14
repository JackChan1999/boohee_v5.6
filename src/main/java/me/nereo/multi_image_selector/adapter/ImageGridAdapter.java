package me.nereo.multi_image_selector.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.MultiImageSelectorFragment;
import me.nereo.multi_image_selector.R;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.utils.MultiImageLoader;
import me.nereo.multi_image_selector.utils.MultiImageSelector;

public class ImageGridAdapter extends BaseAdapter {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;
    private MultiImageSelectorActivity mActivity;
    private MultiImageLoader mImageLoader;
    private List<Image> mImages = new ArrayList();
    private LayoutInflater mInflater;
    private LayoutParams mItemLayoutParams;
    private int mItemSize;
    private int mMaxImageNum = 1;
    private List<String> mResultList;
    private List<Image> mSelectedImages = new ArrayList();
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private class CheckClickListener implements OnClickListener {
        private int position;

        public CheckClickListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {
            if (ImageGridAdapter.this.mActivity != null) {
                ImageGridAdapter.this.select((Image) ImageGridAdapter.this.mImages.get(this.position));
                ImageGridAdapter.this.mActivity.refreshSubmitBtnState();
                MultiImageSelectorFragment fragment = (MultiImageSelectorFragment) ImageGridAdapter.this.mActivity.getSupportFragmentManager().findFragmentByTag(MultiImageSelectorFragment.TAG);
                if (fragment != null) {
                    fragment.refreshPreviewBtn();
                }
            }
        }
    }

    class ViewHolde {
        ImageView image;
        ImageView indicator;

        ViewHolde(View view) {
            this.image = (ImageView) view.findViewById(R.id.image);
            this.indicator = (ImageView) view.findViewById(R.id.checkmark);
            view.setTag(this);
        }

        void bindData(Image data) {
            if (data != null) {
                if (ImageGridAdapter.this.showSelectIndicator) {
                    this.indicator.setVisibility(0);
                    if (ImageGridAdapter.this.mSelectedImages.contains(data)) {
                        this.indicator.setImageResource(R.drawable.btn_selected);
                        this.image.setColorFilter(Color.parseColor("#77000000"));
                    } else {
                        this.indicator.setImageResource(R.drawable.btn_unselected);
                        this.image.setColorFilter(null);
                    }
                } else {
                    this.indicator.setVisibility(8);
                }
                Uri uri = ImageGridAdapter.this.getPicUri(data.id);
                if (ImageGridAdapter.this.mItemSize > 0 && ImageGridAdapter.this.mImageLoader != null) {
                    ImageGridAdapter.this.mImageLoader.loadImage(uri.toString(), this.image, ImageGridAdapter.this.mItemSize, ImageGridAdapter.this.mItemSize, R.drawable.default_error);
                }
            }
        }
    }

    public ImageGridAdapter(Context context, boolean showCamera, int mMaxImageNum) {
        this.mMaxImageNum = mMaxImageNum;
        this.mActivity = (MultiImageSelectorActivity) context;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.showCamera = showCamera;
        this.mItemLayoutParams = new LayoutParams(-1, -1);
        this.mImageLoader = MultiImageSelector.getImageLoader();
    }

    public void showSelectIndicator(boolean b) {
        this.showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (this.showCamera != b) {
            this.showCamera = b;
            notifyDataSetChanged();
        }
    }

    public boolean isShowCamera() {
        return this.showCamera;
    }

    public void select(Image image) {
        if (this.mSelectedImages.contains(image)) {
            this.mSelectedImages.remove(image);
        } else if (this.mResultList.size() != this.mMaxImageNum) {
            this.mSelectedImages.add(image);
        }
        if (this.mResultList.contains(image.path)) {
            this.mResultList.remove(image.path);
        } else if (this.mResultList.size() == this.mMaxImageNum) {
            Toast.makeText(this.mActivity, R.string.msg_amount_limit, 0).show();
            return;
        } else {
            this.mResultList.add(image.path);
        }
        notifyDataSetChanged();
    }

    public void setDefaultSelected(ArrayList<String> resultList) {
        if (resultList != null) {
            this.mResultList = resultList;
            this.mSelectedImages.clear();
            Iterator i$ = resultList.iterator();
            while (i$.hasNext()) {
                Image image = getImageByPath((String) i$.next());
                if (image != null) {
                    this.mSelectedImages.add(image);
                }
            }
        }
    }

    private Image getImageByPath(String path) {
        if (this.mImages != null && this.mImages.size() > 0) {
            for (Image image : this.mImages) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }

    public void setData(List<Image> images) {
        this.mSelectedImages.clear();
        if (images == null || images.size() <= 0) {
            this.mImages.clear();
        } else {
            this.mImages = images;
        }
        notifyDataSetChanged();
    }

    public void setItemSize(int columnWidth) {
        if (this.mItemSize != columnWidth) {
            this.mItemSize = columnWidth;
            this.mItemLayoutParams = new LayoutParams(this.mItemSize, this.mItemSize);
            notifyDataSetChanged();
        }
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if (this.showCamera && position == 0) {
            return 0;
        }
        return 1;
    }

    public int getCount() {
        return this.showCamera ? this.mImages.size() + 1 : this.mImages.size();
    }

    public Image getItem(int i) {
        if (!this.showCamera) {
            return (Image) this.mImages.get(i);
        }
        if (i == 0) {
            return null;
        }
        return (Image) this.mImages.get(i - 1);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        if (type == 0) {
            view = this.mInflater.inflate(R.layout.list_item_camera, viewGroup, false);
            view.setTag(null);
        } else if (type == 1) {
            ViewHolde holde;
            if (view == null) {
                view = this.mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                holde = new ViewHolde(view);
            } else {
                holde = (ViewHolde) view.getTag();
                if (holde == null) {
                    view = this.mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                    holde = new ViewHolde(view);
                }
            }
            holde.bindData(getItem(i));
            ImageView imageView = holde.indicator;
            if (this.showCamera) {
                i--;
            }
            imageView.setOnClickListener(new CheckClickListener(i));
        }
        if (((LayoutParams) view.getLayoutParams()).height != this.mItemSize) {
            view.setLayoutParams(this.mItemLayoutParams);
        }
        return view;
    }

    private Uri getPicUri(String id) {
        return Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, id);
    }
}
