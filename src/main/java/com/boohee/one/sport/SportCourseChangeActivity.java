package com.boohee.one.sport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.sport.model.CourseDesc;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.RcvAdapterWrapper;
import kale.adapter.item.AdapterItem;

import org.json.JSONObject;

public class SportCourseChangeActivity extends GestureActivity implements OnClickListener {
    private CommonRcvAdapter<CourseDesc> mAdapter  = new CommonRcvAdapter<CourseDesc>(this
            .mDataList) {
        @NonNull
        public AdapterItem createItem(Object o) {
            return new AdapterItem<CourseDesc>() {
                public Button bt_join;
                public ImageView iv_bg;
                public TextView tv_desc;
                public TextView tv_name;
                public View view_content;

                public int getLayoutResId() {
                    return R.layout.j2;
                }

                public void bindViews(View view) {
                    this.view_content = view.findViewById(R.id.view_content);
                    this.iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
                    this.tv_name = (TextView) view.findViewById(R.id.tv_name);
                    this.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                    this.bt_join = (Button) view.findViewById(R.id.bt_join);
                }

                public void setViews() {
                }

                public void handleData(CourseDesc desc, int i) {
                    this.tv_name.setText(desc.name);
                    this.tv_desc.setText(desc.desc);
                    SportCourseChangeActivity.this.imageLoader.displayImage(desc.pic_url, this
                            .iv_bg, ImageLoaderOptions.randomColor());
                    this.view_content.setTag(Integer.valueOf(desc.id));
                    this.bt_join.setTag(Integer.valueOf(desc.id));
                    this.bt_join.setOnClickListener(SportCourseChangeActivity.this);
                    if (desc.isActive) {
                        this.bt_join.setText(R.string.abv);
                        this.bt_join.setBackgroundResource(R.drawable.dk);
                        return;
                    }
                    this.bt_join.setText(R.string.abw);
                    this.bt_join.setBackgroundResource(R.drawable.di);
                }
            };
        }
    };
    private List<CourseDesc>             mDataList = new ArrayList();
    RecyclerView mRecyclerView;
    private RcvAdapterWrapper mWrapper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d4);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        initView();
        requestData();
    }

    private void initView() {
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mWrapper = new RcvAdapterWrapper(this.mAdapter, this.mRecyclerView.getLayoutManager());
        this.mWrapper.setHeaderView(LayoutInflater.from(this).inflate(R.layout.mq, null));
        this.mWrapper.setFooterView(LayoutInflater.from(this).inflate(R.layout.mp, null));
        this.mRecyclerView.setAdapter(this.mWrapper);
    }

    private void requestData() {
        showLoading();
        SportV3Api.requestCourseList(new JsonCallback(this) {
            public void ok(JSONObject object) {
                List<CourseDesc> courseDescList = FastJsonUtils.parseList(object.optString
                        ("sports_courses"), CourseDesc.class);
                if (courseDescList != null && courseDescList.size() != 0) {
                    SportCourseChangeActivity.this.mDataList.addAll(courseDescList);
                    List<Integer> userCourse = FastJsonUtils.parseList(object.optString
                            ("user_courses"), Integer.class);
                    for (CourseDesc course : SportCourseChangeActivity.this.mDataList) {
                        for (Integer id : userCourse) {
                            if (course.id == id.intValue()) {
                                course.isActive = true;
                                break;
                            }
                        }
                    }
                    SportCourseChangeActivity.this.mAdapter.notifyDataSetChanged();
                }
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                SportCourseChangeActivity.this.dismissLoading();
            }
        }, this);
    }

    private void chooseCourse(int courseId) {
        showLoading();
        SportV3Api.chooseCourse(courseId, new JsonCallback(this) {
            public void ok(JSONObject object) {
                SportCourseActivity.comeOnBaby(SportCourseChangeActivity.this);
                SportCourseChangeActivity.this.finish();
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                SportCourseChangeActivity.this.dismissLoading();
            }
        }, this);
    }

    public void onClick(View v) {
        chooseCourse(((Integer) v.getTag()).intValue());
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            Intent intent = new Intent();
            intent.setClass(context, SportCourseChangeActivity.class);
            context.startActivity(intent);
        }
    }
}
