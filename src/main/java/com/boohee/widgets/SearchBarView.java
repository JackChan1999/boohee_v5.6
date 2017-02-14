package com.boohee.widgets;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.boohee.one.R;
import com.boohee.utils.Helper;
import com.boohee.utils.Keyboard;

public class SearchBarView extends FrameLayout implements OnClickListener {
    static final String TAG = SearchBarView.class.getName();
    private ImageView              closeBtn;
    private OnFinishSearchListener finishSearchlistener;
    public  String                 hint;
    private boolean                inSearchMode;
    private EditText               inputTxt;
    public  String                 mQuery;
    private OnSearchListener       searchlistener;
    private TextView               txt_search;

    public interface OnSearchListener {
        void startSearch(String str);
    }

    public interface OnFinishSearchListener {
        void finishSearch();
    }

    public SearchBarView(Context context, int msg) {
        super(context);
        initView();
    }

    public SearchBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SearchBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setHint(String hint) {
        this.hint = hint;
        this.inputTxt.setHint(hint);
    }

    private void initView() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.qo, null));
        this.txt_search = (TextView) findViewById(R.id.txt_search);
        this.txt_search.setOnClickListener(this);
        this.closeBtn = (ImageView) findViewById(R.id.search_close_btn);
        this.inputTxt = (EditText) findViewById(R.id.search_text);
        this.closeBtn.setOnClickListener(this);
        this.inputTxt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    SearchBarView.this.closeBtn.setVisibility(8);
                    SearchBarView.this.changeTosearchMode();
                    return;
                }
                SearchBarView.this.closeBtn.setVisibility(0);
                SearchBarView.this.changeTodisplayMode();
            }

            public void afterTextChanged(Editable s) {
            }
        });
        changeTodisplayMode();
        this.mQuery = this.inputTxt.getText().toString().trim();
        this.inputTxt.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SearchBarView.this.changeTosearchMode();
                SearchBarView.this.mQuery = SearchBarView.this.inputTxt.getText().toString().trim();
                Keyboard.close(SearchBarView.this.getContext(), SearchBarView.this.inputTxt);
                SearchBarView.this.searchlistener.startSearch(SearchBarView.this.mQuery);
                return false;
            }
        });
    }

    public void onClick(View v) {
        if (this.inSearchMode) {
            this.finishSearchlistener.finishSearch();
            Keyboard.close(getContext(), this.inputTxt);
            this.inputTxt.setText("");
            new Handler().post(new Runnable() {
                public void run() {
                    SearchBarView.this.changeTodisplayMode();
                }
            });
            return;
        }
        this.mQuery = this.inputTxt.getText().toString().trim();
        if ("".equals(this.mQuery)) {
            Helper.showToast(getContext(), (int) R.string.ex);
            return;
        }
        changeTosearchMode();
        Keyboard.close(getContext(), this.inputTxt);
        this.searchlistener.startSearch(this.mQuery);
    }

    public void changeTosearchMode() {
        this.inSearchMode = true;
        this.txt_search.setText(getResources().getString(R.string.eq));
    }

    public void changeTodisplayMode() {
        this.inSearchMode = false;
        this.txt_search.setText(getResources().getString(R.string.a38));
    }

    public void setSearchListener(OnSearchListener listener) {
        this.searchlistener = listener;
    }

    public void setFinishSearchListener(OnFinishSearchListener listener) {
        this.finishSearchlistener = listener;
    }
}
