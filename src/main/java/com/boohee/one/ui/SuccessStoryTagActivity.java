package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.model.SuccessStory.Tag;
import com.boohee.one.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout.OnTagClickListener;

import java.io.Serializable;
import java.util.List;

public class SuccessStoryTagActivity extends GestureActivity {
    public static String  KEY_TAGS  = "key_tags";
    private       boolean isClicked = false;
    LayoutInflater layoutInflater;
    @InjectView(2131427647)
    LinearLayout llContent;
    private List<Tag> tags;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dj);
        ButterKnife.inject((Activity) this);
        this.layoutInflater = LayoutInflater.from(this.ctx);
        this.tags = (List) getIntent().getSerializableExtra(KEY_TAGS);
        init();
    }

    private void init() {
        for (int i = 0; i < this.tags.size(); i++) {
            String title = ((Tag) this.tags.get(i)).category;
            View tagView = this.layoutInflater.inflate(R.layout.jd, this.llContent, false);
            ((TextView) tagView.findViewById(R.id.tv_tag_title)).setText(title);
            TagFlowLayout tagFlowLayout = (TagFlowLayout) tagView.findViewById(R.id.tag_layout);
            final List<String> items = ((Tag) this.tags.get(i)).items;
            tagFlowLayout.setAdapter(new TagAdapter<String>(items) {
                public View getView(FlowLayout flowLayout, int i, String s) {
                    TextView textView = (TextView) SuccessStoryTagActivity.this.layoutInflater
                            .inflate(R.layout.jh, flowLayout, false);
                    textView.setText(s);
                    return textView;
                }
            });
            tagFlowLayout.setOnTagClickListener(new OnTagClickListener() {
                public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                    if (!SuccessStoryTagActivity.this.isClicked) {
                        SuccessStoryActivity.comeOnWithTag(SuccessStoryTagActivity.this.ctx,
                                (String) items.get(i));
                        SuccessStoryTagActivity.this.isClicked = true;
                    }
                    return true;
                }
            });
            this.llContent.addView(tagView);
        }
    }

    protected void onResume() {
        super.onResume();
        this.isClicked = false;
    }

    public static void comeOn(Context context, List<Tag> tags) {
        Intent intent = new Intent(context, SuccessStoryTagActivity.class);
        intent.putExtra(KEY_TAGS, (Serializable) tags);
        context.startActivity(intent);
    }
}
