package com.boohee.one.video.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.one.video.download.VideoDownloadHelper;
import com.boohee.one.video.entity.Mention;

public class MentionPreviewFragment extends BaseFragment {
    @InjectView(2131429366)
    LinearLayout breathLayout;
    @InjectView(2131429364)
    LinearLayout commonErrorLayout;
    @InjectView(2131429358)
    LinearLayout essentials1Layout;
    @InjectView(2131429360)
    LinearLayout essentials2Layout;
    @InjectView(2131429362)
    LinearLayout essentials3Layout;
    private Mention mention;
    @InjectView(2131429367)
    TextView tvBreath;
    @InjectView(2131429365)
    TextView tvCommonError;
    @InjectView(2131429359)
    TextView tvEssentials1;
    @InjectView(2131429361)
    TextView tvEssentials2;
    @InjectView(2131429363)
    TextView tvEssentials3;
    @InjectView(2131427514)
    TextView tvName;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.ps, null);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (this.mention != null) {
            this.tvName.setText(this.mention.name);
            VideoDownloadHelper helper = VideoDownloadHelper.getInstance();
            helper.isNeedShowInfoText(this.essentials1Layout, this.tvEssentials1, this.mention
                    .info.summary1);
            helper.isNeedShowInfoText(this.essentials2Layout, this.tvEssentials2, this.mention
                    .info.summary2);
            helper.isNeedShowInfoText(this.essentials3Layout, this.tvEssentials3, this.mention
                    .info.summary3);
            helper.isNeedShowInfoText(this.commonErrorLayout, this.tvCommonError, this.mention
                    .info.common_errors);
            helper.isNeedShowInfoText(this.breathLayout, this.tvBreath, this.mention.info.breath);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public static MentionPreviewFragment newInstance(Mention mention) {
        MentionPreviewFragment fragment = new MentionPreviewFragment();
        fragment.mention = mention;
        return fragment;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }
}
