package com.boohee.one.bet;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

import com.boohee.utility.BooheeScheme;
import com.boohee.widgets.LightAlertDialog;
import com.umeng.socialize.common.SocializeConstants;

public class BetUtil {
    public static void showBetDialog(final Context context) {
        try {
            String text = "";
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService("clipboard");
            if (clipboard.hasPrimaryClip()) {
                ClipDescription description = clipboard.getPrimaryClipDescription();
                ClipData data = clipboard.getPrimaryClip();
                if (!(data == null || description == null || !description.hasMimeType
                        ("text/plain"))) {
                    text = String.valueOf(data.getItemAt(0).getText());
                }
            }
            if (!TextUtils.isEmpty(text)) {
                text = text.trim();
                if (text.contains("我赌我会瘦")) {
                    int betId = 0;
                    if (text.contains(BetActivity.BET_ROOT_URL)) {
                        betId = Integer.parseInt(text.substring(text.lastIndexOf("/") + 1));
                    }
                    if (betId <= 0) {
                        betId = Integer.parseInt(text.substring(text.indexOf(SocializeConstants
                                .OP_OPEN_PAREN) + 1, text.indexOf(SocializeConstants
                                .OP_CLOSE_PAREN)));
                    }
                    if (betId > 0) {
                        final LightAlertDialog dialog = LightAlertDialog.create(context,
                                "是否打开我赌我会瘦活动页面");
                        dialog.setNegativeButton((CharSequence) "取消", new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        });
                        final int finalBetId = betId;
                        dialog.setPositiveButton((CharSequence) "立即打开", new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BooheeScheme.handleUrl(context, "boohee://bets/" + finalBetId);
                            }
                        });
                        dialog.show();
                        clipboard.setPrimaryClip(ClipData.newPlainText("", ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
