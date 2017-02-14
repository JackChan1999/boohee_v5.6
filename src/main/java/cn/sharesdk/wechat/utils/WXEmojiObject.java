package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject;
import com.mob.tools.utils.Ln;
import java.io.File;

public class WXEmojiObject implements IMediaObject {
    public byte[] emojiData;
    public String emojiPath;

    public WXEmojiObject(String str) {
        this.emojiPath = str;
    }

    public WXEmojiObject(byte[] bArr) {
        this.emojiData = bArr;
    }

    public boolean checkArgs() {
        if ((this.emojiData == null || this.emojiData.length == 0) && TextUtils.isEmpty(this.emojiPath)) {
            Ln.e("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, both arguments is null");
            return false;
        } else if (this.emojiData == null || this.emojiData.length <= 10485760) {
            if (this.emojiPath != null) {
                File file = new File(this.emojiPath);
                if (!file.exists()) {
                    Ln.e("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, emojiPath not found");
                    return false;
                } else if (file.length() > 10485760) {
                    Ln.e("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, emojiSize is too large");
                    return false;
                }
            }
            return true;
        } else {
            Ln.e("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, emojiData is too large");
            return false;
        }
    }

    public void serialize(Bundle bundle) {
        bundle.putByteArray("_wxemojiobject_emojiData", this.emojiData);
        bundle.putString("_wxemojiobject_emojiPath", this.emojiPath);
    }

    public void setEmojiData(byte[] bArr) {
        this.emojiData = bArr;
    }

    public void setEmojiPath(String str) {
        this.emojiPath = str;
    }

    public int type() {
        return 8;
    }

    public void unserialize(Bundle bundle) {
        this.emojiData = bundle.getByteArray("_wxemojiobject_emojiData");
        this.emojiPath = bundle.getString("_wxemojiobject_emojiPath");
    }
}
