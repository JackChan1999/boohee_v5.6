package com.boohee.one.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.OnePreference;
import com.boohee.model.RecordSport;
import com.boohee.model.Sport;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.AddSportFragment;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class GoSportAdapter extends Adapter<SportVH> {
    private int              calory;
    private FragmentActivity mContext;
    private String           record_on;
    private List<Sport>      sportList;
    private float weight = 55.0f;

    public static class SportVH extends ViewHolder {
        @InjectView(2131427891)
        TextView        duration;
        @InjectView(2131427370)
        CircleImageView icon;
        @InjectView(2131428053)
        TextView        name;
        @InjectView(2131428453)
        Button          record;

        public SportVH(View itemView) {
            super(itemView);
            ButterKnife.inject((Object) this, itemView);
        }
    }

    public GoSportAdapter(FragmentActivity context, List<Sport> sportList, int calory, String
            record_on) {
        this.mContext = context;
        this.sportList = sportList;
        this.calory = calory;
        this.record_on = record_on;
        if (OnePreference.getLatestWeight() > 0.0f) {
            this.weight = OnePreference.getLatestWeight();
        }
    }

    public void updateCalory(int calory) {
        this.calory = calory;
        notifyDataSetChanged();
    }

    public SportVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SportVH(LayoutInflater.from(this.mContext).inflate(R.layout.i3, parent, false));
    }

    public void onBindViewHolder(SportVH holder, int position) {
        final Sport sport = (Sport) this.sportList.get(position);
        if (!TextUtils.isEmpty(sport.big_photo_url)) {
            ImageLoader.getInstance().displayImage(sport.big_photo_url, holder.icon,
                    ImageLoaderOptions.global((int) R.drawable.aa5));
        }
        if (this.calory > 0) {
            holder.duration.setText(((int) ((((float) this.calory) / ((float) sport.calcCalory
                    (this.weight))) * 60.0f)) + "");
        } else {
            holder.duration.setText("0");
        }
        holder.name.setText(sport.name);
        holder.record.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RecordSport recordSport = new RecordSport();
                recordSport.mets = Float.parseFloat(sport.mets);
                recordSport.activity_name = sport.name;
                recordSport.activity_id = sport.id;
                recordSport.unit_name = Sport.UNIT_NAME;
                recordSport.thumb_img_url = sport.big_photo_url;
                recordSport.record_on = GoSportAdapter.this.record_on;
                AddSportFragment.newInstance(0, recordSport).show(GoSportAdapter.this.mContext
                        .getSupportFragmentManager(), "addSportFragment");
            }
        });
    }

    public int getItemCount() {
        return this.sportList == null ? 0 : this.sportList.size();
    }
}
