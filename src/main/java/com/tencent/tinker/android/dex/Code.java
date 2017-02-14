package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public final class Code extends Item<Code> {
    public CatchHandler[] catchHandlers;
    public int            debugInfoOffset;
    public int            insSize;
    public short[]        instructions;
    public int            outsSize;
    public int            registersSize;
    public Try[]          tries;

    public static class CatchHandler implements Comparable<CatchHandler> {
        public int[] addresses;
        public int   catchAllAddress;
        public int   offset;
        public int[] typeIndexes;

        public CatchHandler(int[] typeIndexes, int[] addresses, int catchAllAddress, int offset) {
            this.typeIndexes = typeIndexes;
            this.addresses = addresses;
            this.catchAllAddress = catchAllAddress;
            this.offset = offset;
        }

        public int compareTo(CatchHandler other) {
            int res = CompareUtils.sArrCompare(this.typeIndexes, other.typeIndexes);
            if (res != 0) {
                return res;
            }
            res = CompareUtils.sArrCompare(this.addresses, other.addresses);
            if (res != 0) {
                return res;
            }
            return CompareUtils.sCompare(this.catchAllAddress, other.catchAllAddress);
        }
    }

    public static class Try implements Comparable<Try> {
        public int catchHandlerIndex;
        public int instructionCount;
        public int startAddress;

        public Try(int startAddress, int instructionCount, int catchHandlerIndex) {
            this.startAddress = startAddress;
            this.instructionCount = instructionCount;
            this.catchHandlerIndex = catchHandlerIndex;
        }

        public int compareTo(Try other) {
            int res = CompareUtils.sCompare(this.startAddress, other.startAddress);
            if (res != 0) {
                return res;
            }
            res = CompareUtils.sCompare(this.instructionCount, other.instructionCount);
            if (res != 0) {
                return res;
            }
            return CompareUtils.sCompare(this.catchHandlerIndex, other.catchHandlerIndex);
        }
    }

    public Code(int off, int registersSize, int insSize, int outsSize, int debugInfoOffset,
                short[] instructions, Try[] tries, CatchHandler[] catchHandlers) {
        super(off);
        this.registersSize = registersSize;
        this.insSize = insSize;
        this.outsSize = outsSize;
        this.debugInfoOffset = debugInfoOffset;
        this.instructions = instructions;
        this.tries = tries;
        this.catchHandlers = catchHandlers;
    }

    public int compareTo(Code other) {
        int res = CompareUtils.sCompare(this.registersSize, other.registersSize);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.sCompare(this.insSize, other.insSize);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.sCompare(this.outsSize, other.outsSize);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.sCompare(this.debugInfoOffset, other.debugInfoOffset);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.uArrCompare(this.instructions, other.instructions);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.aArrCompare(this.tries, other.tries);
        if (res != 0) {
            return res;
        }
        return CompareUtils.aArrCompare(this.catchHandlers, other.catchHandlers);
    }

    public int byteCountInDex() {
        int insnsSize = this.instructions.length;
        int res = (insnsSize * 2) + 16;
        if (this.tries.length > 0) {
            if ((insnsSize & 1) == 1) {
                res += 2;
            }
            res = (res + (this.tries.length * 8)) + Leb128.unsignedLeb128Size(this.catchHandlers
                    .length);
            for (CatchHandler catchHandler : this.catchHandlers) {
                int typeIdxAddrPairCount = catchHandler.typeIndexes.length;
                if (catchHandler.catchAllAddress != -1) {
                    res += Leb128.signedLeb128Size(-typeIdxAddrPairCount) + Leb128
                            .unsignedLeb128Size(catchHandler.catchAllAddress);
                } else {
                    res += Leb128.signedLeb128Size(typeIdxAddrPairCount);
                }
                for (int i = 0; i < typeIdxAddrPairCount; i++) {
                    res += Leb128.unsignedLeb128Size(catchHandler.typeIndexes[i]) + Leb128
                            .unsignedLeb128Size(catchHandler.addresses[i]);
                }
            }
        }
        return res;
    }
}
