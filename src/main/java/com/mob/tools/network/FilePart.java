package com.mob.tools.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FilePart extends HTTPPart {
    private File file;

    protected InputStream getInputStream() throws Throwable {
        return new FileInputStream(this.file);
    }

    protected long length() throws Throwable {
        return this.file.length();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFile(String str) {
        this.file = new File(str);
    }

    public String toString() {
        return this.file.toString();
    }
}
