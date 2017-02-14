package com.mob.tools.network;

import com.qiniu.android.common.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringPart extends HTTPPart {
    private StringBuilder sb = new StringBuilder();

    public StringPart append(String str) {
        this.sb.append(str);
        return this;
    }

    protected InputStream getInputStream() throws Throwable {
        return new ByteArrayInputStream(this.sb.toString().getBytes(Constants.UTF_8));
    }

    protected long length() throws Throwable {
        return (long) this.sb.toString().getBytes(Constants.UTF_8).length;
    }

    public String toString() {
        return this.sb.toString();
    }
}
