package com.boohee.status;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.api.StatusApi;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import org.json.JSONObject;

public class FriendsAdapter extends BaseAdapter {
    static final String TAG = FriendsAdapter.class.getSimpleName();
    private Context          mContext;
    private ImageLoader      mImageLoader;
    private List<StatusUser> mUsers;
    private String           type;

    private class OnFollowingListener implements OnClickListener {
        private StatusUser user;

        public OnFollowingListener(StatusUser user) {
            this.user = user;
        }

        public void onClick(final View v) {
            if (this.user.following) {
                StatusApi.deleteFriendships(FriendsAdapter.this.mContext, this.user.id, new
                        JsonCallback((Activity) FriendsAdapter.this.mContext) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        OnFollowingListener.this.user.following = false;
                        ((Button) v).setText(R.string.m9);
                        v.setSelected(false);
                    }
                });
            } else {
                StatusApi.createFriendships(FriendsAdapter.this.mContext, this.user.id, new
                        JsonCallback((Activity) FriendsAdapter.this.mContext) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        OnFollowingListener.this.user.following = true;
                        ((Button) v).setText(R.string.bm);
                        v.setSelected(true);
                    }
                });
            }
        }
    }

    static class ViewHolder {
        public ImageView    avatarImage;
        public TextView     descriptionText;
        public ToggleButton followingBtn;
        public TextView     userNameText;

        ViewHolder() {
        }
    }

    public FriendsAdapter(Context context, List<StatusUser> users) {
        this(context, users, "following");
    }

    public FriendsAdapter(Context context, List<StatusUser> users, String type) {
        this.mImageLoader = ImageLoader.getInstance();
        this.mContext = context;
        this.mUsers = users;
        this.type = type;
    }

    public int getCount() {
        if (this.mUsers == null) {
            return 0;
        }
        return this.mUsers.size();
    }

    public StatusUser getItem(int position) {
        return (StatusUser) this.mUsers.get(position);
    }

    public long getItemId(int position) {
        return (long) getItem(position).id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.gw, null);
            holder.avatarImage = (ImageView) convertView.findViewById(R.id.avatar_image);
            holder.userNameText = (TextView) convertView.findViewById(R.id.user_name);
            holder.descriptionText = (TextView) convertView.findViewById(R.id.description);
            holder.followingBtn = (ToggleButton) convertView.findViewById(R.id.following_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StatusUser user = getItem(position);
        holder.userNameText.setText(user.nickname);
        holder.descriptionText.setText("BMI " + String.valueOf(user.getBmi()));
        this.mImageLoader.displayImage(user.avatar_url, holder.avatarImage, ImageLoaderOptions
                .avatar());
        if ("follower".equals(this.type)) {
            holder.followingBtn.setVisibility(0);
            if (user.following) {
                holder.followingBtn.setChecked(true);
            } else {
                holder.followingBtn.setChecked(false);
            }
            holder.followingBtn.setOnClickListener(new OnFollowingListener(user));
        } else if ("following".equals(this.type)) {
            holder.followingBtn.setVisibility(8);
        }
        return convertView;
    }
}
