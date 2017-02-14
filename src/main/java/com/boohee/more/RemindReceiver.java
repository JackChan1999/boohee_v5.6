package com.boohee.more;

import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boohee.database.OnePreference;
import com.boohee.model.AlarmTip;
import com.boohee.modeldao.AlarmTipDao;
import com.boohee.modeldao.NoticeDao;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.utility.Const;
import com.boohee.utils.BadgeUtils;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.common.SocializeConstants;

public class RemindReceiver extends BroadcastReceiver {
    static final String TAG = RemindReceiver.class.getName();
    private AlarmTip alarmTip;
    private Context  ctx;
    private String   noticeCode;
    private int      noticeId;
    private int      noticeType;

    public void onReceive(Context context, Intent intent) {
        this.ctx = context;
        getAlarmMsg(intent);
        setNotification(this.alarmTip, getIntent());
        BadgeUtils.setIconBadge(context, 1);
    }

    private void getAlarmMsg(Intent intent) {
        MobclickAgent.onEvent(this.ctx, "other_receiveRemind");
        this.noticeCode = intent.getStringExtra(Const.NOTICE_CODE);
        this.noticeType = intent.getIntExtra(Const.NOTICE_TYPE, 2);
        AlarmTipDao tipDao = new AlarmTipDao(this.ctx);
        this.alarmTip = tipDao.getRandomTip();
        tipDao.closeDB();
        if (this.alarmTip != null) {
            addNotice(this.alarmTip);
        }
    }

    private void addNotice(AlarmTip alarmTip) {
        NoticeDao noticeDao = new NoticeDao(this.ctx);
        noticeDao.add(alarmTip);
        noticeDao.closeDB();
        this.noticeId = alarmTip.id;
    }

    public void setNotification(AlarmTip alarmTip, Intent intent) {
        Helper.showLog(TAG, "发送Notification " + alarmTip.id + SocializeConstants.OP_DIVIDER_MINUS
                + alarmTip.code + SocializeConstants.OP_DIVIDER_MINUS + alarmTip.name +
                SocializeConstants.OP_DIVIDER_MINUS + alarmTip.message);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.ctx, 0, intent, 268435456);
        NotificationManager nfcManager = (NotificationManager) this.ctx.getSystemService
                ("notification");
        String title = this.ctx.getString(R.string.dl);
        Builder builder = new Builder(this.ctx);
        builder.setAutoCancel(false);
        builder.setTicker(title);
        builder.setContentTitle(title);
        builder.setContentText(alarmTip.message);
        builder.setSmallIcon(R.drawable.ja);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        nfcManager.notify(this.noticeType, builder.getNotification());
    }

    private Intent getIntent() {
        Intent intent;
        if (!hasPwd() || MyApplication.getIsMainOpened()) {
            intent = new Intent(this.ctx, ViewTipActivity.class);
        } else {
            intent = new Intent(this.ctx, PasscodeActivity.class);
            intent.setAction(PasscodeActivity.ACTION_PASSWORD_INPUT);
        }
        intent.putExtra(Const.NOTICE_ID, this.noticeId);
        intent.putExtra(ViewTipActivity.FROM_NOTIFICATION, true);
        intent.putExtra(Const.NOTICE_MESSAGE, this.alarmTip.message);
        intent.setFlags(335544320);
        return intent;
    }

    private boolean hasPwd() {
        if (new OnePreference(this.ctx).getString(Const.PASSWORD) != null) {
            return true;
        }
        return false;
    }
}
