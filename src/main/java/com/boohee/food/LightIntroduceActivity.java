package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

public class LightIntroduceActivity extends GestureActivity {
    public static final String COLOR_GREEN  = "#59B700";
    public static final String COLOR_RED    = "#FF3300";
    public static final String COLOR_YELLOW = "#FFB700";
    @InjectView(2131427760)
    TextView tvGreen;
    @InjectView(2131427766)
    TextView tvRed;
    @InjectView(2131427763)
    TextView tvYellow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bx);
        ButterKnife.inject((Activity) this);
        setItemText(R.string.qu, COLOR_GREEN, R.string.qt, this.tvGreen);
        setItemText(R.string.qz, COLOR_YELLOW, R.string.qy, this.tvYellow);
        setItemText(R.string.qw, COLOR_RED, R.string.qv, this.tvRed);
    }

    private void setItemText(int title, String color, int content, TextView textView) {
        StringBuilder builder = new StringBuilder();
        builder.append("<font color='" + color + "'>");
        builder.append("<big><big>");
        builder.append(getResources().getString(title));
        builder.append("</big></big>");
        builder.append("</font>");
        builder.append("<big>");
        builder.append(getResources().getString(content));
        builder.append("</big>");
        textView.setText(Html.fromHtml(builder.toString()));
    }

    public static void comeOnBaby(Context context) {
        context.startActivity(new Intent(context, LightIntroduceActivity.class));
    }
}
