package com.boohee.one.sport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boohee.myview.DividerItemDecoration;
import com.boohee.one.R;
import com.boohee.one.sport.model.CourseItemInfo;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

public class SportCourseFragment extends Fragment {
    private static final String KEY_COURSE_DATA = "key_course_data";
    private CourseAdapter mAdapter;
    private List<CourseItemInfo> mDataList = new ArrayList();
    private LinearLayoutManager mLayoutManager;
    private RecyclerView        mRecyclerView;

    private class CourseAdapter extends Adapter<ViewHolder> implements OnClickListener {
        private List<CourseItemInfo> mDataList;

        public CourseAdapter(List<CourseItemInfo> dataList) {
            this.mDataList = dataList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                    .j1, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int index) {
            int i;
            CourseItemInfo course = (CourseItemInfo) this.mDataList.get(index);
            viewHolder.view_content.setTag(course);
            viewHolder.view_content.setOnClickListener(this);
            viewHolder.tv_id.setText(String.format("第 %s 课", new Object[]{Integer.valueOf(course
                    .index)}));
            viewHolder.view_today.setVisibility(course.today ? 0 : 8);
            viewHolder.tv_name.setText(course.name);
            TextView textView = viewHolder.tv_download;
            if (course.download) {
                i = 0;
            } else {
                i = 8;
            }
            textView.setVisibility(i);
            if (TextUtils.isEmpty(course.date)) {
                viewHolder.tv_date.setVisibility(8);
                viewHolder.view_checked.setVisibility(8);
                return;
            }
            viewHolder.tv_date.setVisibility(0);
            String date = DateFormatUtils.string2String(course.date, "MM月dd日");
            viewHolder.tv_date.setText(String.format("完成于%s", new Object[]{date}));
            viewHolder.view_checked.setVisibility(0);
        }

        public int getItemCount() {
            return this.mDataList.size();
        }

        public void onClick(View v) {
            CourseItemInfo course = (CourseItemInfo) v.getTag();
            if (!course.is_rest || TextUtils.isEmpty(course.link_to)) {
                SportDetailActivity.startActivity(SportCourseFragment.this.getActivity(), course
                        .id, course.video_url);
            } else {
                BrowserActivity.comeOnBaby(v.getContext(), course.name, course.link_to);
            }
        }
    }

    private static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public TextView tv_date;
        public TextView tv_download;
        public TextView tv_id;
        public TextView tv_name;
        public View     view_checked;
        public View     view_content;
        public View     view_today;

        public ViewHolder(View view) {
            super(view);
            this.tv_id = (TextView) view.findViewById(R.id.tv_id);
            this.tv_download = (TextView) view.findViewById(R.id.tv_download);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_date = (TextView) view.findViewById(R.id.tv_date);
            this.view_today = view.findViewById(R.id.view_today);
            this.view_content = view.findViewById(R.id.view_content);
            this.view_checked = view.findViewById(R.id.view_checked);
        }
    }

    public static SportCourseFragment newInstance(ArrayList<CourseItemInfo> data) {
        SportCourseFragment instance = new SportCourseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_COURSE_DATA, data);
        instance.setArguments(bundle);
        return instance;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<CourseItemInfo> dataList = bundle.getParcelableArrayList(KEY_COURSE_DATA);
            if (dataList != null && dataList.size() > 0) {
                this.mDataList.addAll(dataList);
            }
            this.mAdapter = new CourseAdapter(this.mDataList);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        this.mRecyclerView = new RecyclerView(getActivity());
        return this.mRecyclerView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        this.mAdapter.notifyDataSetChanged();
    }

    public void notifyDataChange(List<CourseItemInfo> dataList) {
        if (dataList != null && dataList.size() > 0 && this.mAdapter != null) {
            this.mDataList.clear();
            this.mDataList.addAll(dataList);
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
