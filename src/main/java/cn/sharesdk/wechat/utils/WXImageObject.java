package cn.sharesdk.wechat.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject;
import com.mob.tools.utils.Ln;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

public class WXImageObject implements IMediaObject {
    public byte[] imageData;
    public String imagePath;
    public String imageUrl;

    public WXImageObject(Bitmap bitmap) {
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 85, byteArrayOutputStream);
            this.imageData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (Throwable e) {
            Ln.e(e);
        }
    }

    public WXImageObject(byte[] bArr) {
        this.imageData = bArr;
    }

    public boolean checkArgs() {
        if ((this.imageData == null || this.imageData.length == 0) && ((this.imagePath == null || this.imagePath.length() == 0) && (this.imageUrl == null || this.imageUrl.length() == 0))) {
            Ln.e("checkArgs fail, all arguments are null", new Object[0]);
            return false;
        } else if (this.imageData != null && this.imageData.length > 10485760) {
            Ln.e("checkArgs fail, content is too large", new Object[0]);
            return false;
        } else if (this.imagePath != null && this.imagePath.length() > 10240) {
            Ln.e("checkArgs fail, path is invalid", new Object[0]);
            return false;
        } else if (this.imagePath != null && new File(this.imagePath).length() > 10485760) {
            Ln.e("checkArgs fail, image content is too large", new Object[0]);
            return false;
        } else if (this.imageUrl == null || this.imageUrl.length() <= 10240) {
            return true;
        } else {
            Ln.e("checkArgs fail, url is invalid", new Object[0]);
            return false;
        }
    }

    public void serialize(Bundle bundle) {
        bundle.putByteArray("_wximageobject_imageData", this.imageData);
        bundle.putString("_wximageobject_imagePath", this.imagePath);
        bundle.putString("_wximageobject_imageUrl", this.imageUrl);
    }

    public int type() {
        return 2;
    }

    public void unserialize(Bundle bundle) {
        this.imageData = bundle.getByteArray("_wximageobject_imageData");
        this.imagePath = bundle.getString("_wximageobject_imagePath");
        this.imageUrl = bundle.getString("_wximageobject_imageUrl");
    }
}
