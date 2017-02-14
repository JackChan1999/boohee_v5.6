package com.boohee.more;

import android.os.Bundle;
import android.widget.TextView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

public class DescriptionActivity extends GestureActivity {
    public static final int ABOUT_INDEX  = 1;
    public static final int CLAUSE_INDEX = 2;
    public static final int LEVEL_INDEX  = 0;
    private TextView content;
    private int      index;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em);
        this.index = getIntent().getIntExtra("index", -1);
        this.content = (TextView) findViewById(R.id.description_content);
        initUI();
    }

    private void initUI() {
        switch (this.index) {
            case 0:
                this.content.setText(R.string.m3);
                return;
            case 1:
                setTitle(R.string.ai);
                this.content.setText(R.string.aj);
                return;
            case 2:
                setTitle(R.string.a83);
                this.content.setText(R.string.a84);
                return;
            default:
                return;
        }
    }
}
