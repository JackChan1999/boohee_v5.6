package com.boohee.myview;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boohee.one.R;

public class PasscodeView extends FrameLayout {
    static final String TAG = PasscodeView.class.getSimpleName();
    private Context                    ctx;
    private OnPasswordCompleteListener listener;
    private EditText                   password1;
    private EditText                   password2;
    private EditText                   password3;
    private EditText                   password4;
    private TextView                   titleText;

    public interface OnPasswordCompleteListener {
        void onPasswordComplete(String str);
    }

    public PasscodeView(Context context) {
        super(context);
        init();
    }

    public PasscodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasscodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.lr, null));
        ((Activity) this.ctx).getWindow().setSoftInputMode(16);
        findView();
    }

    private void findView() {
        this.titleText = (TextView) findViewById(R.id.title);
        this.password1 = (EditText) findViewById(R.id.password1);
        this.password2 = (EditText) findViewById(R.id.password2);
        this.password3 = (EditText) findViewById(R.id.password3);
        this.password4 = (EditText) findViewById(R.id.password4);
        setFocusable(1);
        this.password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.password3.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.password4.setTransformationMethod(PasswordTransformationMethod.getInstance());
        setTextChangeListener(this.password1, 2);
        setTextChangeListener(this.password2, 3);
        setTextChangeListener(this.password3, 4);
        setTextChangeListener(this.password4, 0);
    }

    private void setFocusable(int i) {
        this.password1.setFocusableInTouchMode(false);
        this.password2.setFocusableInTouchMode(false);
        this.password3.setFocusableInTouchMode(false);
        this.password4.setFocusableInTouchMode(false);
        switch (i) {
            case 1:
                this.password1.setFocusableInTouchMode(true);
                this.password1.requestFocus();
                return;
            case 2:
                this.password2.setFocusableInTouchMode(true);
                this.password2.requestFocus();
                return;
            case 3:
                this.password3.setFocusableInTouchMode(true);
                this.password3.requestFocus();
                return;
            case 4:
                this.password4.setFocusableInTouchMode(true);
                this.password4.requestFocus();
                return;
            default:
                return;
        }
    }

    public void emptyInput() {
        this.password1.setText("");
        this.password2.setText("");
        this.password3.setText("");
        this.password4.setText("");
        setFocusable(1);
    }

    private void setTextChangeListener(EditText editText, final int index) {
        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    PasscodeView.this.setFocusable(index);
                    if (index == 0 && PasscodeView.this.listener != null) {
                        PasscodeView.this.listener.onPasswordComplete(PasscodeView.this
                                .getPassword());
                    }
                }
            }
        });
    }

    public void setPasscodeTitle(int resid) {
        this.titleText.setText(resid);
    }

    public String getPassword() {
        return this.password1.getText().toString() + this.password2.getText().toString() + this
                .password3.getText().toString() + this.password4.getText().toString();
    }

    public void setOnPasswordCompleteListener(OnPasswordCompleteListener listener) {
        this.listener = listener;
    }
}
