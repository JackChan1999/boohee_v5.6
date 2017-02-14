package com.tencent.wxop.stat;

public final class f {
    private String  a  = null;
    private String  b  = null;
    private boolean ba = false;
    private boolean bb = false;
    private String  c  = null;

    public final boolean R() {
        return this.ba;
    }

    public final String S() {
        return this.a;
    }

    public final String T() {
        return this.b;
    }

    public final boolean U() {
        return this.bb;
    }

    public final String getVersion() {
        return this.c;
    }

    public final void s(String str) {
        this.a = str;
    }

    public final String toString() {
        return "StatSpecifyReportedInfo [appKey=" + this.a + ", installChannel=" + this.b + ", " +
                "version=" + this.c + ", sendImmediately=" + this.ba + ", isImportant=" + this.bb
                + "]";
    }
}
