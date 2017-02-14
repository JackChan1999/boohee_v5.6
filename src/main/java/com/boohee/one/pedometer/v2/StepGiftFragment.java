package com.boohee.one.pedometer.v2;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pedometer.StepApi;
import com.boohee.one.pedometer.v2.adapter.RewardsAdapter;
import com.boohee.one.pedometer.v2.model.StepReward;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class StepGiftFragment extends BaseDialogFragment {
    @InjectView(2131428360)
    Button btOperate;
    private StepMainActivity mActivity;
    private RewardsAdapter   mAdapter;
    @InjectView(2131427552)
    ListView mListView;
    private List<StepReward> mRewardList = new ArrayList();
    @InjectView(2131427613)
    View         viewContent;
    @InjectView(2131428359)
    LinearLayout viewList;

    @OnClick({2131428360, 2131428358})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_close:
                dismiss();
                return;
            case R.id.bt_operate:
                if (this.mRewardList.size() > 0) {
                    requestRewards();
                    return;
                } else {
                    dismiss();
                    return;
                }
            default:
                return;
        }
    }

    public static StepGiftFragment newInstance() {
        return new StepGiftFragment();
    }

    public void setData(List<StepReward> data) {
        this.mRewardList.clear();
        if (data != null && data.size() > 0) {
            this.mRewardList.addAll(data);
        }
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (StepMainActivity) activity;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gr, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mAdapter = new RewardsAdapter(this.mActivity, this.mRewardList);
        this.mListView.setAdapter(this.mAdapter);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        window.setGravity(80);
        LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = -1;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.de);
        window.setBackgroundDrawable(new ColorDrawable(0));
    }

    public void onResume() {
        super.onResume();
        if (this.mRewardList.size() > 0) {
            this.viewList.setVisibility(0);
            this.viewContent.setVisibility(8);
            this.btOperate.setText(getString(R.string.a7c));
        } else {
            this.viewList.setVisibility(8);
            this.viewContent.setVisibility(0);
            this.btOperate.setText(getString(R.string.a7b));
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void requestRewards() {
        StepApi.requestRewards(getActivity(), new JsonCallback(getActivity()) {
            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                StepGiftFragment.this.mActivity.requestData();
                StepGiftFragment.this.dismiss();
            }
        }, this.mRewardList);
    }
}
