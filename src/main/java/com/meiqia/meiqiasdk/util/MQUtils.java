package com.meiqia.meiqiasdk.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.boohee.utils.Coder;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.model.FileMessage;
import com.meiqia.meiqiasdk.model.PhotoMessage;
import com.meiqia.meiqiasdk.model.TextMessage;
import com.meiqia.meiqiasdk.model.VoiceMessage;
import com.meiqia.meiqiasdk.util.MQConfig.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class MQUtils {
    public static final int KEYBOARD_CHANGE_DELAY = 300;
    private static long lastClickTime;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }

    public static void runInUIThread(Runnable task) {
        sHandler.post(task);
    }

    public static void runInUIThread(Runnable task, long delayMillis) {
        sHandler.postDelayed(task, delayMillis);
    }

    public static BaseMessage parseMQMessageIntoBaseMessage(MQMessage message, BaseMessage
            baseMessage) {
        int itemType;
        if ("client".equals(message.getFrom_type())) {
            itemType = 0;
        } else {
            itemType = 1;
        }
        baseMessage.setStatus(message.getStatus());
        baseMessage.setItemViewType(itemType);
        baseMessage.setContent(message.getContent());
        baseMessage.setContentType(message.getContent_type());
        baseMessage.setStatus(message.getStatus());
        baseMessage.setId(message.getId());
        baseMessage.setType(message.getType());
        baseMessage.setConversationId(message.getConversation_id());
        baseMessage.setAgentNickname(message.getAgent_nickname());
        baseMessage.setCreatedOn(message.getCreated_on());
        baseMessage.setAvatar(message.getAvatar());
        baseMessage.setIsRead(message.is_read());
        if ("photo".equals(message.getContent_type())) {
            ((PhotoMessage) baseMessage).setUrl(message.getMedia_url());
        } else if ("audio".equals(message.getContent_type())) {
            ((VoiceMessage) baseMessage).setUrl(message.getMedia_url());
        } else if ("file".equals(message.getContent_type())) {
            FileMessage fileMessage = (FileMessage) baseMessage;
            fileMessage.setUrl(message.getMedia_url());
            fileMessage.setExtra(message.getExtra());
            updateFileState(fileMessage);
        }
        return baseMessage;
    }

    public static BaseMessage parseMQMessageToBaseMessage(MQMessage message) {
        int itemType;
        BaseMessage baseMessage;
        if ("client".equals(message.getFrom_type())) {
            itemType = 0;
        } else {
            itemType = 1;
        }
        if ("photo".equals(message.getContent_type())) {
            baseMessage = new PhotoMessage();
            if (isLocalPath(message.getMedia_url())) {
                ((PhotoMessage) baseMessage).setLocalPath(message.getMedia_url());
            } else {
                ((PhotoMessage) baseMessage).setUrl(message.getMedia_url());
            }
            baseMessage.setContent("[photo]");
        } else if ("audio".equals(message.getContent_type())) {
            baseMessage = new VoiceMessage(message.getMedia_url());
            if (isLocalPath(message.getMedia_url())) {
                ((VoiceMessage) baseMessage).setLocalPath(message.getMedia_url());
            } else {
                ((VoiceMessage) baseMessage).setUrl(message.getMedia_url());
            }
            baseMessage.setContent("[voice]");
        } else if ("file".equals(message.getContent_type())) {
            baseMessage = new FileMessage(message.getMedia_url());
            if (isLocalPath(message.getMedia_url())) {
                ((FileMessage) baseMessage).setLocalPath(message.getMedia_url());
            } else {
                ((FileMessage) baseMessage).setUrl(message.getMedia_url());
            }
            ((FileMessage) baseMessage).setExtra(message.getExtra());
            baseMessage.setContent("[file]");
            baseMessage.setId(message.getId());
            updateFileState((FileMessage) baseMessage);
        } else {
            baseMessage = new TextMessage(message.getContent());
            baseMessage.setContent(message.getContent());
        }
        baseMessage.setConversationId(message.getConversation_id());
        baseMessage.setStatus(message.getStatus());
        baseMessage.setItemViewType(itemType);
        baseMessage.setContentType(message.getContent_type());
        baseMessage.setType(message.getType());
        baseMessage.setStatus(message.getStatus());
        baseMessage.setId(message.getId());
        baseMessage.setAgentNickname(message.getAgent_nickname());
        baseMessage.setCreatedOn(message.getCreated_on());
        baseMessage.setAvatar(message.getAvatar());
        baseMessage.setIsRead(message.is_read());
        return baseMessage;
    }

    public static MQMessage parseBaseMessageToMQMessage(BaseMessage baseMessage) {
        MQMessage message = new MQMessage(baseMessage.getContentType());
        message.setConversation_id(baseMessage.getConversationId());
        message.setStatus(baseMessage.getStatus());
        message.setContent_type(baseMessage.getContentType());
        message.setType(baseMessage.getType());
        message.setStatus(baseMessage.getStatus());
        message.setId(baseMessage.getId());
        message.setAgent_nickname(baseMessage.getAgentNickname());
        message.setCreated_on(baseMessage.getCreatedOn());
        message.setAvatar(baseMessage.getAvatar());
        if (baseMessage instanceof FileMessage) {
            message.setExtra(((FileMessage) baseMessage).getExtra());
            message.setMedia_url(((FileMessage) baseMessage).getUrl());
        }
        return message;
    }

    public static List<BaseMessage> parseMQMessageToChatBaseList(List<MQMessage> mqMessageList) {
        List<BaseMessage> baseMessages = new ArrayList();
        for (MQMessage mqMessage : mqMessageList) {
            baseMessages.add(parseMQMessageToBaseMessage(mqMessage));
        }
        return baseMessages;
    }

    public static Agent parseMQAgentToAgent(MQAgent mqAgent) {
        if (mqAgent == null) {
            return null;
        }
        Agent agent = new Agent();
        agent.setId(mqAgent.getId());
        agent.setNickname(mqAgent.getNickname());
        agent.setStatus(mqAgent.getStatus());
        agent.setIsOnline(mqAgent.isOnLine());
        return agent;
    }

    private static boolean isLocalPath(String path) {
        return (TextUtils.isEmpty(path) || path.startsWith("http")) ? false : true;
    }

    public static synchronized boolean isFastClick() {
        boolean z;
        synchronized (MQUtils.class) {
            long time = System.currentTimeMillis();
            if (time - lastClickTime < 500) {
                z = true;
            } else {
                lastClickTime = time;
                z = false;
            }
        }
        return z;
    }

    public static void applyCustomUITextAndImageColor(int resourceResId, int codeResId, ImageView
            iconIv, TextView... textViews) {
        int i = 0;
        Context context = null;
        if (iconIv != null) {
            context = iconIv.getContext();
        }
        if (textViews != null && textViews.length > 0) {
            context = textViews[0].getContext();
        }
        if (context != null) {
            if (-1 != codeResId) {
                resourceResId = codeResId;
            }
            int color = context.getResources().getColor(resourceResId);
            if (iconIv != null) {
                iconIv.setColorFilter(color);
            }
            if (textViews != null) {
                int length = textViews.length;
                while (i < length) {
                    textViews[i].setTextColor(color);
                    i++;
                }
            }
        }
    }

    public static void applyCustomUITitleGravity(TextView backTv, TextView titleTv) {
        if (MQConfig$ui$MQTitleGravity.LEFT == ui.titleGravity) {
            ((LayoutParams) titleTv.getLayoutParams()).addRule(1, R.id.back_rl);
            titleTv.setGravity(19);
            if (backTv != null) {
                backTv.setVisibility(8);
            }
        }
    }

    public static void applyCustomUITintDrawable(View view, int finalResId, int resourceResId,
                                                 int codeResId) {
        Context context = view.getContext();
        if (-1 != codeResId) {
            resourceResId = codeResId;
        }
        if (context.getResources().getColor(resourceResId) != context.getResources().getColor
                (finalResId)) {
            setBackground(view, tintDrawable(context, view.getBackground(), resourceResId));
        }
    }

    public static Drawable tintDrawable(Context context, Drawable drawable, @ColorRes int color) {
        if (drawable == null) {
            return null;
        }
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(color));
        return wrappedDrawable;
    }

    public static void setBackground(View v, Drawable bgDrawable) {
        if (VERSION.SDK_INT >= 16) {
            v.setBackground(bgDrawable);
        } else {
            v.setBackgroundDrawable(bgDrawable);
        }
    }

    public static void tintPressedIndicator(ImageView imageView, @DrawableRes int normalResId,
                                            @DrawableRes int pressedResId) {
        imageView.setImageDrawable(getPressedSelectorDrawable(imageView.getResources()
                .getDrawable(normalResId), tintDrawable(imageView.getContext(), imageView
                .getResources().getDrawable(pressedResId), R.color.mq_indicator_selected)));
    }

    public static StateListDrawable getPressedSelectorDrawable(Drawable normal, Drawable pressed) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{16842919, 16842910}, pressed);
        bg.addState(new int[]{16842910}, normal);
        bg.addState(new int[0], normal);
        return bg;
    }

    public static boolean isSdcardAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static boolean isFileExist(String filePath) {
        try {
            return new File(filePath).exists();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updateFileState(FileMessage fileMessage) {
        boolean isExist = isFileExist(getFileMessageFilePath(fileMessage));
        if (isExist) {
            fileMessage.setFileState(0);
        }
        return isExist;
    }

    public static void delFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getFileMessageFilePath(FileMessage fileMessage) {
        String path = null;
        try {
            String destFileName = new JSONObject(fileMessage.getExtra()).optString("filename");
            int lastIndexOf = destFileName.lastIndexOf(".");
            String prefix = destFileName.substring(0, lastIndexOf);
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath() + "/" + (prefix + fileMessage.getId() + destFileName
                    .substring(lastIndexOf, destFileName.length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getPicStorePath(Context ctx) {
        File file = ctx.getExternalFilesDir(null);
        if (!file.exists()) {
            file.mkdir();
        }
        File imageStoreFile = new File(file.getAbsolutePath() + "/mq");
        if (!imageStoreFile.exists()) {
            imageStoreFile.mkdir();
        }
        return imageStoreFile.getAbsolutePath();
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height",
                "dimen", DeviceInfoConstant.OS_ANDROID));
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static void show(Context context, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.length() < 10) {
                Toast.makeText(context, text, 0).show();
            } else {
                Toast.makeText(context, text, 1).show();
            }
        }
    }

    public static void show(Context context, @StringRes int resId) {
        show(context, context.getResources().getString(resId));
    }

    public static void showSafe(final Context context, final CharSequence text) {
        runInUIThread(new Runnable() {
            public void run() {
                MQUtils.show(context, text);
            }
        });
    }

    public static void showSafe(Context context, @StringRes int resId) {
        showSafe(context, context.getResources().getString(resId));
    }

    public static void clip(Context context, String text) {
        if (VERSION.SDK_INT < 11) {
            ((ClipboardManager) context.getSystemService("clipboard")).setText(text);
        } else {
            ((android.content.ClipboardManager) context.getSystemService("clipboard"))
                    .setPrimaryClip(ClipData.newPlainText("mq_content", text));
        }
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService("input_method"))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void closeKeyboard(Dialog dialog) {
        View view = dialog.getWindow().peekDecorView();
        if (view != null) {
            ((InputMethodManager) dialog.getContext().getSystemService("input_method"))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openKeyboard(final EditText editText) {
        runInUIThread(new Runnable() {
            public void run() {
                editText.requestFocus();
                editText.setSelection(editText.getText().toString().length());
                ((InputMethodManager) editText.getContext().getSystemService("input_method"))
                        .showSoftInput(editText, 2);
            }
        }, 300);
    }

    public static void scrollListViewToBottom(final AbsListView absListView) {
        if (absListView != null && absListView.getAdapter() != null && ((ListAdapter) absListView
                .getAdapter()).getCount() > 0) {
            absListView.post(new Runnable() {
                public void run() {
                    absListView.setSelection(((ListAdapter) absListView.getAdapter()).getCount()
                            - 1);
                }
            });
        }
    }

    public static String getRealPathByUri(Context context, Uri uri) {
        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }
        try {
            Cursor cursor = Media.query(context.getContentResolver(), uri, new String[]{"_data"});
            String realPath = null;
            if (cursor == null) {
                return null;
            }
            int columnIndex = cursor.getColumnIndexOrThrow("_data");
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                realPath = cursor.getString(columnIndex);
            }
            cursor.close();
            return realPath;
        } catch (Exception e) {
            return uri.getPath();
        }
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static String stringToMD5(String string) {
        try {
            byte[] hash = MessageDigest.getInstance(Coder.KEY_MD5).digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 255) < 16) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 255));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static File getImageDir(Context context) {
        File imageDir = null;
        if (isExternalStorageWritable()) {
            imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment
                    .DIRECTORY_PICTURES), "MeiqiaSDK" + File.separator + context
                    .getApplicationInfo().loadLabel(context.getPackageManager()).toString());
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
        } else {
            showSafe(context, R.string.mq_no_sdcard);
        }
        return imageDir;
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }
}
