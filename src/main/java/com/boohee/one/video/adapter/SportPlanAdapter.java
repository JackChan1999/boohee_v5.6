package com.boohee.one.video.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.boohee.api.ApiUrls;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.Plan;
import com.boohee.one.video.ui.SportPlanActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ResolutionUtils;
import com.boohee.widgets.LightAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;

import org.json.JSONObject;

public class SportPlanAdapter extends BaseAdapter {
    private Context         context;
    private ArrayList<Plan> plans;

    static class ViewHolder {
        ImageView bgImage;
        ImageView completedImg;
        TextView  tvProgress;

        ViewHolder() {
        }
    }

    public SportPlanAdapter(Context context, ArrayList<Plan> plans) {
        this.context = context;
        this.plans = plans;
    }

    public int getCount() {
        return this.plans == null ? 0 : this.plans.size();
    }

    public Object getItem(int position) {
        return this.plans.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.j5, null);
            holder = new ViewHolder();
            holder.bgImage = (ImageView) convertView.findViewById(R.id.bg_img);
            holder.tvProgress = (TextView) convertView.findViewById(R.id.tv_lesson_progress);
            holder.completedImg = (ImageView) convertView.findViewById(R.id.iv_completed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (convertView.getLayoutParams() == null) {
            convertView.setLayoutParams(new LayoutParams(this.context.getResources()
                    .getDisplayMetrics().widthPixels, ResolutionUtils.getHeight(this.context,
                    750, 550)));
        } else {
            convertView.getLayoutParams().height = ResolutionUtils.getHeight(this.context, 750,
                    550);
        }
        final Plan plan = (Plan) this.plans.get(position);
        SpannableString styledText = new SpannableString(String.format("第%d/%d天", new
                Object[]{Integer.valueOf(plan.progress), Integer.valueOf(plan.total_progress)}));
        styledText.setSpan(new TextAppearanceSpan(this.context, R.style.fo), 1, 2, 33);
        holder.tvProgress.setText(styledText, BufferType.SPANNABLE);
        ImageLoader.getInstance().displayImage(plan.pic_url, holder.bgImage, ImageLoaderOptions
                .randomColor());
        if (plan.is_complete) {
            holder.completedImg.setVisibility(0);
        } else {
            holder.completedImg.setVisibility(8);
        }
        if (plan.is_complete && plan.progress == plan.total_progress) {
            holder.bgImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (SportPlanAdapter.this.plans.size() == 1) {
                        SportPlanAdapter.this.showTestDialog(plan);
                    } else {
                        SportPlanAdapter.this.showDialog(plan);
                    }
                }
            });
        }
        return convertView;
    }

    private void showTestDialog(final Plan plan) {
        LightAlertDialog.create(this.context, (int) R.string.a5r).setNegativeButton((int) R
                .string.a60, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SportPlanAdapter.this.setAgainLesson(plan.id);
            }
        }).setPositiveButton((int) R.string.a5y, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SportPlanAdapter.this.setCompletedLesson(plan.id);
                BrowserActivity.comeOnBaby(SportPlanAdapter.this.context, "评测", BooheeClient
                        .build(BooheeClient.BINGO).getDefaultURL(ApiUrls.SPORT_QUESTIONS_URL));
                if (SportPlanAdapter.this.context instanceof Activity) {
                    ((Activity) SportPlanAdapter.this.context).finish();
                }
            }
        }).show();
    }

    private void showDialog(final Plan plan) {
        LightAlertDialog.create(this.context, (int) R.string.a64).setNegativeButton((int) R
                .string.a60, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SportPlanAdapter.this.setAgainLesson(plan.id);
            }
        }).setPositiveButton((int) R.string.a61, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SportPlanAdapter.this.setCompletedLesson(plan.id);
            }
        }).show();
    }

    private void setAgainLesson(int lesson_id) {
        SportApi.postLessonAgain(this.context, lesson_id, new JsonCallback(this.context) {
            public void ok(JSONObject object) {
                super.ok(object);
                EventBus.getDefault().post(SportPlanActivity.REFRESH_SPORT_PLAN);
            }
        });
    }

    private void setCompletedLesson(int lesson_id) {
        SportApi.postLessonCompleted(this.context, lesson_id, new JsonCallback(this.context) {
            public void ok(JSONObject object) {
                super.ok(object);
                EventBus.getDefault().post(SportPlanActivity.REFRESH_SPORT_PLAN);
            }
        });
    }
}
