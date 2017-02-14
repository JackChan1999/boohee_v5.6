package com.boohee.one.video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.one.video.entity.DailyCourse;
import com.boohee.utils.DateHelper;

import java.util.Date;
import java.util.List;

public class CourseRecyclerAdapter extends Adapter<ViewHolder> {
    private Context           context;
    private List<DailyCourse> courses;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        @InjectView(2131428439)
        RelativeLayout bgLayout;
        @InjectView(2131428440)
        RelativeLayout dailyCourseContainer;
        @InjectView(2131428441)
        RelativeLayout dailyCourseStatusLayout;
        @InjectView(2131428443)
        ImageView      ivCourseStatus;
        @InjectView(2131428444)
        TextView       tvBig;
        @InjectView(2131428442)
        TextView       tvCourseName;
        @InjectView(2131427736)
        TextView       tvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
        }
    }

    public CourseRecyclerAdapter(Context context, List<DailyCourse> list) {
        this.context = context;
        this.courses = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hw,
                parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyCourse dailyCourse = (DailyCourse) this.courses.get(position);
        if (dailyCourse != null) {
            boolean isToday;
            if (TextUtils.equals(DateHelper.format(new Date()), dailyCourse.date)) {
                isToday = true;
                holder.bgLayout.setBackgroundResource(R.drawable.pn);
                holder.dailyCourseContainer.setBackgroundResource(0);
            } else {
                isToday = false;
                holder.bgLayout.setBackgroundResource(0);
                holder.dailyCourseContainer.setBackgroundResource(R.color.j4);
            }
            holder.tvTime.setText(DateHelper.formatString(dailyCourse.date, "MMMdd日"));
            if (TextUtils.equals(dailyCourse.status, "lazy")) {
                holder.dailyCourseStatusLayout.setVisibility(0);
                holder.tvCourseName.setText("偷懒了一天");
                holder.ivCourseStatus.setImageResource(R.drawable.r8);
                holder.tvBig.setVisibility(8);
            } else if (TextUtils.equals(dailyCourse.status, "complete")) {
                holder.dailyCourseStatusLayout.setVisibility(0);
                holder.tvCourseName.setText(dailyCourse.name);
                holder.ivCourseStatus.setImageResource(R.drawable.r7);
                holder.tvBig.setVisibility(8);
            } else if (TextUtils.equals(dailyCourse.status, "rest")) {
                if (isToday) {
                    holder.dailyCourseStatusLayout.setVisibility(8);
                    holder.tvBig.setVisibility(0);
                    holder.tvBig.setText("今日休息");
                    return;
                }
                holder.dailyCourseStatusLayout.setVisibility(0);
                holder.tvCourseName.setText("休息日");
                holder.ivCourseStatus.setImageResource(R.drawable.r_);
                holder.tvBig.setVisibility(8);
            } else if (TextUtils.equals(dailyCourse.status, "locked")) {
                holder.dailyCourseStatusLayout.setVisibility(0);
                holder.tvCourseName.setText(dailyCourse.name);
                holder.ivCourseStatus.setImageResource(R.drawable.r9);
                holder.tvBig.setVisibility(8);
            } else if (TextUtils.equals(dailyCourse.status, "pre")) {
                holder.dailyCourseStatusLayout.setVisibility(8);
                holder.tvBig.setVisibility(0);
                holder.tvBig.setText("今日运动");
            }
        }
    }

    public int getItemCount() {
        return this.courses.size();
    }
}
