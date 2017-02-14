package com.boohee.nice.fragment;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.NiceApi;
import com.boohee.database.OnePreference;
import com.boohee.myview.highlight.HighLight;
import com.boohee.myview.highlight.HighLight.MarginInfo;
import com.boohee.myview.highlight.HighLight.OnHighLightClickListener;
import com.boohee.myview.highlight.HighLight.OnPosCallback;
import com.boohee.nice.NiceAdviserActivity;
import com.boohee.nice.NiceSellActivity;
import com.boohee.nice.model.NiceDetail;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;

import org.json.JSONObject;

public class NiceMainFragment extends BaseFragment {
    private HighLight  highLightOne;
    private HighLight  highLightThree;
    private HighLight  highLightTwo;
    private NiceDetail niceDetail;
    @InjectView(2131428294)
    ProgressBar    pbNiceProgress;
    @InjectView(2131428288)
    RelativeLayout rlNiceTop;
    @InjectView(2131428299)
    TextView       tvAdviserDesc;
    @InjectView(2131428291)
    TextView       tvNiceBegin;
    @InjectView(2131428289)
    TextView       tvNiceDay;
    @InjectView(2131428293)
    TextView       tvNiceEnd;
    @InjectView(2131427820)
    TextView       tvNiceTitle;
    @InjectView(2131428292)
    TextView       tvRenew;
    @InjectView(2131428296)
    TextView       tvReportTitle;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.g7, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNiceDetail();
    }

    private void getMessageStatus() {
        NiceApi.getMessageStatus(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                int status = object.optInt("status");
                int unread_count = object.optInt("unread_count");
                if (status == 2 && unread_count > 0) {
                    NiceMainFragment.this.tvAdviserDesc.setTextColor(NiceMainFragment.this
                            .getResources().getColor(R.color.d8));
                    NiceMainFragment.this.tvAdviserDesc.setText("新消息");
                }
            }
        });
    }

    private void showGuideOne() {
        if (this.highLightOne == null) {
            this.highLightOne = new HighLight(getActivity()).addHighLight((int) R.id
                    .rl_nice_report, (int) R.layout.pz, new OnPosCallback() {
                public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                        marginInfo) {
                    marginInfo.rightMargin = (float) ViewUtils.dip2px(NiceMainFragment.this
                            .getActivity(), 16.0f);
                    marginInfo.bottomMargin = bottomMargin - ((float) ViewUtils.dip2px
                            (NiceMainFragment.this.getActivity(), 140.0f));
                }
            });
            this.highLightOne.show();
            this.highLightOne.setOnHighLightClickListener(new OnHighLightClickListener() {
                public void onClick() {
                    OnePreference.setNiceServiceGuide(true);
                }
            });
        }
    }

    private void showGuideTwo() {
        if (this.highLightTwo == null) {
            this.highLightTwo = new HighLight(getActivity()).addHighLight((int) R.id
                    .rl_nice_plan, (int) R.layout.q1, new OnPosCallback() {
                public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                        marginInfo) {
                    marginInfo.rightMargin = (float) ViewUtils.dip2px(NiceMainFragment.this
                            .getActivity(), 16.0f);
                    marginInfo.bottomMargin = bottomMargin - ((float) ViewUtils.dip2px
                            (NiceMainFragment.this.getActivity(), 140.0f));
                }
            });
            this.highLightTwo.show();
            this.highLightTwo.setOnHighLightClickListener(new OnHighLightClickListener() {
                public void onClick() {
                    OnePreference.setNiceServiceGuideSecond(true);
                }
            });
        }
    }

    private void showGuideThree() {
        if (this.highLightThree == null) {
            this.highLightThree = new HighLight(getActivity()).addHighLight((int) R.id
                    .ll_nice_adviser, (int) R.layout.q0, new OnPosCallback() {
                public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                        marginInfo) {
                    marginInfo.rightMargin = (float) ViewUtils.dip2px(NiceMainFragment.this
                            .getActivity(), 16.0f);
                    marginInfo.bottomMargin = bottomMargin - ((float) ViewUtils.dip2px
                            (NiceMainFragment.this.getActivity(), 140.0f));
                }
            });
            this.highLightThree.show();
            this.highLightThree.setOnHighLightClickListener(new OnHighLightClickListener() {
                public void onClick() {
                    OnePreference.setNiceServiceGuideThird(true);
                }
            });
        }
    }

    private void getNiceDetail() {
        NiceApi.getNiceDetail(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                NiceMainFragment.this.niceDetail = (NiceDetail) FastJsonUtils.fromJson(object,
                        NiceDetail.class);
                if (NiceMainFragment.this.niceDetail != null && NiceMainFragment.this.niceDetail
                        .period != null) {
                    NiceMainFragment.this.initView();
                    NiceMainFragment.this.initGuide();
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        refreshStatus();
    }

    public void refreshStatus() {
        getNiceDetail();
    }

    private void initGuide() {
        if (!OnePreference.isNiceServiceGuide()) {
            showGuideOne();
        } else if (this.niceDetail.survey_status == 1 && !OnePreference.isNiceServiceGuideSecond
                ()) {
            showGuideTwo();
        } else if (this.niceDetail.report_status == 1 && !OnePreference.isNiceServiceGuideThird()) {
            showGuideThree();
        }
    }

    private void initView() {
        this.tvNiceTitle.setText(this.niceDetail.period.title);
        this.tvNiceDay.setText(this.niceDetail.period.period_index_desc);
        if (TextUtils.isEmpty(this.niceDetail.period.start_at) || TextUtils.isEmpty(this
                .niceDetail.period.end_on)) {
            this.tvNiceBegin.setText("");
            this.tvNiceEnd.setText("");
        } else {
            this.tvNiceBegin.setText(this.niceDetail.period.start_at);
            this.tvNiceEnd.setText(this.niceDetail.period.end_on);
            this.pbNiceProgress.setMax(DateHelper.between(DateHelper.parseString(this.niceDetail
                    .period.end_on), DateHelper.parseString(this.niceDetail.period.start_at)));
            this.pbNiceProgress.setProgress(this.niceDetail.period.period_index);
        }
        if (this.niceDetail.report_status != 0) {
            this.tvReportTitle.setText("评测报告");
        } else if (this.niceDetail.survey_status == 0) {
            this.tvReportTitle.setText("填写评测");
        } else {
            this.tvReportTitle.setText("报告分析中");
        }
        this.tvAdviserDesc.setTextColor(getResources().getColor(R.color.gz));
        if (this.niceDetail.advisor.status == 0) {
            this.tvAdviserDesc.setText("正在为您安排顾问，请耐心等待");
        } else {
            this.tvAdviserDesc.setText("顾问 " + this.niceDetail.advisor.name);
        }
        if (this.niceDetail.period.payment_state == 1) {
            this.tvRenew.setVisibility(0);
            this.tvRenew.setText("我要续费");
        } else if (this.niceDetail.period.payment_state == 2) {
            this.tvRenew.setVisibility(0);
            this.tvRenew.setText("重新购买");
            this.tvAdviserDesc.setText("查看历史聊天记录");
        } else if (this.niceDetail.period.payment_state == 0) {
            this.tvRenew.setVisibility(8);
        }
        getMessageStatus();
    }

    public static NiceMainFragment newInstance() {
        return new NiceMainFragment();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131428295, 2131428297, 2131428298, 2131428300, 2131428288, 2131428301, 2131428302})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_nice_top:
                if (this.niceDetail != null && this.niceDetail.period != null) {
                    if (this.niceDetail.period.payment_state == 1) {
                        NiceSellActivity.startActivity(getActivity(), NiceSellActivity
                                .RENEW_NICE_SERVICE);
                        return;
                    } else if (this.niceDetail.period.payment_state == 2) {
                        NiceSellActivity.startActivity(getActivity(), NiceSellActivity
                                .NICE_SERVICE);
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case R.id.rl_nice_report:
                showReport();
                return;
            case R.id.rl_nice_plan:
                showPlan();
                return;
            case R.id.ll_nice_adviser:
                showAdviser();
                return;
            case R.id.ll_nice_diet:
                if (this.niceDetail.nice_urls != null) {
                    BrowserActivity.comeOnBaby(getActivity(), "饮食指南", this.niceDetail.nice_urls
                            .diet_page);
                    return;
                }
                return;
            case R.id.ll_nice_sport:
                if (this.niceDetail.nice_urls != null) {
                    BrowserActivity.comeOnBaby(getActivity(), "运动资料库", this.niceDetail.nice_urls
                            .activity_page);
                    return;
                }
                return;
            case R.id.ll_nice_knowledge:
                if (this.niceDetail.nice_urls != null) {
                    BrowserActivity.comeOnBaby(getActivity(), "减肥知识库", this.niceDetail.nice_urls
                            .knowledge);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void showAdviser() {
        if (this.niceDetail != null) {
            if (this.niceDetail.advisor.status == 0) {
                Helper.showToast((CharSequence) "请耐心等待顾问分配");
            } else if (this.niceDetail.advisor.status == 1) {
                NiceAdviserActivity.startActivity(getActivity(), true);
            } else if (this.niceDetail.advisor.status == 2) {
                Helper.showToast((CharSequence) "服务已过期");
                NiceAdviserActivity.startActivity(getActivity(), false);
            }
        }
    }

    private void showPlan() {
        if (this.niceDetail != null) {
            if (this.niceDetail.survey_status == 0) {
                Helper.showToast((CharSequence) "请先评测");
            } else if (this.niceDetail.report_status == 0) {
                Helper.showToast((CharSequence) "减重方案分析中");
            } else if (this.niceDetail.report_status == 1) {
                BrowserActivity.comeOnBaby(getActivity(), "填写评测", this.niceDetail.nice_urls
                        .solution_report);
            }
        }
    }

    private void showReport() {
        if (this.niceDetail != null && this.niceDetail.nice_urls != null) {
            if (this.niceDetail.report_status == 1) {
                BrowserActivity.comeOnBaby(getActivity(), "评测报告", this.niceDetail.nice_urls
                        .survey_report);
            } else if (this.niceDetail.survey_status == 0) {
                BrowserActivity.comeOnBaby(getActivity(), "填写评测", this.niceDetail.nice_urls.survey);
            } else if (this.niceDetail.survey_status == 1) {
                Helper.showToast((CharSequence) "评测报告分析中");
            }
        }
    }
}
