package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.boohee.one.R;

import org.json.JSONArray;

public class UserGuideChooseInterestActivity extends BaseActivity {
    private MGridAdapter adapter;
    private int currentCheckedNum = 1;
    private GridView       gridView;
    private LayoutInflater li;
    private Context        mContext;
    private String[] tags   = new String[this.titles.length];
    final   String[] titles = new String[]{"减肥", "健身", "美食", "学生", "办公族", "辣妈"};

    private class MGridAdapter extends BaseAdapter {
        final int[] drawables;

        private MGridAdapter() {
            this.drawables = new int[]{R.drawable.ln, R.drawable.ll, R.drawable.lj, R.drawable
                    .lm, R.drawable.lo, R.drawable.lk};
        }

        public int getCount() {
            return this.drawables.length;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = UserGuideChooseInterestActivity.this.li.inflate(R.layout.h3, null);
            }
            TextView text = (TextView) convertView.findViewById(R.id
                    .grid_item_choose_your_interest_titleText);
            text.setText(UserGuideChooseInterestActivity.this.titles[position]);
            if (position == 0) {
                ((RadioButton) convertView.findViewById(R.id
                        .grid_item_choose_your_interest_radioBtn)).setChecked(true);
                UserGuideChooseInterestActivity.this.tags[position] =
                        UserGuideChooseInterestActivity.this.titles[position];
            }
            Drawable drawable = UserGuideChooseInterestActivity.this.getResources().getDrawable
                    (this.drawables[position]);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            text.setCompoundDrawables(null, drawable, null, null);
            return convertView;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar);
        setTitle("选择你的兴趣");
        findView();
        addListener();
        init();
    }

    private void findView() {
        this.gridView = (GridView) findViewById(R.id.choose_interest_gridView);
    }

    private void addListener() {
        findViewById(R.id.choose_interest_nextBtn).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserGuideChooseInterestActivity.this.mContext,
                        UserGuideRecomandActivity.class);
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < UserGuideChooseInterestActivity.this.tags.length; i++) {
                    if (!TextUtils.isEmpty(UserGuideChooseInterestActivity.this.tags[i])) {
                        jsonArray.put(UserGuideChooseInterestActivity.this.tags[i]);
                    }
                }
                intent.putExtra(SuccessStoryActivity.TAGS, jsonArray.toString());
                UserGuideChooseInterestActivity.this.startActivity(intent);
                UserGuideChooseInterestActivity.this.finish();
            }
        });
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RadioButton btn = (RadioButton) view.findViewById(R.id
                        .grid_item_choose_your_interest_radioBtn);
                if (UserGuideChooseInterestActivity.this.currentCheckedNum <= 1 &&
                        UserGuideChooseInterestActivity.this.titles[position].equals
                                (UserGuideChooseInterestActivity.this.tags[position])) {
                    return;
                }
                if (btn.isChecked()) {
                    btn.setChecked(false);
                    UserGuideChooseInterestActivity.this.tags[position] = "";
                    UserGuideChooseInterestActivity.this.currentCheckedNum =
                            UserGuideChooseInterestActivity.this.currentCheckedNum - 1;
                    return;
                }
                btn.setChecked(true);
                UserGuideChooseInterestActivity.this.tags[position] =
                        UserGuideChooseInterestActivity.this.titles[position];
                UserGuideChooseInterestActivity.this.currentCheckedNum =
                        UserGuideChooseInterestActivity.this.currentCheckedNum + 1;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this.mContext, MainActivity.class).setFlags(67108864));
        finish();
        return true;
    }

    private void init() {
        this.mContext = this;
        this.li = LayoutInflater.from(this);
        this.adapter = new MGridAdapter();
        this.gridView.setAdapter(this.adapter);
    }
}
