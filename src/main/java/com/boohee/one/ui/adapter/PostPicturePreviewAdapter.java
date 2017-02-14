package com.boohee.one.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.boohee.one.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.List;

public class PostPicturePreviewAdapter extends BaseAdapter {
    public static final String KEY_ADD = "add";
    private List<String>        mDatas;
    private DisplayImageOptions mDisplayOptions;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private int            mItemSize;
    private LayoutInflater mLi;
    private int            maxPicNums;

    private class DelListener implements OnClickListener {
        private int position;

        public DelListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {
            PostPicturePreviewAdapter.this.mDatas.remove(this.position);
            if (PostPicturePreviewAdapter.this.mDatas.size() == 1) {
                PostPicturePreviewAdapter.this.mDatas.clear();
            }
            PostPicturePreviewAdapter.this.notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        Button    btn;
        ImageView iv;

        ViewHolder() {
        }
    }

    public PostPicturePreviewAdapter(Context context, List<String> data, int maxPicNums, int
            itemSize) {
        this.mDatas = data;
        this.maxPicNums = maxPicNums;
        this.mLi = LayoutInflater.from(context);
        this.mItemSize = itemSize;
        initDisplayOptions();
    }

    private void initDisplayOptions() {
        Builder builder = new Builder();
        builder.bitmapConfig(Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).cacheInMemory
                (true).cacheOnDisk(true).showImageForEmptyUri((int) R.color.in).showImageOnFail(
                (int) R.color.in).showImageOnLoading((int) R.color.in);
        this.mDisplayOptions = builder.build();
    }

    public int getCount() {
        return this.mDatas.size() < this.maxPicNums ? this.mDatas.size() : this.maxPicNums;
    }

    public Object getItem(int position) {
        return this.mDatas.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.mLi.inflate(R.layout.jl, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.btn = (Button) convertView.findViewById(R.id.btn_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setImageViewSize(holder.iv);
        String path = (String) this.mDatas.get(position);
        if (KEY_ADD.equals(path)) {
            this.mImageLoader.displayImage("drawable://" + R.drawable.qv, holder.iv, this
                    .mDisplayOptions);
            holder.btn.setVisibility(8);
        } else {
            holder.btn.setVisibility(0);
            loadImage(holder.iv, Uri.fromFile(new File(path)));
        }
        holder.btn.setOnClickListener(new DelListener(position));
        return convertView;
    }

    private void setImageViewSize(ImageView iv) {
        iv.getLayoutParams().width = this.mItemSize;
        iv.getLayoutParams().height = this.mItemSize;
        iv.setScaleType(ScaleType.CENTER_CROP);
    }

    private void loadImage(ImageView imageView, Uri imageUri) {
        if (imageUri != null && imageView != null && imageView.getTag() != imageUri) {
            imageView.setTag(imageUri);
            this.mImageLoader.displayImage(Uri.decode(imageUri.toString()), imageView, this.mDisplayOptions);
        }
    }
}
