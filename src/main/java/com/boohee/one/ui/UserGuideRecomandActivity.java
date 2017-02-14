package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.boohee.api.StatusApi;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserGuideRecomandActivity extends BaseActivity {
    private MListViewAdapter  adapter;
    private ArrayList<String> chosenList;
    private LayoutInflater    li;
    private ListView          listView;
    private Context           mContext;
    private List<StatusUser> mFriendsUsers = new ArrayList();

    private class MListViewAdapter extends BaseAdapter {
        private ImageLoader imageloader = ImageLoader.getInstance();

        public int getCount() {
            return UserGuideRecomandActivity.this.mFriendsUsers.size();
        }

        public Object getItem(int position) {
            return UserGuideRecomandActivity.this.mFriendsUsers.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            boolean z;
            if (convertView == null) {
                convertView = UserGuideRecomandActivity.this.li.inflate(R.layout.k6, null);
                holder = new ViewHolder();
                holder.photoImage = (ImageView) convertView.findViewById(R.id
                        .userguide_recommand_userPhoto);
                holder.userName = (TextView) convertView.findViewById(R.id
                        .userguide_recommand_userName);
                holder.userBMI = (TextView) convertView.findViewById(R.id
                        .userguide_recommand_userBMI);
                holder.radioButton = (RadioButton) convertView.findViewById(R.id
                        .userguide_recommand_radioBtn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            StatusUser user = (StatusUser) UserGuideRecomandActivity.this.mFriendsUsers.get
                    (position);
            this.imageloader.displayImage(user.avatar_url, holder.photoImage, ImageLoaderOptions
                    .avatar());
            holder.userName.setText(user.nickname);
            holder.userBMI.setText("BMI " + String.format("%.1f", new Object[]{Float.valueOf(user
                    .bmi)}));
            RadioButton radioButton = holder.radioButton;
            if (user.following) {
                z = false;
            } else {
                z = true;
            }
            radioButton.setChecked(z);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView   photoImage;
        RadioButton radioButton;
        TextView    userBMI;
        TextView    userName;

        ViewHolder() {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt);
        setTitle("为你推荐");
        findView();
        addListener();
        init();
    }

    private void findView() {
        this.listView = (ListView) findViewById(R.id.userguide_recommand_listView);
    }

    private void addListener() {
        findViewById(R.id.userguide_recommand_addFocus).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (UserGuideRecomandActivity.this.chosenList != null) {
                    ArrayList<String> tempList = new ArrayList();
                    for (int i = 0; i < UserGuideRecomandActivity.this.chosenList.size(); i++) {
                        if (UserGuideRecomandActivity.this.chosenList.get(i) != null) {
                            tempList.add(UserGuideRecomandActivity.this.chosenList.get(i));
                        }
                    }
                    UserGuideRecomandActivity.this.chosenList = tempList;
                    if (UserGuideRecomandActivity.this.chosenList.size() < 1) {
                        Helper.showToast(UserGuideRecomandActivity.this.mContext, (CharSequence)
                                "请选择一位用户！");
                        return;
                    }
                    UserGuideRecomandActivity.this.showLoading();
                    StatusApi.createFriendshipList(UserGuideRecomandActivity.this.activity,
                            UserGuideRecomandActivity.this.chosenList, new JsonCallback
                                    (UserGuideRecomandActivity.this.activity) {
                        public void ok(JSONObject object) {
                            super.ok(object);
                            Intent intent = new Intent();
                            intent.setClass(UserGuideRecomandActivity.this.mContext, MainActivity
                                    .class);
                            UserGuideRecomandActivity.this.startActivity(intent);
                            UserGuideRecomandActivity.this.finish();
                        }

                        public void onFinish() {
                            super.onFinish();
                            UserGuideRecomandActivity.this.dismissLoading();
                        }
                    });
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                boolean z = true;
                StatusUser user = (StatusUser) UserGuideRecomandActivity.this.mFriendsUsers.get
                        (position);
                ViewHolder holder = (ViewHolder) view.getTag();
                if (user.following) {
                    UserGuideRecomandActivity.this.chosenList.add(position, ((StatusUser)
                            UserGuideRecomandActivity.this.mFriendsUsers.get(position)).id + "");
                    user.following = false;
                    RadioButton radioButton = holder.radioButton;
                    if (user.following) {
                        z = false;
                    }
                    radioButton.setChecked(z);
                    return;
                }
                UserGuideRecomandActivity.this.chosenList.set(position, null);
                user.following = true;
                holder.radioButton.setChecked(!user.following);
            }
        });
    }

    private void init() {
        this.mContext = this;
        this.li = LayoutInflater.from(this.mContext);
        this.adapter = new MListViewAdapter();
        this.listView.setAdapter(this.adapter);
        loadMoreData();
    }

    private void loadMoreData() {
        JSONException e1;
        JSONArray jSONArray;
        try {
            JSONArray jsonArray = new JSONArray(getIntent().getStringExtra(SuccessStoryActivity
                    .TAGS));
            if (jsonArray != null) {
                try {
                    if (jsonArray.length() > 0) {
                        showLoading();
                        StatusApi.getUserRecommended(this.activity, new JsonCallback(this
                                .activity) {
                            public void ok(JSONObject object) {
                                super.ok(object);
                                try {
                                    JSONArray usersArray = object.getJSONArray("users");
                                    if (usersArray != null && usersArray.length() > 0) {
                                        UserGuideRecomandActivity.this.mFriendsUsers = StatusUser
                                                .parseUsers(usersArray.toString());
                                        UserGuideRecomandActivity.this.chosenList = new ArrayList();
                                        for (StatusUser user : UserGuideRecomandActivity.this
                                                .mFriendsUsers) {
                                            UserGuideRecomandActivity.this.chosenList.add(user.id
                                                    + "");
                                        }
                                        UserGuideRecomandActivity.this.adapter
                                                .notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            public void onFinish() {
                                super.onFinish();
                                UserGuideRecomandActivity.this.dismissLoading();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e1 = e;
                    jSONArray = jsonArray;
                    e1.printStackTrace();
                    showLoading();
                    StatusApi.getUserRecommended(this.activity, /* anonymous class already
                    generated */);
                }
            }
            Helper.showToast(this.mContext, (CharSequence) "获取数据失败请重试！");
            jSONArray = jsonArray;
        } catch (JSONException e2) {
            e1 = e2;
            e1.printStackTrace();
            showLoading();
            StatusApi.getUserRecommended(this.activity, /* anonymous class already generated */);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "跳过").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            postAction();
            return true;
        } else if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        } else {
            startActivity(new Intent(this.mContext, UserGuideChooseInterestActivity.class));
            finish();
            return true;
        }
    }

    private void postAction() {
        startActivity(new Intent(this, MainActivity.class).setFlags(67108864));
        finish();
    }
}
