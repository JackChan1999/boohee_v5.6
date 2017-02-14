package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.toolbox.StringRequest;
import com.boohee.cipher.BooheeCipher;
import com.boohee.database.OnePreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.IpInfo;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.http.DnspodFree;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.RequestManager;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.uploader.QiniuConfig;
import com.boohee.utility.Config;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.BlackTech;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.SystemUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class ChangeEnvironmentActivity extends GestureActivity implements OnCheckedChangeListener {
    public static final String URL = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
    @InjectView(2131427529)
    CheckBox cbIPConnect;
    private Handler handler = new Handler();
    @InjectView(2131427528)
    RadioButton rbPRO;
    @InjectView(2131427526)
    RadioButton rbQA;
    @InjectView(2131427527)
    RadioButton rbRC;
    @InjectView(2131427525)
    RadioGroup  rgEnvironment;
    StringBuilder sb = new StringBuilder();
    @InjectView(2131427530)
    TextView tvConnectIPs;
    @InjectView(2131427524)
    TextView tvDns;
    @InjectView(2131427523)
    TextView tvIpState;
    @InjectView(2131427522)
    TextView tvNetState;
    @InjectView(2131427521)
    TextView tvPhone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ao);
        ButterKnife.inject((Activity) this);
        initView();
        showIPs();
    }

    private void initView() {
        this.tvPhone.setText(SystemUtil.getPhoneModel() + " Android" + SystemUtil.getVersionCode
                () + " one:v" + Config.getVersionName());
        if (HttpUtils.isNetworkAvailable(this.ctx)) {
            showNetInfo();
        } else {
            this.tvNetState.setText("无网络连接");
        }
        initIpView();
    }

    private void initIpView() {
        this.cbIPConnect.setChecked(BlackTech.isIPConnectOpen());
        this.cbIPConnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BlackTech.setIPConnectOpen(isChecked);
                String str = "IP直连已：%s";
                Object[] objArr = new Object[1];
                objArr[0] = BlackTech.isIPConnectOpen() ? "开启" : "关闭";
                ChangeEnvironmentActivity.this.showMessage(ChangeEnvironmentActivity.this
                        .cbIPConnect, String.format(str, objArr));
                ChangeEnvironmentActivity.this.showIPs();
            }
        });
        String env = BlackTech.getApiEnvironment();
        if (BlackTech.API_ENV_QA.equals(env)) {
            this.rbQA.setChecked(true);
        } else if (BlackTech.API_ENV_RC.equals(env)) {
            this.rbRC.setChecked(true);
        } else {
            this.rbPRO.setChecked(true);
        }
        this.rgEnvironment.setOnCheckedChangeListener(this);
    }

    private void showNetInfo() {
        this.tvNetState.setText(HttpUtils.getNetworkType(this.ctx).toString());
        JsonCallback callback = new JsonCallback(this) {
            public void fail(String message) {
                ChangeEnvironmentActivity.this.tvIpState.setText("IpInfo请求失败");
            }

            public void ok(JSONObject object) {
                if (object.optInt("code") == 0) {
                    if (((IpInfo) FastJsonUtils.fromJson(object.optString("data"), IpInfo.class))
                            != null) {
                        ChangeEnvironmentActivity.this.tvIpState.append(String.format("%s %s %s " +
                                "%s", new Object[]{info.ip, info.country, info.city, info.isp}));
                        return;
                    }
                    return;
                }
                ChangeEnvironmentActivity.this.tvIpState.append("IpInfo查询失败");
            }
        };
        RequestManager.addRequest(new StringRequest(URL, callback, callback), this);
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (InetAddress inet : InetAddress.getAllByName(BooheeClient.getHost
                            (BooheeClient.API))) {
                        ChangeEnvironmentActivity.this.sb.append(inet.getHostAddress() + " ; ");
                    }
                    ChangeEnvironmentActivity.this.handler.post(new Runnable() {
                        public void run() {
                            ChangeEnvironmentActivity.this.tvDns.setText
                                    (ChangeEnvironmentActivity.this.sb.toString());
                        }
                    });
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        boolean z;
        DnspodFree.clearIpCache();
        switch (checkedId) {
            case R.id.rb_qa:
                BlackTech.setApiEnvironment(BlackTech.API_ENV_QA);
                QiniuConfig.init(QiniuConfig.DOMAIN_QA);
                break;
            case R.id.rb_rc:
                BlackTech.setApiEnvironment(BlackTech.API_ENV_RC);
                QiniuConfig.init(QiniuConfig.DOMAIN_RC);
                break;
            case R.id.rb_pro:
                BlackTech.setApiEnvironment(BlackTech.API_ENV_PRO);
                QiniuConfig.init(QiniuConfig.DOMAIN_PRO);
                break;
            default:
                BlackTech.setApiEnvironment(BlackTech.API_ENV_PRO);
                QiniuConfig.init(QiniuConfig.DOMAIN_PRO);
                break;
        }
        Context applicationContext = getApplicationContext();
        if (BlackTech.isApiProduction().booleanValue()) {
            z = false;
        } else {
            z = true;
        }
        BooheeCipher.setModule(applicationContext, z);
        showMessage(this.rgEnvironment, String.format("准备切换到: %s", new Object[]{BlackTech
                .getApiEnvironment()}));
        this.rgEnvironment.postDelayed(new Runnable() {
            public void run() {
                AccountUtils.logout();
                Intent intent = new Intent(ChangeEnvironmentActivity.this.ctx, WelcomeActivity
                        .class);
                intent.setFlags(268468224);
                ChangeEnvironmentActivity.this.startActivity(intent);
                ChangeEnvironmentActivity.this.finish();
            }
        }, 500);
    }

    private void showIPs() {
        int i = 0;
        if (BlackTech.isIPConnectOpen()) {
            this.tvConnectIPs.setVisibility(0);
            StringBuilder builder = new StringBuilder();
            OnePreference prefs = OnePreference.getInstance(MyApplication.getContext());
            String[] strArr = DnspodFree.hosts;
            int length = strArr.length;
            while (i < length) {
                String host = strArr[i];
                String ip = prefs.getString(host);
                if (!TextUtils.isEmpty(ip)) {
                    builder.append(host);
                    builder.append(": \n");
                    builder.append(ip);
                    builder.append("\n\n");
                }
                i++;
            }
            builder.append("================ \n");
            builder.append("current_ip: \n");
            builder.append(BlackTech.getCurrentIp());
            builder.append("\n\n");
            builder.append("isCanIpConnect: \n");
            builder.append(BlackTech.isCanIPConnect());
            builder.append("\n\n");
            builder.append("next_ip: \n");
            builder.append(DnspodFree.getNextCacheIp());
            builder.append("\n\n");
            builder.append("all_ips: \n");
            builder.append(DnspodFree.getCachedIp(BooheeClient.getHost(BooheeClient.BH_ALL)));
            this.tvConnectIPs.setText(builder.toString());
            return;
        }
        this.tvConnectIPs.setVisibility(8);
    }

    private void showMessage(View view, String message) {
        Snackbar.make(view, (CharSequence) message, 0).show();
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, ChangeEnvironmentActivity.class));
        }
    }
}
