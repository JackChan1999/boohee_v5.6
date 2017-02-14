package com.boohee.more;

import android.os.Bundle;
import android.webkit.WebView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

public class SubjectActivity extends GestureActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mc);
        setTitle(R.string.a7e);
        initUI();
    }

    private void initUI() {
        ((WebView) findViewById(R.id.subject_content)).loadUrl("file:///android_asset/home.html");
    }
}
