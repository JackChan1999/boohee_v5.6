package com.boohee.nice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.nice.NiceConfirmOrderActivity;
import com.boohee.nice.NiceSellActivity;
import com.boohee.nice.model.NiceServices.ServicesBean;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.ViewUtils;

public class NiceGoodsFragment extends BaseFragment {
    private int bgColor = 0;
    @InjectView(2131428285)
    Button btBuy;
    private int color = 0;
    @InjectView(2131428278)
    LinearLayout llBuyDesc;
    @InjectView(2131427824)
    LinearLayout llCover;
    private ServicesBean servicesBean;
    @InjectView(2131428282)
    TextView tvDescFour;
    @InjectView(2131428279)
    TextView tvDescOne;
    @InjectView(2131428281)
    TextView tvDescThree;
    @InjectView(2131428280)
    TextView tvDescTwo;
    @InjectView(2131428284)
    TextView tvNicePrice;
    @InjectView(2131427820)
    TextView tvNiceTitle;
    @InjectView(2131428283)
    TextView tvRenewDesc;
    private String type = "";
    @InjectView(2131427716)
    View vLine;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.g5, container, false);
        ButterKnife.inject((Object) this, view);
        initView();
        return view;
    }

    private void initView() {
        if (this.servicesBean != null) {
            this.llCover.getLayoutParams().width = ResolutionUtils.getScreenWidth(getActivity())
                    - ViewUtils.dip2px(getActivity(), 40.0f);
            this.color = getResources().getColor(this.bgColor);
            this.tvNiceTitle.setTextColor(this.color);
            this.tvNiceTitle.setText(this.servicesBean.title + "\n" + this.servicesBean.month +
                    "个月");
            this.vLine.setBackgroundColor(this.color);
            this.tvNicePrice.setText("¥" + this.servicesBean.base_price);
            if (TextUtils.equals(this.type, NiceSellActivity.NICE_SERVICE)) {
                this.llBuyDesc.setVisibility(0);
                this.tvRenewDesc.setVisibility(8);
                initDescTwo();
                initDescThreeAndFour();
            } else if (TextUtils.equals(this.type, NiceSellActivity.RENEW_NICE_SERVICE)) {
                this.llBuyDesc.setVisibility(8);
                this.tvRenewDesc.setVisibility(0);
                this.tvRenewDesc.setText(this.servicesBean.month + "个月 减重顾问服务 \n (不包括方案)");
            }
            initBuyButton();
            if (TextUtils.equals(this.servicesBean.state, "on_sale")) {
                this.btBuy.setClickable(true);
                if (TextUtils.equals(this.type, NiceSellActivity.NICE_SERVICE)) {
                    this.btBuy.setText("我要购买");
                    return;
                } else {
                    this.btBuy.setText("我要续费");
                    return;
                }
            }
            this.btBuy.setClickable(false);
            this.btBuy.setText("暂未开售");
        }
    }

    private void initDescTwo() {
        SpannableStringBuilder builder = new SpannableStringBuilder(this.servicesBean.month +
                "个月减重服务");
        builder.setSpan(new ForegroundColorSpan(this.color), 0, 2, 33);
        this.tvDescTwo.setText(builder);
    }

    private void initDescThreeAndFour() {
        String desc;
        switch (this.servicesBean.month) {
            case 1:
                desc = "体重稳定减少5-8斤";
                this.tvDescFour.setText("摆脱难瘦困扰");
                break;
            case 2:
                desc = "体重稳定减少8-14斤";
                this.tvDescFour.setText("衣服小一号");
                break;
            case 3:
                desc = "体重稳定减少20斤";
                this.tvDescFour.setText("养成不易发胖体质");
                break;
            default:
                desc = "体重稳定减少20斤";
                this.tvDescFour.setText("养成不易发胖体质");
                break;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(desc);
        builder.setSpan(new ForegroundColorSpan(this.color), 6, desc.length() - 1, 33);
        this.tvDescThree.setText(builder);
    }

    private void initBuyButton() {
        switch (this.servicesBean.month) {
            case 1:
                this.btBuy.setBackgroundResource(R.drawable.gs);
                return;
            case 2:
                this.btBuy.setBackgroundResource(R.drawable.gu);
                return;
            case 3:
                this.btBuy.setBackgroundResource(R.drawable.gt);
                return;
            default:
                this.btBuy.setBackgroundResource(R.drawable.gt);
                return;
        }
    }

    public static NiceGoodsFragment newInstance(ServicesBean service, int color, String type) {
        NiceGoodsFragment fragment = new NiceGoodsFragment();
        fragment.servicesBean = service;
        fragment.bgColor = color;
        fragment.type = type;
        return fragment;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131428285})
    public void onClick() {
        if (this.servicesBean != null) {
            NiceConfirmOrderActivity.startActivity(getActivity(), this.servicesBean);
        }
    }
}
