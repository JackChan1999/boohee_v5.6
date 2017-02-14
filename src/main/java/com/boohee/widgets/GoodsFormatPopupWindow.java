package com.boohee.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.boohee.model.CartGoods;
import com.boohee.model.FormatModel;
import com.boohee.model.Goods;
import com.boohee.model.GoodsArgument;
import com.boohee.model.GoodsFormat;
import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.joooonho.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout.OnTagClickListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GoodsFormatPopupWindow implements OnClickListener {
    static final String TAG = GoodsFormatPopupWindow.class.getName();
    private static GoodsFormatPopupWindow sGoodsFormatPop;
    private        Button                 btn_cart_add;
    private        Button                 btn_decrease;
    private        Button                 btn_increase;
    private        Animation              inAnim;
    private        ImageView              iv_avatar;
    private        LinearLayout           ll_content;
    private        LinearLayout           ll_format;
    private Set<Integer> mCheckedRow  = new HashSet();
    private Set<Integer> mCheckedTemp = new HashSet();
    private Set<Integer> mCompareTemp = new HashSet();
    private Context mContext;
    private Goods   mGoods;
    private Map<Integer, CartGoods> mGoodsMap = new HashMap();
    private OnSelectListener mOnSelectListener;
    private int                       mQuantity      = 1;
    private int                       mSelectGoodsId = -1;
    private Map<Integer, FormatModel> map_tip        = new HashMap();
    private Animation   outAnim;
    private PopupWindow popWindow;
    private String      tips;
    private TextView    tv_goods_num;
    private TextView    txt_price;
    private TextView    txt_tips;

    public interface OnSelectListener {
        void onSelect(boolean z, int i, int i2, String str);
    }

    public static GoodsFormatPopupWindow getInstance() {
        if (sGoodsFormatPop == null) {
            sGoodsFormatPop = new GoodsFormatPopupWindow();
        }
        return sGoodsFormatPop;
    }

    private void createPopWindow(Context context, Goods goods) {
        this.mContext = context;
        this.mGoods = goods;
        if (this.mGoods != null) {
            initOptionGoods(this.mGoods);
            this.popWindow = new PopupWindow(createContentView(this.mGoods.specs), -1, -2, true);
            this.popWindow.setBackgroundDrawable(new BitmapDrawable());
            this.popWindow.setFocusable(true);
            this.popWindow.setTouchable(true);
            this.popWindow.setOutsideTouchable(true);
            this.inAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.s);
            this.outAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.t);
        }
    }

    private View createContentView(List<GoodsFormat> goodsFormats) {
        View contentView = View.inflate(this.mContext, R.layout.p8, null);
        contentView.findViewById(R.id.mask).setOnClickListener(this);
        this.ll_content = (LinearLayout) contentView.findViewById(R.id.ll_content);
        this.ll_content.setOnClickListener(null);
        this.ll_format = (LinearLayout) contentView.findViewById(R.id.ll_format);
        this.iv_avatar = (SelectableRoundedImageView) contentView.findViewById(R.id.iv_avatar);
        this.btn_cart_add = (Button) contentView.findViewById(R.id.btn_cart_add);
        this.btn_decrease = (Button) contentView.findViewById(R.id.btn_decrease);
        this.btn_increase = (Button) contentView.findViewById(R.id.btn_increase);
        contentView.findViewById(R.id.iv_closed).setOnClickListener(this);
        this.btn_cart_add.setOnClickListener(this);
        this.btn_decrease.setOnClickListener(this);
        this.btn_increase.setOnClickListener(this);
        this.txt_tips = (TextView) contentView.findViewById(R.id.txt_tips);
        this.txt_price = (TextView) contentView.findViewById(R.id.txt_price);
        this.tv_goods_num = (TextView) contentView.findViewById(R.id.tv_goods_num);
        this.mQuantity = this.mGoods.quantity;
        this.tv_goods_num.setText(this.mQuantity + "");
        this.btn_decrease.setEnabled(this.mQuantity > 1);
        if (!TextUtil.isEmpty(this.mGoods.big_photo_url)) {
            ImageLoader.getInstance().displayImage(this.mGoods.big_photo_url, this.iv_avatar,
                    ImageLoaderOptions.global((int) R.drawable.aa3));
        }
        this.txt_price.setText(this.mGoods.base_price);
        if (goodsFormats != null && goodsFormats.size() > 0) {
            initCheckedList(goodsFormats);
            initIsCanClick(goodsFormats);
            initFormat(goodsFormats);
            getGoodsId(goodsFormats);
        }
        return contentView;
    }

    private void initOptionGoods(Goods goods) {
        if (goods.option_goods != null && goods.option_goods.size() > 0) {
            for (int i = 0; i < goods.option_goods.size(); i++) {
                CartGoods cartGoods = (CartGoods) goods.option_goods.get(i);
                this.mGoodsMap.put(Integer.valueOf(cartGoods.id), cartGoods);
            }
        }
    }

    private void initFormat(final List<GoodsFormat> goodsFormats) {
        this.ll_format.removeAllViews();
        for (int i = 0; i < goodsFormats.size(); i++) {
            GoodsFormat goodsFormat = (GoodsFormat) goodsFormats.get(i);
            View itemView = LayoutInflater.from(this.mContext).inflate(R.layout.i4, null);
            TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            if (!TextUtils.isEmpty(goodsFormat.name)) {
                tv_title.setText(goodsFormat.name);
            }
            final List<GoodsArgument> goodsArguments = goodsFormat.arguments;
            if (!(goodsArguments == null || goodsArguments.size() == 0)) {
                final TagFlowLayout tfl_format = (TagFlowLayout) itemView.findViewById(R.id
                        .tfl_format);
                TagAdapter<GoodsArgument> tagAdapter = new TagAdapter<GoodsArgument>
                        (goodsArguments) {
                    public View getView(FlowLayout parent, int position, GoodsArgument
                            goodsArgument) {
                        TextView tv = (TextView) LayoutInflater.from(GoodsFormatPopupWindow.this
                                .mContext).inflate(R.layout.i2, tfl_format, false);
                        tv.setText(goodsArgument.name);
                        if (!goodsArgument.isCanClick) {
                            tv.setBackgroundResource(R.drawable.hl);
                            tv.setTextColor(GoodsFormatPopupWindow.this.mContext.getResources()
                                    .getColor(R.color.du));
                            tv.getPaint().setFlags(17);
                            tv.setAlpha(0.5f);
                        } else if (goodsArgument.isChecked) {
                            tv.setBackgroundResource(R.drawable.hm);
                            tv.setTextColor(GoodsFormatPopupWindow.this.mContext.getResources()
                                    .getColor(R.color.he));
                        } else {
                            tv.setTextColor(GoodsFormatPopupWindow.this.mContext.getResources()
                                    .getColor(R.color.e4));
                        }
                        return tv;
                    }
                };
                tfl_format.setAdapter(tagAdapter);
                if (getCheckedIndex(goodsArguments) != -1) {
                    tagAdapter.setSelectedList(new int[]{getCheckedIndex(goodsArguments)});
                }
                tfl_format.setOnTagClickListener(new OnTagClickListener() {
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        GoodsArgument goodsArgument = (GoodsArgument) goodsArguments.get(position);
                        if (goodsArgument.isCanClick) {
                            boolean z;
                            if (goodsArgument.isChecked) {
                                z = false;
                            } else {
                                z = true;
                            }
                            goodsArgument.isChecked = z;
                            for (int i = 0; i < goodsArguments.size(); i++) {
                                if (i != position) {
                                    ((GoodsArgument) goodsArguments.get(i)).isChecked = false;
                                }
                            }
                            GoodsFormatPopupWindow.this.initCheckedList(goodsFormats);
                            GoodsFormatPopupWindow.this.initIsCanClick(goodsFormats);
                            GoodsFormatPopupWindow.this.initFormat(goodsFormats);
                            GoodsFormatPopupWindow.this.getGoodsId(goodsFormats);
                        }
                        return false;
                    }
                });
                this.ll_format.addView(itemView);
            }
        }
    }

    private void initCheckedList(List<GoodsFormat> goodsFormats) {
        this.mCheckedTemp.clear();
        this.mCheckedRow.clear();
        this.map_tip.clear();
        for (int i = 0; i < goodsFormats.size(); i++) {
            List<GoodsArgument> arguments = ((GoodsFormat) goodsFormats.get(i)).arguments;
            this.map_tip.put(Integer.valueOf(i), new FormatModel(false, ((GoodsFormat)
                    goodsFormats.get(i)).name));
            for (int j = 0; j < arguments.size(); j++) {
                GoodsArgument argument = (GoodsArgument) arguments.get(j);
                Set<Integer> ids = argument.goods_ids;
                if (argument.isChecked && ids != null) {
                    this.mCheckedRow.add(Integer.valueOf(i));
                    if (this.mCheckedTemp.size() == 0) {
                        this.mCheckedTemp.addAll(ids);
                    } else {
                        this.mCheckedTemp.retainAll(ids);
                    }
                    this.map_tip.put(Integer.valueOf(i), new FormatModel(true, argument.name));
                }
            }
        }
    }

    private void initIsCanClick(List<GoodsFormat> goodsFormats) {
        int i;
        List<GoodsArgument> arguments;
        int j;
        GoodsArgument argument;
        Set<Integer> ids;
        if (this.mCheckedRow.size() == goodsFormats.size()) {
            for (i = 0; i < goodsFormats.size(); i++) {
                Set<Integer> otherList = getOthersList(i, goodsFormats);
                arguments = ((GoodsFormat) goodsFormats.get(i)).arguments;
                for (j = 0; j < arguments.size(); j++) {
                    argument = (GoodsArgument) arguments.get(j);
                    ids = argument.goods_ids;
                    if (!(argument.isChecked || ids == null)) {
                        this.mCompareTemp.clear();
                        this.mCompareTemp.addAll(ids);
                        if (otherList.size() > 0) {
                            this.mCompareTemp.retainAll(otherList);
                        }
                        argument.isCanClick = this.mCompareTemp.size() > 0;
                    }
                }
            }
            return;
        }
        for (i = 0; i < goodsFormats.size(); i++) {
            if (!this.mCheckedRow.contains(Integer.valueOf(i))) {
                arguments = ((GoodsFormat) goodsFormats.get(i)).arguments;
                for (j = 0; j < arguments.size(); j++) {
                    argument = (GoodsArgument) arguments.get(j);
                    ids = argument.goods_ids;
                    if (ids == null || ids.size() == 0) {
                        argument.isCanClick = false;
                    } else {
                        this.mCompareTemp.clear();
                        this.mCompareTemp.addAll(ids);
                        if (this.mCheckedTemp.size() == 0) {
                            argument.isCanClick = true;
                        } else {
                            this.mCompareTemp.retainAll(this.mCheckedTemp);
                            argument.isCanClick = this.mCompareTemp.size() > 0;
                        }
                    }
                }
            }
        }
    }

    private Set<Integer> getOthersList(int row, List<GoodsFormat> goodsFormats) {
        Set<Integer> temp = new HashSet();
        for (int i = 0; i < goodsFormats.size(); i++) {
            if (row != i) {
                List<GoodsArgument> arguments = ((GoodsFormat) goodsFormats.get(i)).arguments;
                for (int j = 0; j < arguments.size(); j++) {
                    GoodsArgument argument = (GoodsArgument) arguments.get(j);
                    Set<Integer> ids = argument.goods_ids;
                    if (argument.isChecked && ids != null) {
                        if (temp.size() == 0) {
                            temp.addAll(ids);
                        } else {
                            temp.retainAll(ids);
                        }
                    }
                }
            }
        }
        return temp;
    }

    private void getGoodsId(List<GoodsFormat> goodsFormats) {
        if (this.mCheckedRow.size() == goodsFormats.size()) {
            this.btn_cart_add.setEnabled(true);
            Iterator it = this.mCheckedTemp.iterator();
            if (it.hasNext()) {
                this.mSelectGoodsId = ((Integer) it.next()).intValue();
            }
            this.tips = getTips(this.map_tip, true);
            if (!TextUtils.isEmpty(this.tips)) {
                this.txt_tips.setText(this.tips);
                if (this.mOnSelectListener != null) {
                    this.mOnSelectListener.onSelect(false, this.mSelectGoodsId, this.mQuantity,
                            this.tips);
                }
            }
            if (this.mSelectGoodsId != -1) {
                CartGoods g = (CartGoods) this.mGoodsMap.get(Integer.valueOf(this.mSelectGoodsId));
                if (g != null) {
                    this.txt_price.setText("￥" + g.base_price);
                    this.mQuantity = this.mGoods.quantity;
                    ImageLoader.getInstance().displayImage(g.big_photo_url, this.iv_avatar,
                            ImageLoaderOptions.global((int) R.drawable.aa3));
                    return;
                }
                return;
            }
            return;
        }
        this.btn_cart_add.setEnabled(false);
        this.mSelectGoodsId = -1;
        this.tips = getTips(this.map_tip, false);
        if (!TextUtils.isEmpty(this.tips)) {
            this.txt_tips.setText(this.tips);
            if (this.mOnSelectListener != null) {
                this.mOnSelectListener.onSelect(false, this.mSelectGoodsId, this.mQuantity, this
                        .tips);
            }
        }
    }

    private String getTips(Map<Integer, FormatModel> map_tip, boolean isCanAdd) {
        StringBuffer sb_tip = new StringBuffer();
        FormatModel formatModel;
        if (isCanAdd) {
            sb_tip.append("已选择 ");
            for (Entry<Integer, FormatModel> entry : map_tip.entrySet()) {
                formatModel = (FormatModel) entry.getValue();
                if (formatModel.isChecked) {
                    sb_tip.append(formatModel.name + " ");
                }
            }
        } else {
            sb_tip.append("请选择");
            for (Entry<Integer, FormatModel> entry2 : map_tip.entrySet()) {
                formatModel = (FormatModel) entry2.getValue();
                if (!formatModel.isChecked) {
                    sb_tip.append(" " + formatModel.name);
                }
            }
        }
        return sb_tip.toString();
    }

    private int getCheckedIndex(List<GoodsArgument> goodsArguments) {
        if (goodsArguments == null || goodsArguments.size() == 0) {
            return -1;
        }
        for (int i = 0; i < goodsArguments.size(); i++) {
            if (((GoodsArgument) goodsArguments.get(i)).isChecked) {
                return i;
            }
        }
        return -1;
    }

    public synchronized boolean isShowing() {
        boolean z;
        z = this.popWindow != null && this.popWindow.isShowing();
        return z;
    }

    public synchronized void dismiss() {
        if (isShowing()) {
            this.popWindow.dismiss();
            this.ll_content.startAnimation(this.outAnim);
            if (this.mOnSelectListener != null) {
                this.mOnSelectListener.onSelect(false, this.mSelectGoodsId, this.mQuantity, this
                        .tips);
            }
        }
    }

    public synchronized void show(Context context, Goods goods) {
        createPopWindow(context, goods);
        if (!(this.popWindow == null || this.popWindow.isShowing())) {
            this.popWindow.showAtLocation(new View(context), 48, 0, 0);
            this.ll_content.startAnimation(this.inAnim);
        }
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
    }

    public void onClick(View v) {
        Goods goods;
        switch (v.getId()) {
            case R.id.btn_cart_add:
                if (this.mSelectGoodsId == -1) {
                    Helper.showToast(TextUtils.isEmpty(this.tips) ? "请完成商品规格选择" : this.tips);
                    return;
                } else if (this.mOnSelectListener != null) {
                    this.mOnSelectListener.onSelect(true, this.mSelectGoodsId, this.mQuantity,
                            this.tips);
                    return;
                } else {
                    return;
                }
            case R.id.btn_decrease:
                goods = this.mGoods;
                goods.quantity--;
                this.mQuantity = this.mGoods.quantity;
                if (this.mGoods.quantity == 1) {
                    this.btn_decrease.setEnabled(false);
                }
                this.tv_goods_num.setText(this.mQuantity + "");
                return;
            case R.id.btn_increase:
                goods = this.mGoods;
                goods.quantity++;
                this.mQuantity = this.mGoods.quantity;
                this.btn_decrease.setEnabled(true);
                this.tv_goods_num.setText(this.mQuantity + "");
                return;
            default:
                dismiss();
                return;
        }
    }
}
