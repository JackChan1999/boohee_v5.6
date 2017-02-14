package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.boohee.model.FormatModel;
import com.boohee.model.Showcase;
import com.boohee.model.Showcase.ExhibitType;
import com.boohee.one.ui.MainActivity;
import com.boohee.one.ui.ShopLabelActivity;
import com.boohee.uchoice.GoodsDetailActivity;
import com.boohee.utility.BooheeScheme;

import java.util.List;

public class ShopUtils {
    public static void handleExhibit(Context context, Showcase showcase) {
        int id = -1;
        if (TextUtils.equals(showcase.exhibit_type, ExhibitType.category_label.name()) ||
                TextUtils.equals(showcase.exhibit_type, ExhibitType.topic_label.name()) ||
                TextUtils.equals(showcase.exhibit_type, ExhibitType.goods.name())) {
            try {
                id = Integer.parseInt(showcase.exhibit);
            } catch (Exception e) {
                return;
            }
        }
        if (TextUtils.equals(showcase.exhibit_type, ExhibitType.category_label.name())) {
            ShopLabelActivity.start(context, id);
        } else if (TextUtils.equals(showcase.exhibit_type, ExhibitType.topic_label.name())) {
            ShopLabelActivity.start(context, id);
        } else if (TextUtils.equals(showcase.exhibit_type, ExhibitType.goods.name())) {
            GoodsDetailActivity.comeOnBaby(context, id);
        } else if (TextUtils.equals(showcase.exhibit_type, ExhibitType.page.name())) {
            BooheeScheme.handleUrl(context, showcase.exhibit, showcase.page_title);
        }
    }

    public static String getFormatInfo(List<FormatModel> chosen_specs) {
        if (chosen_specs == null || chosen_specs.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (FormatModel formatModel : chosen_specs) {
            stringBuffer.append(" " + formatModel.name);
        }
        return stringBuffer.toString();
    }

    public static void scanAnyWhere(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(67108864);
        intent.putExtra(MainActivity.KEY_ONNEWINTENT, 3);
        context.startActivity(intent);
        context.finish();
    }
}
