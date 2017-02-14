package com.boohee.nice.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.nice.model.NiceMessage;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.status.LargeImageActivity;
import com.boohee.utility.Const;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLineUtility;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class AdviserAdapter extends SimpleBaseAdapter<NiceMessage> {
    private Context context;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private String mAdviserAvatar;
    private String mAdviserName;
    private String mUserAvatar;
    private String mUserName;

    public AdviserAdapter(Context context, List<NiceMessage> data) {
        super(context, data);
        this.context = context;
    }

    public int getItemResource() {
        return R.layout.ia;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView avatar = (CircleImageView) holder.getView(R.id.avatar);
        TextView tv_user_name = (TextView) holder.getView(R.id.tv_user_name);
        TextView tv_create_at = (TextView) holder.getView(R.id.tv_create_at);
        TextView tv_content = (TextView) holder.getView(R.id.tv_content);
        ImageView iv_image = (ImageView) holder.getView(R.id.iv_image);
        final NiceMessage msg = (NiceMessage) this.data.get(position);
        if (msg != null) {
            if (Const.USER.equals(msg.sender_type)) {
                tv_user_name.setText(this.mUserName);
                this.imageLoader.displayImage(this.mUserAvatar, avatar, ImageLoaderOptions.avatar
                        ());
            } else {
                tv_user_name.setText(this.mAdviserName);
                this.imageLoader.displayImage(this.mAdviserAvatar, avatar, ImageLoaderOptions
                        .avatar());
            }
            tv_create_at.setText(msg.create_at);
            if ("text".equals(msg.msg_type)) {
                tv_content.setText(msg.content);
                tv_content.setVisibility(0);
                iv_image.setVisibility(8);
            } else {
                this.imageLoader.displayImage(msg.content, iv_image);
                tv_content.setVisibility(8);
                iv_image.setVisibility(0);
                iv_image.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        LargeImageActivity.start(AdviserAdapter.this.context, msg.content);
                    }
                });
            }
        }
        TimeLineUtility.addLinks(tv_content);
        return convertView;
    }

    public void setUserInfo(String userName, String userAvatar) {
        this.mUserName = userName;
        this.mUserAvatar = userAvatar;
    }

    public void setAdviserInfo(String adviserName, String adviserAvatar) {
        this.mAdviserName = adviserName;
        this.mAdviserAvatar = adviserAvatar;
    }
}
