package cn.sharesdk.onekeyshare.theme.skyblue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.onekeyshare.FollowerListFakeActivity;
import cn.sharesdk.onekeyshare.FollowerListFakeActivity.FollowersResult;
import cn.sharesdk.onekeyshare.FollowerListFakeActivity.Following;
import com.boohee.one.video.fragment.SportPlanFragment;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.gui.BitmapProcessor;
import com.mob.tools.gui.PullToRefreshListAdapter;
import com.mob.tools.gui.PullToRefreshView;
import com.mob.tools.utils.R;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class FollowListPage extends FollowerListFakeActivity implements OnClickListener, OnItemClickListener {
    private FollowAdapter adapter;
    private int lastPosition = -1;
    private TitleLayout llTitle;

    private static class FollowAdapter extends PullToRefreshListAdapter implements PlatformActionListener, Callback {
        private static final int FOLLOW_LIST_EMPTY = 2;
        private Bitmap bmChd;
        private Bitmap bmUnch;
        private int curPage = -1;
        private ArrayList<Following> follows = new ArrayList();
        private boolean hasNext = true;
        private PRTHeader llHeader = new PRTHeader(getContext());
        private HashMap<String, Boolean> map = new HashMap();
        private Platform platform;

        public FollowAdapter(PullToRefreshView view) {
            super(view);
            int resId = R.getBitmapRes(getContext(), "auth_follow_cb_chd");
            if (resId > 0) {
                this.bmChd = BitmapFactory.decodeResource(view.getResources(), resId);
            }
            resId = R.getBitmapRes(getContext(), "auth_follow_cb_unc");
            if (resId > 0) {
                this.bmUnch = BitmapFactory.decodeResource(view.getResources(), resId);
            }
        }

        public void setPlatform(Platform platform) {
            this.platform = platform;
            platform.setPlatformActionListener(this);
        }

        private void next() {
            if (this.hasNext) {
                this.platform.listFriend(15, this.curPage + 1, null);
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            FollowListItem item;
            boolean simpleMode = "FacebookMessenger".equals(this.platform.getName());
            if (convertView == null) {
                View llItem = new LinearLayout(parent.getContext());
                item = new FollowListItem();
                llItem.setTag(item);
                convertView = llItem;
                int dp_52 = R.dipToPx(getContext(), 52);
                int dp_10 = R.dipToPx(parent.getContext(), 10);
                int dp_5 = R.dipToPx(parent.getContext(), 5);
                if (!simpleMode) {
                    item.aivIcon = new AsyncImageView(getContext());
                    LayoutParams lpIcon = new LayoutParams(dp_52, dp_52);
                    lpIcon.gravity = 16;
                    lpIcon.setMargins(dp_10, dp_5, dp_10, dp_5);
                    item.aivIcon.setLayoutParams(lpIcon);
                    llItem.addView(item.aivIcon);
                }
                LinearLayout llText = new LinearLayout(parent.getContext());
                llText.setPadding(0, dp_10, dp_10, dp_10);
                llText.setOrientation(1);
                LayoutParams lpText = new LayoutParams(-2, -2);
                lpText.gravity = 16;
                lpText.weight = 1.0f;
                llText.setLayoutParams(lpText);
                llItem.addView(llText);
                item.tvName = new TextView(parent.getContext());
                item.tvName.setTextColor(-16777216);
                item.tvName.setTextSize(1, 18.0f);
                item.tvName.setSingleLine();
                if (simpleMode) {
                    item.tvName.setPadding(dp_10, 0, 0, 0);
                }
                llText.addView(item.tvName);
                if (!simpleMode) {
                    item.tvSign = new TextView(parent.getContext());
                    item.tvSign.setTextColor(2130706432);
                    item.tvSign.setTextSize(1, 14.0f);
                    item.tvSign.setSingleLine();
                    llText.addView(item.tvSign);
                }
                item.ivCheck = new ImageView(parent.getContext());
                item.ivCheck.setPadding(0, 0, dp_10, 0);
                LayoutParams lpCheck = new LayoutParams(-2, -2);
                lpCheck.gravity = 16;
                item.ivCheck.setLayoutParams(lpCheck);
                llItem.addView(item.ivCheck);
            } else {
                item = (FollowListItem) convertView.getTag();
            }
            Following following = getItem(position);
            item.tvName.setText(following.screenName);
            if (!simpleMode) {
                item.tvSign.setText(following.description);
            }
            item.ivCheck.setImageBitmap(following.checked ? this.bmChd : this.bmUnch);
            if (!simpleMode) {
                if (isFling()) {
                    Bitmap bm = BitmapProcessor.getBitmapFromCache(following.icon);
                    if (bm == null || bm.isRecycled()) {
                        item.aivIcon.execute(null, 0);
                    } else {
                        item.aivIcon.setImageBitmap(bm);
                    }
                } else {
                    item.aivIcon.execute(following.icon, 0);
                }
            }
            if (position == getCount() - 1) {
                next();
            }
            return convertView;
        }

        public Following getItem(int position) {
            return (Following) this.follows.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public int getCount() {
            return this.follows == null ? 0 : this.follows.size();
        }

        public View getHeaderView() {
            return this.llHeader;
        }

        public void onPullDown(int percent) {
            this.llHeader.onPullDown(percent);
        }

        public void onRequest() {
            this.llHeader.onRequest();
            this.curPage = -1;
            this.hasNext = true;
            this.map.clear();
            next();
        }

        public void onCancel(Platform plat, int action) {
            UIHandler.sendEmptyMessage(-1, this);
        }

        public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
            FollowersResult followersResult = FollowerListFakeActivity.parseFollowers(this.platform.getName(), res, this.map);
            if (followersResult == null) {
                UIHandler.sendEmptyMessage(2, this);
                return;
            }
            this.hasNext = followersResult.hasNextPage;
            if (followersResult.list != null && followersResult.list.size() > 0) {
                this.curPage++;
                Message msg = new Message();
                msg.what = 1;
                msg.obj = followersResult.list;
                UIHandler.sendMessage(msg, this);
            }
        }

        public void onError(Platform plat, int action, Throwable t) {
            t.printStackTrace();
        }

        public boolean handleMessage(Message msg) {
            if (msg.what < 0) {
                ((Activity) getContext()).finish();
            } else if (msg.what == 2) {
                notifyDataSetChanged();
            } else {
                if (this.curPage <= 0) {
                    this.follows.clear();
                }
                this.follows.addAll(msg.obj);
                notifyDataSetChanged();
            }
            return false;
        }

        public void onReversed() {
            super.onReversed();
            this.llHeader.reverse();
        }
    }

    private static class FollowListItem {
        public AsyncImageView aivIcon;
        public ImageView ivCheck;
        public TextView tvName;
        public TextView tvSign;

        private FollowListItem() {
        }
    }

    private static class PRTHeader extends LinearLayout {
        private RotateImageView ivArrow;
        private ProgressBar pbRefreshing;
        private TextView tvHeader;

        public PRTHeader(Context context) {
            super(context);
            setOrientation(1);
            LinearLayout llInner = new LinearLayout(context);
            LayoutParams lpInner = new LayoutParams(-2, -2);
            lpInner.gravity = 1;
            addView(llInner, lpInner);
            this.ivArrow = new RotateImageView(context);
            int resId = R.getBitmapRes(context, "ssdk_oks_ptr_ptr");
            if (resId > 0) {
                this.ivArrow.setImageResource(resId);
            }
            int dp_32 = R.dipToPx(context, 32);
            LayoutParams lpIv = new LayoutParams(dp_32, dp_32);
            lpIv.gravity = 16;
            llInner.addView(this.ivArrow, lpIv);
            this.pbRefreshing = new ProgressBar(context);
            llInner.addView(this.pbRefreshing, lpIv);
            this.pbRefreshing.setVisibility(8);
            this.tvHeader = new TextView(getContext());
            this.tvHeader.setTextSize(1, 18.0f);
            this.tvHeader.setGravity(17);
            int dp_10 = R.dipToPx(getContext(), 10);
            this.tvHeader.setPadding(dp_10, dp_10, dp_10, dp_10);
            this.tvHeader.setTextColor(-16777216);
            LayoutParams lpTv = new LayoutParams(-2, -2);
            lpTv.gravity = 16;
            llInner.addView(this.tvHeader, lpTv);
        }

        public void onPullDown(int percent) {
            if (percent > 100) {
                int degree = ((percent - 100) * 180) / 20;
                if (degree > 180) {
                    degree = 180;
                }
                if (degree < 0) {
                    degree = 0;
                }
                this.ivArrow.setRotation(degree);
            } else {
                this.ivArrow.setRotation(0);
            }
            int resId;
            if (percent < 100) {
                resId = R.getStringRes(getContext(), "pull_to_refresh");
                if (resId > 0) {
                    this.tvHeader.setText(resId);
                    return;
                }
                return;
            }
            resId = R.getStringRes(getContext(), "release_to_refresh");
            if (resId > 0) {
                this.tvHeader.setText(resId);
            }
        }

        public void onRequest() {
            this.ivArrow.setVisibility(8);
            this.pbRefreshing.setVisibility(0);
            int resId = R.getStringRes(getContext(), "refreshing");
            if (resId > 0) {
                this.tvHeader.setText(resId);
            }
        }

        public void reverse() {
            this.pbRefreshing.setVisibility(8);
            this.ivArrow.setRotation(180);
            this.ivArrow.setVisibility(0);
        }
    }

    private static class RotateImageView extends ImageView {
        private int rotation;

        public RotateImageView(Context context) {
            super(context);
        }

        public void setRotation(int degree) {
            this.rotation = degree;
            invalidate();
        }

        protected void onDraw(Canvas canvas) {
            canvas.rotate((float) this.rotation, (float) (getWidth() / 2), (float) (getHeight() / 2));
            super.onDraw(canvas);
        }
    }

    public void onCreate() {
        LinearLayout llPage = new LinearLayout(getContext());
        llPage.setBackgroundColor(-657931);
        llPage.setOrientation(1);
        this.activity.setContentView(llPage);
        this.llTitle = new TitleLayout(getContext());
        int resId = R.getBitmapRes(getContext(), "title_back");
        if (resId > 0) {
            this.llTitle.setBackgroundResource(resId);
        }
        this.llTitle.getBtnBack().setOnClickListener(this);
        resId = R.getStringRes(getContext(), "multi_share");
        if (resId > 0) {
            this.llTitle.getTvTitle().setText(resId);
        }
        this.llTitle.getBtnRight().setVisibility(0);
        resId = R.getStringRes(getContext(), SportPlanFragment.COURSE_STATUS_FINISH);
        if (resId > 0) {
            this.llTitle.getBtnRight().setText(resId);
        }
        this.llTitle.getBtnRight().setOnClickListener(this);
        this.llTitle.setLayoutParams(new LayoutParams(-1, -2));
        llPage.addView(this.llTitle);
        FrameLayout flPage = new FrameLayout(getContext());
        LayoutParams lpFl = new LayoutParams(-1, -2);
        lpFl.weight = 1.0f;
        flPage.setLayoutParams(lpFl);
        llPage.addView(flPage);
        PullToRefreshView followList = new PullToRefreshView(getContext());
        followList.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        flPage.addView(followList);
        this.adapter = new FollowAdapter(followList);
        this.adapter.setPlatform(this.platform);
        followList.setAdapter(this.adapter);
        this.adapter.getListView().setOnItemClickListener(this);
        ImageView ivShadow = new ImageView(getContext());
        resId = R.getBitmapRes(getContext(), "title_shadow");
        if (resId > 0) {
            ivShadow.setBackgroundResource(resId);
        }
        ivShadow.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        flPage.addView(ivShadow);
        followList.performPulling(true);
    }

    public void onClick(View v) {
        if (v.equals(this.llTitle.getBtnRight())) {
            ArrayList<String> selected = new ArrayList();
            int size = this.adapter.getCount();
            for (int i = 0; i < size; i++) {
                if (this.adapter.getItem(i).checked) {
                    selected.add(this.adapter.getItem(i).atName);
                }
            }
            setResultForChecked(selected);
        }
        finish();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        boolean z = false;
        if (isRadioMode(this.platform.getName())) {
            if (this.lastPosition >= 0) {
                this.adapter.getItem(this.lastPosition).checked = false;
            }
            this.lastPosition = position;
        }
        Following following = this.adapter.getItem(position);
        if (!following.checked) {
            z = true;
        }
        following.checked = z;
        this.adapter.notifyDataSetChanged();
    }
}
