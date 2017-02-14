package com.boohee.apn;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.status.Photo;
import com.boohee.one.R;
import com.boohee.one.ui.NineGridGalleryActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends Adapter<ViewHolder> {
    private static final int TYPE_QUESTION = 1;
    private static final int TYPE_REPLY    = 0;
    private Activity         mActivity;
    private List<ApnContent> mDataList;
    private ImageLoader     mImageLoader = ImageLoader.getInstance();
    private OnClickListener mOnClick     = new OnClickListener() {
        public void onClick(View v) {
            List<PhotoModel> obj = v.getTag();
            if (obj != null) {
                List<PhotoModel> origin = obj;
                List<Photo> photos = new ArrayList();
                for (PhotoModel model : origin) {
                    Photo photo = new Photo();
                    photo.big_url = model.origin_image_url;
                    photo.small_url = model.thumb_image_url;
                    photos.add(photo);
                }
                NineGridGalleryActivity.comeOn(ContentAdapter.this.mActivity, photos, 0);
            }
        }
    };

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public static List<ImageView> iv_list = new ArrayList();
        public TextView  tv_content;
        public TextView  tv_date;
        public ViewGroup view_images;

        public ViewHolder(View view) {
            super(view);
            this.tv_date = (TextView) view.findViewById(R.id.tv_date);
            this.tv_content = (TextView) view.findViewById(R.id.tv_content);
            this.view_images = (ViewGroup) view.findViewById(R.id.view_images);
            iv_list.clear();
            iv_list.add((ImageView) view.findViewById(R.id.iv_1));
            iv_list.add((ImageView) view.findViewById(R.id.iv_2));
            iv_list.add((ImageView) view.findViewById(R.id.iv_3));
        }
    }

    public ContentAdapter(Activity activity, List<ApnContent> dataList) {
        this.mActivity = activity;
        this.mDataList = dataList;
    }

    public int getItemViewType(int position) {
        return ((ApnContent) this.mDataList.get(position)).is_question ? 1 : 0;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(viewType == 1 ?
                R.layout.hh : R.layout.hi, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        boolean hasPhoto;
        int i;
        viewHolder.setIsRecyclable(false);
        final ApnContent item = (ApnContent) this.mDataList.get(index);
        viewHolder.tv_date.setText(DateFormatUtils.timezoneFormat(item.date, "yyyy-MM-dd " +
                "HH:mm:ss"));
        viewHolder.tv_content.setText(item.content);
        TimeLineUtility.addLinks(viewHolder.tv_content);
        viewHolder.tv_content.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                TimeLineUtility.copyText(ContentAdapter.this.mActivity, item.content);
                Helper.showToast((CharSequence) "内容已复制到剪切板");
                return true;
            }
        });
        if (item.photos == null || item.photos.size() <= 0) {
            hasPhoto = false;
        } else {
            hasPhoto = true;
        }
        ViewGroup viewGroup = viewHolder.view_images;
        if (hasPhoto) {
            i = 0;
        } else {
            i = 8;
        }
        viewGroup.setVisibility(i);
        if (hasPhoto) {
            List<PhotoModel> photos = item.photos;
            int i2 = 0;
            while (i2 < photos.size()) {
                String url = ((PhotoModel) photos.get(i2)).thumb_image_url;
                ImageView imageView = (ImageView) ViewHolder.iv_list.get(i2);
                imageView.setVisibility(0);
                imageView.setTag(photos);
                imageView.setOnClickListener(this.mOnClick);
                this.mImageLoader.displayImage(url, imageView, ImageLoaderOptions.noImage());
                i2++;
            }
            while (i2 < 3) {
                ((ImageView) ViewHolder.iv_list.get(i2)).setImageDrawable(null);
                ((ImageView) ViewHolder.iv_list.get(i2)).setVisibility(8);
                i2++;
            }
        }
    }

    public int getItemCount() {
        return this.mDataList.size();
    }
}
