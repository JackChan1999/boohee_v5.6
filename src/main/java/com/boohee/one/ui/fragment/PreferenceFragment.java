package com.boohee.one.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.TextUtils;

import boohee.lib.share.ShareManager;

import com.boohee.api.OneApi;
import com.boohee.database.OnePreference;
import com.boohee.more.DescriptionActivity;
import com.boohee.more.PasscodeActivity;
import com.boohee.one.R;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.push.PushManager;
import com.boohee.utility.Const;
import com.boohee.utils.Helper;

public class PreferenceFragment extends android.preference.PreferenceFragment implements
        OnPreferenceClickListener, OnPreferenceChangeListener {
    private Preference         mAboutBooheePreference;
    private CheckBoxPreference mIsReceivePushPreference;
    private CheckBoxPreference mPrivacyPreference;
    private Preference         mScorePreference;
    private Preference         mSharePreference;
    private Preference         mTermsPreference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.a);
        try {
            findPreference("pref_about_boohee").setSummary(getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 16384).versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        this.mSharePreference = findPreference("pref_share");
        this.mSharePreference.setOnPreferenceClickListener(this);
        this.mTermsPreference = findPreference("pref_terms");
        this.mTermsPreference.setOnPreferenceClickListener(this);
        this.mAboutBooheePreference = findPreference("pref_about_boohee");
        this.mAboutBooheePreference.setOnPreferenceClickListener(this);
        this.mScorePreference = findPreference("pref_score");
        this.mScorePreference.setOnPreferenceClickListener(this);
        this.mPrivacyPreference = (CheckBoxPreference) findPreference(Const.PASSWORD);
        this.mPrivacyPreference.setOnPreferenceChangeListener(this);
        this.mIsReceivePushPreference = (CheckBoxPreference) findPreference("isReceivePush");
        this.mIsReceivePushPreference.setOnPreferenceChangeListener(this);
    }

    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(new OnePreference(getActivity()).getString(Const.PASSWORD))) {
            this.mPrivacyPreference.setChecked(false);
        } else {
            this.mPrivacyPreference.setChecked(true);
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference == this.mSharePreference) {
            share();
        } else if (preference == this.mTermsPreference) {
            Intent articleIntent = new Intent(getActivity(), BrowserActivity.class);
            articleIntent.putExtra("url", BooheeClient.build(BooheeClient.ONE).getDefaultURL
                    ("/api/v1/articles/partner_rules.html"));
            articleIntent.putExtra("title", getString(R.string.abp));
            startActivity(articleIntent);
        } else if (preference == this.mAboutBooheePreference) {
            Intent aboutIntent = new Intent(getActivity(), DescriptionActivity.class);
            aboutIntent.putExtra("index", 1);
            startActivity(aboutIntent);
        } else if (preference == this.mScorePreference) {
            scoreUs();
        }
        return false;
    }

    private void share() {
        ShareManager.share(getActivity(), "最有效的减肥APP",
                "推荐“薄荷”app给大家哦，简直专业到令人感动！它会根据你的身高体重建议你一天该摄取的卡路里是多少，还有很全的食物卡路里数据，知道食物热量就不担心吃错东西长肉啦！传送门>>>", "http://a.app.qq.com/o/simple.jsp?pkgname=com.boohee.one&g_f=991653", "http://up.boohee.cn/house/u/one/ad/boohee_weibo.png");
    }

    private void scoreUs() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse
                    ("market://details?id=" + getActivity().getPackageName())));
            OneApi.getUserBehaviorAppraise(getActivity(), null);
        } catch (ActivityNotFoundException e) {
            Helper.showToast((int) R.string.xt);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == this.mPrivacyPreference) {
            Intent intent;
            if (String.valueOf(newValue).equals("true")) {
                intent = new Intent(getActivity(), PasscodeActivity.class);
                intent.setAction(PasscodeActivity.ACTION_PASSWORD_OPEN);
                startActivity(intent);
            } else {
                intent = new Intent(getActivity(), PasscodeActivity.class);
                intent.setAction(PasscodeActivity.ACTION_PASSWORD_CLOSE);
                startActivity(intent);
            }
        } else if (preference == this.mIsReceivePushPreference) {
            if (String.valueOf(newValue).equals("true")) {
                PushManager.getInstance().resumePush();
            } else {
                PushManager.getInstance().pausePush();
            }
        }
        return true;
    }
}
