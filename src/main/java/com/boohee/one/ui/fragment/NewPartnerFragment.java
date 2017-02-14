package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.account.HomeImagePagerAdapter;
import com.boohee.api.StatusApi;
import com.boohee.model.Blocks;
import com.boohee.model.Category;
import com.boohee.model.HomeSlider;
import com.boohee.model.PartnerBlock;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.transform.TransformManager;
import com.boohee.one.ui.HomeMoreActivity;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewPartnerFragment extends BaseFragment {
    private HomeImagePagerAdapter adapter;
    private List<Blocks>   blocks    = new ArrayList();
    private List<Category> categorys = new ArrayList();
    @InjectView(2131428276)
    FrameLayout flPartner;
    private Handler handler = new Handler();
    @InjectView(2131427825)
    LinePageIndicator indicator;
    public boolean isLoadFirst = true;
    @InjectView(2131427647)
    LinearLayout llContent;
    private int      mCurrentItem       = 0;
    private Runnable mHomeImageRunnable = new Runnable() {
        public void run() {
            try {
                if (NewPartnerFragment.this.sliders != null && NewPartnerFragment.this.sliders
                        .size() != 0 && NewPartnerFragment.this.adapter != null &&
                        NewPartnerFragment.this.adapter.getCount() > 1) {
                    if (NewPartnerFragment.this.mCurrentItem > NewPartnerFragment.this.sliders
                            .size() - 1) {
                        NewPartnerFragment.this.mCurrentItem = 0;
                    }
                    NewPartnerFragment.this.viewPager.setCurrentItem(NewPartnerFragment.this
                            .mCurrentItem, true);
                    NewPartnerFragment.this.indicator.setCurrentItem(NewPartnerFragment.this
                            .mCurrentItem);
                    NewPartnerFragment.this.mCurrentItem = NewPartnerFragment.this.mCurrentItem + 1;
                    NewPartnerFragment.this.handler.postDelayed(NewPartnerFragment.this
                            .mHomeImageRunnable, 5000);
                }
            } catch (NullPointerException e) {
            }
        }
    };
    @InjectView(2131427340)
    PullToRefreshScrollView scrollView;
    private List<HomeSlider> sliders = new ArrayList();
    @InjectView(2131428277)
    ViewPager viewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.g4, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addListener();
        initShow(this.mCache.getAsJSONObject(CacheKey.NEW_SQUARE));
    }

    private void addListener() {
        this.scrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                NewPartnerFragment.this.initUI();
            }
        });
        this.indicator.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                NewPartnerFragment.this.mCurrentItem = position;
            }
        });
    }

    public void loadFirst() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (NewPartnerFragment.this.isLoadFirst && NewPartnerFragment.this.scrollView !=
                        null) {
                    NewPartnerFragment.this.scrollView.setRefreshing(true);
                }
            }
        }, 800);
    }

    private void initUI() {
        StatusApi.getMainSquare(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (NewPartnerFragment.this.getActivity() != null && object != null) {
                    NewPartnerFragment.this.initShow(object);
                    NewPartnerFragment.this.mCache.put(CacheKey.NEW_SQUARE, object);
                }
            }

            public void onFinish() {
                super.onFinish();
                NewPartnerFragment.this.isLoadFirst = false;
                NewPartnerFragment.this.scrollView.onRefreshComplete();
            }
        });
    }

    private void initShow(JSONObject object) {
        if (getActivity() != null && object != null) {
            initSlider(object);
            this.llContent.removeAllViews();
            initBlock(object);
            initCategory(object);
        }
    }

    private void initSlider(JSONObject object) {
        JSONArray slidersArray = object.optJSONArray("sliders");
        if (slidersArray == null || slidersArray.length() <= 0) {
            this.sliders = null;
        } else {
            this.sliders = HomeSlider.parseSliders(slidersArray.toString());
        }
        initHeadAd();
    }

    private void initBlock(JSONObject object) {
        this.blocks = Blocks.parseBlocks(object.optString("blocks"));
        if (this.blocks != null && this.blocks.size() > 0) {
            for (Blocks bl : this.blocks) {
                if (bl.block != null && bl.block.size() > 0) {
                    showBlock(bl.block);
                }
            }
        }
    }

    private void initCategory(JSONObject object) {
        this.categorys = Category.parseCategories(object.optString("categories"));
        if (this.categorys != null && this.categorys.size() > 0) {
            for (Category category : this.categorys) {
                showCategory(category);
            }
        }
    }

    private void showBlock(List<PartnerBlock> blockList) {
        if (blockList != null && blockList.size() != 0) {
            View bottomView = LayoutInflater.from(getActivity()).inflate(R.layout.qb, null);
            LinearLayout contentView = (LinearLayout) bottomView.findViewById(R.id
                    .ll_block_content);
            int i = 0;
            while (i < blockList.size()) {
                View view = getBlockItem((PartnerBlock) blockList.get(i));
                if (view != null && i == blockList.size() - 1) {
                    view.findViewById(R.id.v_bottom_line).setVisibility(8);
                }
                if (view != null) {
                    contentView.addView(view);
                }
                i++;
            }
            this.llContent.addView(bottomView);
        }
    }

    private void showCategory(final Category category) {
        if (category != null) {
            View bottomView = LayoutInflater.from(getActivity()).inflate(R.layout.qb, null);
            LinearLayout contentView = (LinearLayout) bottomView.findViewById(R.id
                    .ll_block_content);
            contentView.addView(getCategoryTopView(category.title, new OnClickListener() {
                public void onClick(View v) {
                    if (NewPartnerFragment.this.getActivity() != null) {
                        MobclickAgent.onEvent(NewPartnerFragment.this.getActivity(), Event
                                .status_clickCategoryMore);
                        if (!TextUtils.isEmpty(category.getMore_url()) && !BooheeScheme.handleUrl
                                (NewPartnerFragment.this.getActivity(), category.getMore_url())) {
                            HomeMoreActivity.comeOnBaby(NewPartnerFragment.this.getActivity(),
                                    category.getMore_url(), category.getTitle());
                        }
                    }
                }
            }));
            if (!(category.banner_pic_url == null || TextUtils.isEmpty(category.banner_pic_url))) {
                contentView.addView(getImageItemView(category));
            }
            if (category.events != null && category.events.size() > 0) {
                int i = 0;
                while (i < category.events.size()) {
                    View view = getSelectItemView((com.boohee.model.Event) category.events.get(i));
                    if (view != null) {
                        contentView.addView(view);
                    }
                    if (view != null && i == 0) {
                        view.findViewById(R.id.v_divider_line).setVisibility(8);
                    }
                    i++;
                }
            }
            this.llContent.addView(bottomView);
        }
    }

    private View getSelectItemView(final com.boohee.model.Event event) {
        if (event == null) {
            return null;
        }
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.k7, null);
        TextView select_name_tv = (TextView) itemView.findViewById(R.id.main_group_item_name_tv);
        TextView select_title = (TextView) itemView.findViewById(R.id.main_group_item_title_tv);
        this.imageLoader.displayImage(event.pic_url, (ImageView) itemView.findViewById(R.id
                .main_group_iv), ImageLoaderOptions.global((int) R.drawable.aa4));
        select_name_tv.setText(event.title);
        select_title.setText(event.desc);
        itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MobclickAgent.onEvent(NewPartnerFragment.this.getActivity(), Event
                        .status_clickCategoryItem);
                if (!TextUtils.isEmpty(event.url)) {
                    BooheeScheme.handleUrl(NewPartnerFragment.this.getActivity(), event.url);
                }
            }
        });
        return itemView;
    }

    private View getImageItemView(final Category category) {
        if (category == null) {
            return null;
        }
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.oe, null);
        ImageView iv_image = (ImageView) itemView.findViewById(R.id.iv_category_img);
        this.imageLoader.displayImage(category.banner_pic_url, iv_image, ImageLoaderOptions
                .global((int) R.drawable.aa4));
        ViewUtils.setViewScaleHeight(getActivity(), iv_image, 640, 230);
        iv_image.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!TextUtils.isEmpty(category.banner_link)) {
                    MobclickAgent.onEvent(NewPartnerFragment.this.getActivity(), Event
                            .status_clickCategoryBanner);
                    BooheeScheme.handleUrl(NewPartnerFragment.this.getActivity(), category
                            .banner_link);
                }
            }
        });
        return itemView;
    }

    private View getBlockItem(final PartnerBlock block) {
        if (block == null) {
            return null;
        }
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout. if,null);
        TextView select_name_tv = (TextView) itemView.findViewById(R.id.tv_item_name);
        TextView select_title = (TextView) itemView.findViewById(R.id.tv_item_desc);
        ImageView iv_sign = (ImageView) itemView.findViewById(R.id.iv_sign);
        final TextView tv_number = (TextView) itemView.findViewById(R.id.tv_item_num);
        this.imageLoader.displayImage(block.pic_url, (ImageView) itemView.findViewById(R.id
                .iv_item_img), ImageLoaderOptions.global((int) R.drawable.aa4));
        select_name_tv.setText(block.title);
        select_title.setText(block.desc);
        if (block.data_type != null) {
            String str = block.data_type;
            int i = -1;
            switch (str.hashCode()) {
                case -931257130:
                    if (str.equals(PartnerBlock.TYPE_RIBBON)) {
                        i = 2;
                        break;
                    }
                    break;
                case 3556653:
                    if (str.equals("text")) {
                        i = 1;
                        break;
                    }
                    break;
                case 106845584:
                    if (str.equals(PartnerBlock.TYPE_POINT)) {
                        i = 0;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    if (!block.is_new) {
                        tv_number.setText("");
                        break;
                    }
                    tv_number.setText("New");
                    break;
                case 1:
                    tv_number.setText(block.data);
                    break;
                case 2:
                    iv_sign.setVisibility(0);
                    break;
            }
        }
        itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NewPartnerFragment.this.sendClickCount(block.id);
                tv_number.setText("");
                if (block.is_new) {
                    NewPartnerFragment.this.clearBlockDot(block.id);
                }
                if (!TextUtils.isEmpty(block.link_to)) {
                    BooheeScheme.handleUrl(NewPartnerFragment.this.getActivity(), block.link_to);
                }
            }
        });
        return itemView;
    }

    private void sendClickCount(int id) {
        switch (id) {
            case 1:
                MobclickAgent.onEvent(getActivity(), Event.status_friendTimeline);
                return;
            case 2:
                MobclickAgent.onEvent(getActivity(), Event.status_hotTimeline);
                return;
            case 3:
                MobclickAgent.onEvent(getActivity(), Event.status_todayContent);
                return;
            case 4:
                MobclickAgent.onEvent(getActivity(), Event.status_betHome);
                return;
            default:
                return;
        }
    }

    private void clearBlockDot(int id) {
        StatusApi.clearBlockDot(id, getActivity(), new JsonCallback(getActivity()));
    }

    private View getCategoryTopView(String title, OnClickListener listener) {
        View topView = LayoutInflater.from(getActivity()).inflate(R.layout.of, null);
        ((TextView) topView.findViewById(R.id.tv_category_title)).setText(title);
        topView.findViewById(R.id.tv_category_more).setOnClickListener(listener);
        return topView;
    }

    private void initHeadAd() {
        if (this.sliders == null || this.sliders.size() <= 0) {
            this.flPartner.setVisibility(8);
            return;
        }
        this.flPartner.setVisibility(0);
        this.adapter = new HomeImagePagerAdapter(getChildFragmentManager(), this.sliders);
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setPageTransformer(true, TransformManager.getRandomTransform());
        this.indicator.setViewPager(this.viewPager);
        ViewUtils.setViewScaleHeight(getActivity(), this.viewPager, 750, 250);
        startPlayHomeImage();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    private void startPlayHomeImage() {
        this.handler.removeCallbacks(this.mHomeImageRunnable);
        if (this.sliders.size() > 1) {
            this.mCurrentItem = 0;
            this.indicator.setVisibility(0);
            this.handler.post(this.mHomeImageRunnable);
            return;
        }
        this.indicator.setVisibility(8);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mHomeImageRunnable != null) {
            this.handler.removeCallbacks(this.mHomeImageRunnable);
        }
        if (!isDetached() && getActivity() != null) {
        }
    }
}
