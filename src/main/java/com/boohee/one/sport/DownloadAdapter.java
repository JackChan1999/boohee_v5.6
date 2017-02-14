package com.boohee.one.sport;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.one.sport.model.DownloadRecord;
import com.boohee.one.sport.model.SportDetail;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.widgets.ProgressWheel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DownloadAdapter extends Adapter<DownloadVH> {
    public static final String CANCEL_SELECT_ALL = "cancel_select_all";
    private Context              mContext;
    private List<DownloadRecord> mList;
    private Set<String>          selectVideos  = new HashSet();
    private boolean              showSelect    = false;
    private Map<String, Integer> videoPosition = new HashMap();

    public static class DownloadVH extends ViewHolder {
        @InjectView(2131428548)
        ImageView      arrow;
        @InjectView(2131428546)
        View           continueDownload;
        @InjectView(2131428543)
        ImageView      cover;
        @InjectView(2131428542)
        View           coverArea;
        @InjectView(2131427479)
        View           divider;
        @InjectView(2131427891)
        TextView       duration;
        @InjectView(2131428540)
        RelativeLayout item;
        @InjectView(2131428053)
        TextView       name;
        @InjectView(2131427354)
        TextView       progress;
        @InjectView(2131428541)
        CheckBox       select;
        @InjectView(2131428544)
        View           shadow;
        @InjectView(2131428547)
        TextView       wait;
        @InjectView(2131428545)
        ProgressWheel  wheel;

        public DownloadVH(View itemView) {
            super(itemView);
            ButterKnife.inject((Object) this, itemView);
        }
    }

    public DownloadAdapter(Context context, List<DownloadRecord> list) {
        this.mContext = context;
        this.mList = list;
        createVideoPosition();
    }

    public void setList(List<DownloadRecord> list) {
        this.mList = list;
        this.selectVideos.clear();
        createVideoPosition();
        notifyDataSetChanged();
    }

    private void createVideoPosition() {
        if (this.mList != null) {
            this.videoPosition.clear();
            for (int i = 0; i < this.mList.size(); i++) {
                this.videoPosition.put(((DownloadRecord) this.mList.get(i)).sport.video_url,
                        Integer.valueOf(i));
            }
        }
    }

    public int updateRecord(DownloadRecord record) {
        if (!DownloadHelper.getInstance().hasVideoUrl(record) || !this.videoPosition.containsKey
                (record.sport.video_url)) {
            return -1;
        }
        int position = ((Integer) this.videoPosition.get(record.sport.video_url)).intValue();
        DownloadRecord recordReference = (DownloadRecord) this.mList.get(position);
        recordReference.progress = record.progress;
        recordReference.downloadStatus = record.downloadStatus;
        return position;
    }

    public void updateAllRecord() {
        for (DownloadRecord record : this.mList) {
            if (DownloadHelper.getInstance().hasVideoUrl(record)) {
                DownloadRecord newRecord = DownloadHelper.getInstance().getRecord(record.sport
                        .video_url);
                if (newRecord != null) {
                    record.progress = newRecord.progress;
                    record.downloadStatus = newRecord.downloadStatus;
                }
            }
        }
        notifyDataSetChanged();
    }

    public DownloadVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadVH(LayoutInflater.from(this.mContext).inflate(R.layout.j4, parent,
                false));
    }

    public void onBindViewHolder(final DownloadVH holder, int position) {
        final DownloadRecord record = (DownloadRecord) this.mList.get(position);
        final SportDetail sport = record.sport;
        holder.select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DownloadAdapter.this.selectVideos.add(sport.video_url);
                    if (DownloadAdapter.this.selectVideos.size() == DownloadAdapter.this.mList
                            .size()) {
                        ((DownloadManageActivity) DownloadAdapter.this.mContext).cbSelectAll
                                .setChecked(true);
                        return;
                    }
                    return;
                }
                DownloadAdapter.this.selectVideos.remove(sport.video_url);
                ((DownloadManageActivity) DownloadAdapter.this.mContext).cbSelectAll.setTag
                        (DownloadAdapter.CANCEL_SELECT_ALL);
                ((DownloadManageActivity) DownloadAdapter.this.mContext).cbSelectAll.setChecked
                        (false);
            }
        });
        holder.coverArea.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (record.canDownload()) {
                    DownloadAdapter.this.prepareDownload(holder, record);
                } else if (record.inConnectAndDownload()) {
                    holder.continueDownload.setVisibility(0);
                    holder.progress.setVisibility(8);
                    v.setClickable(false);
                    DownloadService.intentPause(DownloadAdapter.this.mContext, sport.video_url);
                }
            }
        });
        holder.item.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!DownloadAdapter.this.showSelect && record.inComplete()) {
                    SportDetailActivity.startActivity(DownloadAdapter.this.mContext, record);
                }
            }
        });
        holder.name.setText(sport.name);
        SpannableString sizeStr = new SpannableString(String.format(" (%dM)", new
                Object[]{Integer.valueOf(record.videoSize)}));
        sizeStr.setSpan(new RelativeSizeSpan(0.7f), 0, sizeStr.length(), 17);
        sizeStr.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.du)
        ), 0, sizeStr.length(), 17);
        holder.name.append(sizeStr);
        holder.duration.setText(String.format("时长：%d分钟  消耗：%d千卡", new Object[]{Integer.valueOf
                (sport.duration), Integer.valueOf(sport.calory)}));
        if (holder.cover.getTag() == null || !holder.cover.getTag().equals(sport.pic_url)) {
            ImageLoader.getInstance().displayImage(sport.pic_url, holder.cover);
            holder.cover.setTag(sport.pic_url);
        }
        if (this.selectVideos.contains(sport.video_url)) {
            holder.select.setChecked(true);
        } else {
            holder.select.setChecked(false);
        }
        if (this.showSelect) {
            holder.select.setVisibility(0);
        } else {
            holder.select.setVisibility(8);
        }
        if (position == this.mList.size() - 1) {
            holder.divider.setVisibility(8);
        } else {
            holder.divider.setVisibility(0);
        }
        updateDownloadStatus(holder, record);
    }

    private void prepareDownload(final DownloadVH holder, final DownloadRecord record) {
        if (!HttpUtils.isNetworkAvailable(this.mContext)) {
            Helper.showToast((CharSequence) "当前处于无网环境，无法下载");
        } else if (HttpUtils.isWifiConnection(this.mContext)) {
            startDownload(holder, record);
        } else {
            new Builder(this.mContext).setMessage(String.format(this.mContext.getString(R.string
                    .jn), new Object[]{Integer.valueOf(record.videoSize)})).setPositiveButton(
                    (CharSequence) "继续下载", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DownloadAdapter.this.startDownload(holder, record);
                }
            }).setNegativeButton((CharSequence) "取消", null).show();
        }
    }

    private void startDownload(DownloadVH holder, DownloadRecord record) {
        holder.continueDownload.setVisibility(8);
        holder.progress.setVisibility(0);
        holder.coverArea.setClickable(false);
        DownloadService.intentDownload(this.mContext, record);
    }

    public void updateDownloadStatus(DownloadVH holder, DownloadRecord record) {
        if (holder != null && record != null) {
            if (record.progress < 100) {
                holder.arrow.setVisibility(8);
                if (record.inConnectAndDownload()) {
                    holder.wait.setVisibility(8);
                    if (holder.coverArea.isClickable()) {
                        holder.continueDownload.setVisibility(8);
                        holder.progress.setVisibility(0);
                    }
                } else if (record.inWaitDownload()) {
                    holder.continueDownload.setVisibility(8);
                    holder.progress.setVisibility(8);
                    holder.wait.setVisibility(0);
                } else {
                    holder.wait.setVisibility(8);
                    holder.continueDownload.setVisibility(0);
                    holder.progress.setVisibility(8);
                }
                holder.progress.setText(String.valueOf(record.progress));
                holder.shadow.setVisibility(0);
                holder.wheel.setVisibility(0);
                holder.wheel.setProgress((record.progress * 360) / 100);
            } else {
                holder.shadow.setVisibility(8);
                holder.progress.setVisibility(8);
                holder.wait.setVisibility(8);
                holder.continueDownload.setVisibility(8);
                holder.wheel.setVisibility(8);
                if (this.showSelect) {
                    holder.arrow.setVisibility(8);
                } else {
                    holder.arrow.setVisibility(0);
                }
            }
            holder.coverArea.setClickable(true);
        }
    }

    public void setShowSelect(boolean showSelect) {
        this.showSelect = showSelect;
        notifyDataSetChanged();
    }

    public void selectAll() {
        if (this.mList != null) {
            for (DownloadRecord record : this.mList) {
                this.selectVideos.add(record.sport.video_url);
            }
        }
        notifyDataSetChanged();
    }

    public void selectNone() {
        this.selectVideos.clear();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.mList == null ? 0 : this.mList.size();
    }

    public Set<String> getSelectVideos() {
        return this.selectVideos;
    }
}
