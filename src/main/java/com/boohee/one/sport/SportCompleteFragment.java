package com.boohee.one.sport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.sport.model.SportDetail;
import com.boohee.one.ui.fragment.BaseDialogFragment;

public class SportCompleteFragment extends BaseDialogFragment {
    @InjectView(2131428346)
    Button btnComplete;
    @InjectView(2131427899)
    Button btnShare;
    @InjectView(2131428347)
    View   closeLine;
    @InjectView(2131428345)
    View   completeLine;
    @InjectView(2131428343)
    View   diamondLine;
    private boolean     showDiamond;
    private SportDetail sport;
    @InjectView(2131427468)
    TextView tvCalorie;
    @InjectView(2131428145)
    TextView tvDiamond;
    @InjectView(2131427891)
    TextView tvDuration;
    @InjectView(2131428344)
    TextView tvMessage;

    public static SportCompleteFragment newInstance(SportDetail sport, boolean showDiamond) {
        SportCompleteFragment fragment = new SportCompleteFragment();
        fragment.sport = sport;
        fragment.showDiamond = showDiamond;
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.gn, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        if (this.sport != null) {
            this.tvCalorie.setText(String.valueOf(this.sport.calory));
            this.tvDuration.setText(String.valueOf(this.sport.duration));
            this.tvDiamond.setText(String.valueOf(this.sport.envious));
            this.tvMessage.setText("“" + this.sport.share_message + "”");
            if (!this.showDiamond) {
                this.completeLine.setVisibility(8);
                this.closeLine.setVisibility(0);
                this.diamondLine.setVisibility(8);
                this.tvMessage.setVisibility(8);
            }
        }
    }

    @OnClick({2131428346, 2131427899, 2131428348})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share:
                ((SportDetailActivity) getActivity()).share();
                dismiss();
                return;
            case R.id.complete:
                dismiss();
                return;
            case R.id.close:
                dismiss();
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
