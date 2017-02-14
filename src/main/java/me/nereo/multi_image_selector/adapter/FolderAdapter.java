package me.nereo.multi_image_selector.adapter;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.nereo.multi_image_selector.R;
import me.nereo.multi_image_selector.bean.Folder;
import me.nereo.multi_image_selector.utils.MultiImageSelector;

public class FolderAdapter extends BaseAdapter {
    int lastSelected = 0;
    private Context mContext;
    private List<Folder> mFolders = new ArrayList();
    int mImageSize;
    private LayoutInflater mInflater;

    class ViewHolder {
        ImageView cover;
        ImageView indicator;
        TextView name;
        TextView size;

        ViewHolder(View view) {
            this.cover = (ImageView) view.findViewById(R.id.cover);
            this.name = (TextView) view.findViewById(R.id.name);
            this.size = (TextView) view.findViewById(R.id.size);
            this.indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            this.name.setText(data.name);
            this.size.setText(data.images.size() + "张");
            MultiImageSelector.getImageLoader().loadImage(FolderAdapter.this.getPicUri(data.cover.id).toString(), this.cover, FolderAdapter.this.mImageSize, FolderAdapter.this.mImageSize, R.drawable.default_error);
        }
    }

    public FolderAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mImageSize = this.mContext.getResources().getDimensionPixelOffset(R.dimen.folder_cover_size);
    }

    public void setData(List<Folder> folders) {
        if (folders == null || folders.size() <= 0) {
            this.mFolders.clear();
        } else {
            this.mFolders = folders;
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mFolders.size() + 1;
    }

    public Folder getItem(int i) {
        if (i == 0) {
            return null;
        }
        return (Folder) this.mFolders.get(i - 1);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if (i == 0) {
                holder.name.setText("所有图片");
                holder.size.setText(getTotalImageSize() + "张");
                if (this.mFolders.size() > 0) {
                    MultiImageSelector.getImageLoader().loadImage(Uri.decode(Uri.fromFile(new File(((Folder) this.mFolders.get(0)).cover.path)).toString()), holder.cover, this.mImageSize, this.mImageSize, R.drawable.default_error);
                }
            } else {
                holder.bindData(getItem(i));
            }
            if (this.lastSelected == i) {
                holder.indicator.setVisibility(0);
            } else {
                holder.indicator.setVisibility(8);
            }
        }
        return view;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (this.mFolders != null && this.mFolders.size() > 0) {
            for (Folder f : this.mFolders) {
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (this.lastSelected != i) {
            this.lastSelected = i;
            notifyDataSetChanged();
        }
    }

    public int getSelectIndex() {
        return this.lastSelected;
    }

    private Uri getPicUri(String id) {
        return Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, id);
    }
}
