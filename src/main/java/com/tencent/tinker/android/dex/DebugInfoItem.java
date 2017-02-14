package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public class DebugInfoItem extends Item<DebugInfoItem> {
    public static final byte DBG_ADVANCE_LINE         = (byte) 2;
    public static final byte DBG_ADVANCE_PC           = (byte) 1;
    public static final byte DBG_END_LOCAL            = (byte) 5;
    public static final byte DBG_END_SEQUENCE         = (byte) 0;
    public static final byte DBG_RESTART_LOCAL        = (byte) 6;
    public static final byte DBG_SET_EPILOGUE_BEGIN   = (byte) 8;
    public static final byte DBG_SET_FILE             = (byte) 9;
    public static final byte DBG_SET_PROLOGUE_END     = (byte) 7;
    public static final byte DBG_START_LOCAL          = (byte) 3;
    public static final byte DBG_START_LOCAL_EXTENDED = (byte) 4;
    public byte[] infoSTM;
    public int    lineStart;
    public int[]  parameterNames;

    public DebugInfoItem(int off, int lineStart, int[] parameterNames, byte[] infoSTM) {
        super(off);
        this.lineStart = lineStart;
        this.parameterNames = parameterNames;
        this.infoSTM = infoSTM;
    }

    public int compareTo(DebugInfoItem o) {
        int origLineStart = this.lineStart;
        int destLineStart = o.lineStart;
        if (origLineStart != destLineStart) {
            return origLineStart - destLineStart;
        }
        int cmpRes = CompareUtils.uArrCompare(this.parameterNames, o.parameterNames);
        if (cmpRes == 0) {
            return CompareUtils.uArrCompare(this.infoSTM, o.infoSTM);
        }
        return cmpRes;
    }

    public int byteCountInDex() {
        int byteCount = Leb128.unsignedLeb128Size(this.lineStart) + Leb128.unsignedLeb128Size
                (this.parameterNames.length);
        for (int pn : this.parameterNames) {
            byteCount += Leb128.unsignedLeb128p1Size(pn);
        }
        return byteCount + (this.infoSTM.length * 1);
    }
}
