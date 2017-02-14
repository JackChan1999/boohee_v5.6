package com.boohee.status;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.widgets.SearchBarView;
import com.boohee.widgets.SearchBarView.OnFinishSearchListener;
import com.boohee.widgets.SearchBarView.OnSearchListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsActivity extends GestureActivity {
    static final String  TAG           = ContactsActivity.class.getSimpleName();
    private      boolean isLastVisible = false;
    private      boolean isSearch      = false;
    private ContactsAdapter       mAdapter;
    private PullToRefreshListView mPullRefreshListView;
    private ArrayList<StatusUser> mUsers;
    private int page = 1;
    private SearchBarView         searchBarView;
    private String                searchString;
    private ArrayList<StatusUser> temps;

    @SuppressLint({"UseSparseArrays"})
    private class ContactsAdapter extends BaseAdapter {
        private ImageLoader          mImageLoader = ImageLoader.getInstance();
        private SparseArray<Boolean> selectedList = new SparseArray();

        public ContactsAdapter() {
            for (int i = 0; i < ContactsActivity.this.mUsers.size(); i++) {
                this.selectedList.put(i, Boolean.valueOf(false));
            }
        }

        public int getCount() {
            return ContactsActivity.this.mUsers.size();
        }

        public StatusUser getItem(int position) {
            return (StatusUser) ContactsActivity.this.mUsers.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ContactsActivity.this.ctx).inflate(R.layout.hm,
                        null);
                holder.iv_avatar = (ImageView) convertView.findViewById(R.id.avatar_image);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            StatusUser user = getItem(position);
            holder.tv_name.setText(user.nickname);
            holder.tv_description.setText(user.description);
            this.mImageLoader.displayImage(user.avatar_url, holder.iv_avatar, ImageLoaderOptions
                    .avatar());
            holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Helper.showLog(ContactsActivity.TAG, "isChecked:" + isChecked);
                    ContactsAdapter.this.selectedList.put(position, Boolean.valueOf(isChecked));
                }
            });
            if (!(this.selectedList == null || this.selectedList.get(position) == null)) {
                holder.checkbox.setChecked(((Boolean) this.selectedList.get(position))
                        .booleanValue());
            }
            return convertView;
        }

        public String getSelectedNickname() {
            String nickName = "";
            if (this.selectedList == null || this.selectedList.size() == 0 || ContactsActivity
                    .this.mUsers == null || ContactsActivity.this.mUsers.size() == 0) {
                return nickName;
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < ContactsActivity.this.mUsers.size()) {
                try {
                    if (((Boolean) this.selectedList.get(i, Boolean.valueOf(false))).booleanValue
                            () && ContactsActivity.this.mUsers.get(i) != null) {
                        sb.append("@");
                        sb.append(((StatusUser) ContactsActivity.this.mUsers.get(i)).nickname);
                        sb.append(" ");
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        public SparseArray<Boolean> getSelectedList() {
            return this.selectedList;
        }
    }

    static class ViewHolder {
        public CheckBox  checkbox;
        public ImageView iv_avatar;
        public TextView  tv_description;
        public TextView  tv_name;

        ViewHolder() {
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.av);
        setTitle(R.string.gx);
        init();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.y8).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent();
                intent.putExtra("contact", this.mAdapter == null ? "" : this.mAdapter
                        .getSelectedNickname());
                setResult(-1, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (ContactsActivity.this.mAdapter.getSelectedList() != null && ContactsActivity
                        .this.mAdapter.getSelectedList().get(position - 1) != null) {
                    ContactsActivity.this.mAdapter.getSelectedList().put(position - 1, Boolean
                            .valueOf(!((Boolean) ContactsActivity.this.mAdapter.getSelectedList()
                                    .get(position + -1)).booleanValue()));
                    ContactsActivity.this.mAdapter.notifyDataSetChanged();
                }
            }
        });
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (ContactsActivity.this.isSearch) {
                    ContactsActivity.this.searchFollowings();
                } else {
                    ContactsActivity.this.getFollowings();
                }
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!ContactsActivity.this.isLastVisible) {
                    ContactsActivity.this.getNextFriends();
                }
            }
        });
        this.searchBarView = (SearchBarView) findViewById(R.id.search_bar);
        this.searchBarView.setHint(getResources().getString(R.string.lz));
        this.searchBarView.setSearchListener(new OnSearchListener() {
            public void startSearch(String q) {
                ContactsActivity.this.isSearch = true;
                ContactsActivity.this.searchString = q;
                ContactsActivity.this.searchFollowings();
            }
        });
        this.searchBarView.setFinishSearchListener(new OnFinishSearchListener() {
            public void finishSearch() {
                ContactsActivity.this.isSearch = false;
                ContactsActivity.this.searchString = "";
                if (ContactsActivity.this.mUsers != null) {
                    ContactsActivity.this.mUsers.clear();
                    ContactsActivity.this.mUsers.addAll(ContactsActivity.this.temps);
                }
                if (ContactsActivity.this.mAdapter != null) {
                    ContactsActivity.this.mAdapter.notifyDataSetChanged();
                }
            }
        });
        firstLoad();
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ContactsActivity.this.mPullRefreshListView.setRefreshing();
            }
        }, 500);
    }

    private void getFollowings() {
        this.page = 1;
        StatusApi.getFollowings(this.activity, this.page, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    JSONArray usersArray = object.getJSONArray("followings");
                    ContactsActivity.this.mUsers = StatusUser.parseUsers(usersArray.toString());
                    ContactsActivity.this.temps = StatusUser.parseUsers(usersArray.toString());
                    ContactsActivity.this.mAdapter = new ContactsAdapter();
                    ContactsActivity.this.mPullRefreshListView.setAdapter(ContactsActivity.this
                            .mAdapter);
                    ContactsActivity.this.page = ContactsActivity.this.page + 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                ContactsActivity.this.dismissLoading();
                ContactsActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    public void getNextFriends() {
        if (!this.isSearch) {
            this.isLastVisible = true;
            StatusApi.getFollowings(this.activity, this.page, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        ContactsActivity.this.mUsers.addAll(StatusUser.parseUsers(object
                                .getJSONArray("followings").toString()));
                        ContactsActivity.this.mAdapter.notifyDataSetChanged();
                        ContactsActivity.this.isLastVisible = false;
                        ContactsActivity.this.page = ContactsActivity.this.page + 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void searchFollowings() {
        if (!TextUtils.isEmpty(this.searchString)) {
            showLoading();
            StatusApi.getFollowingsSearch(this.activity, this.searchString, new JsonCallback(this
                    .activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (ContactsActivity.this.mUsers != null) {
                        ContactsActivity.this.mUsers.clear();
                        ContactsActivity.this.mUsers.addAll(StatusUser.parseUsers(object
                                .optString("followings")));
                    }
                    if (ContactsActivity.this.mAdapter != null) {
                        ContactsActivity.this.mAdapter.notifyDataSetChanged();
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    ContactsActivity.this.mPullRefreshListView.onRefreshComplete();
                    ContactsActivity.this.dismissLoading();
                }
            });
        }
    }
}
