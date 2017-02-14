package com.boohee.one.video.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.one.video.adapter.SpecialTrainAdapter;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.SpecialTrain;
import com.boohee.one.video.ui.AddSpecialLessonActivity;
import com.boohee.utils.FastJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpecialTrainPlanFragment extends BaseFragment {
    public static final String REFRESH_SPECIAL_TRAIN = "special_train_refresh";
    private SpecialTrainAdapter adapter;
    @InjectView(2131428146)
    RelativeLayout addLayout;
    private JSONArray joinedIds;
    @InjectView(2131427472)
    PullToRefreshListView pullToRefresh;
    private List<SpecialTrain> specialTrains = new ArrayList();

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gm, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        this.adapter = new SpecialTrainAdapter(getActivity(), this.specialTrains);
        initView();
        loadData();
        showLoading();
    }

    private void initView() {
        this.pullToRefresh.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                SpecialTrainPlanFragment.this.loadData();
            }
        });
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.f7, null);
        ((TextView) footerView.findViewById(R.id.tv_tips)).setVisibility(8);
        ((TextView) footerView.findViewById(R.id.tv_add_sport)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!SpecialTrainPlanFragment.this.isDetached()) {
                    AddSpecialLessonActivity.comeOn(SpecialTrainPlanFragment.this.getActivity());
                }
            }
        });
        ((ListView) this.pullToRefresh.getRefreshableView()).addFooterView(footerView);
        this.pullToRefresh.setAdapter(this.adapter);
    }

    private void loadData() {
        BooheeClient.build(BooheeClient.BINGO).get(SportApi.SPECIAL_LESSON, new JsonCallback
                (getActivity()) {
            public void onFinish() {
                int i;
                int i2 = 8;
                super.onFinish();
                SpecialTrainPlanFragment.this.pullToRefresh.onRefreshComplete();
                SpecialTrainPlanFragment.this.dismissLoading();
                PullToRefreshListView pullToRefreshListView = SpecialTrainPlanFragment.this
                        .pullToRefresh;
                if (SpecialTrainPlanFragment.this.specialTrains.size() > 0) {
                    i = 0;
                } else {
                    i = 8;
                }
                pullToRefreshListView.setVisibility(i);
                RelativeLayout relativeLayout = SpecialTrainPlanFragment.this.addLayout;
                if (SpecialTrainPlanFragment.this.specialTrains.size() <= 0) {
                    i2 = 0;
                }
                relativeLayout.setVisibility(i2);
            }

            public void ok(JSONObject object) {
                super.ok(object);
                SpecialTrainPlanFragment.this.handleData(object);
            }
        }, getActivity());
    }

    private void handleData(JSONObject object) {
        try {
            this.specialTrains.clear();
            List<SpecialTrain> allTrains = FastJsonUtils.parseList(object.optString("trainings"),
                    SpecialTrain.class);
            this.joinedIds = object.optJSONArray("joined_ids");
            for (int j = 0; j < this.joinedIds.length(); j++) {
                for (int i = 0; i < allTrains.size(); i++) {
                    SpecialTrain specialTrain = (SpecialTrain) allTrains.get(i);
                    if (specialTrain.id == Integer.valueOf((String) this.joinedIds.get(j))
                            .intValue()) {
                        specialTrain.isJioned = true;
                        if (!this.specialTrains.contains(specialTrain)) {
                            this.specialTrains.add(specialTrain);
                        }
                    }
                }
            }
            this.adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({2131428147})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_sport:
                if (!isDetached()) {
                    AddSpecialLessonActivity.comeOn(getActivity());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onEvent(String event) {
        if (TextUtils.equals(event, REFRESH_SPECIAL_TRAIN)) {
            loadData();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }
}
