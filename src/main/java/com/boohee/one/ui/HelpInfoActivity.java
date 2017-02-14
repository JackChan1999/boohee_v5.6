package com.boohee.one.ui;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.HelpInfoAdapter;
import com.boohee.status.FriendShipActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpInfoActivity extends GestureActivity {
    private HelpInfoAdapter           adapter;
    private ExpandableListView        lvExp;
    private Map<String, List<String>> mChildDatas;
    private List<String>              mHeaderDatas;
    private int position = 0;

    private class MOnGroupExpandableListener implements OnGroupExpandListener {
        private MOnGroupExpandableListener() {
        }

        public void onGroupExpand(int groupPosition) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("帮助");
        setContentView(R.layout.bi);
        init();
    }

    private void init() {
        this.position = getIntent().getIntExtra(FriendShipActivity.FRIENDSHIP_POSITION, this
                .position);
        this.lvExp = (ExpandableListView) findViewById(R.id.help_info_listview);
        prepareDatas();
        this.adapter = new HelpInfoAdapter(this, this.mHeaderDatas, this.mChildDatas);
        this.lvExp.setAdapter(this.adapter);
        this.lvExp.setOnGroupExpandListener(new MOnGroupExpandableListener());
        this.lvExp.expandGroup(this.position);
    }

    private void prepareDatas() {
        this.mHeaderDatas = new ArrayList();
        this.mChildDatas = new HashMap();
        this.mHeaderDatas.add("什么是BMI?");
        this.mHeaderDatas.add("什么是预算热量?");
        this.mHeaderDatas.add("什么是身体年龄?");
        this.mHeaderDatas.add("什么是体脂率?");
        this.mHeaderDatas.add("什么是燃脂心率?");
        List<String> bmi = new ArrayList();
        bmi.add("BMI(Body Mass Index)即BMI指数，也叫身体质量指数，是衡量是否肥胖和标准体重的重要指标。");
        List<String> hot = new ArrayList();
        hot.add("预算热量是减肥时每日饮食摄入的最佳热量值，由NICE减重顾问组以美国USDA" +
                "的权威研究和结论为基础，结合中国减肥人群的特点和需求，独家研发而来。依照最佳热量预算控制饮食热量的摄入，配合合理的运动计划，就能够有效实现热量负平衡，最终达到减肥的目的。");
        List<String> age = new ArrayList();
        age.add("身体年龄是综合体重、身体脂肪率等多种指数计算后得出的，它是一个高于或低于实际年龄的综合判断身体状况的标准。");
        List<String> rate = new ArrayList();
        rate.add("体脂率是指人体内脂肪重量在人体总体重中所占的比例，又称体脂百分数，它反映人体内脂肪含量的多少。");
        List<String> fire = new ArrayList();
        fire.add("即人体在进行燃脂运动时保持的心率状态。燃脂运动就是燃烧脂肪的运动，需要满足下面三个必要条件：\n1.该运动要达到中低强度的运动心率；\n2" +
                ".这种中低强度运动心率的运动要持续20分钟以上；\n3.这种运动必须是大肌肉群的运动，如慢跑、游泳、健身操等。");
        this.mChildDatas.put(this.mHeaderDatas.get(0), bmi);
        this.mChildDatas.put(this.mHeaderDatas.get(1), hot);
        this.mChildDatas.put(this.mHeaderDatas.get(2), age);
        this.mChildDatas.put(this.mHeaderDatas.get(3), rate);
        this.mChildDatas.put(this.mHeaderDatas.get(4), fire);
    }
}
