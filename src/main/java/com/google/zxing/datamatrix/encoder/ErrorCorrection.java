package com.google.zxing.datamatrix.encoder;

import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.tencent.tinker.android.dx.instruction.Opcodes;
import com.umeng.socialize.common.SocializeConstants;

public final class ErrorCorrection {
    private static final int[]   ALOG         = new int[255];
    private static final int[][] FACTORS      = new int[][]{new int[]{228, 48, 15, 111, 62}, new
            int[]{23, 68, 144, Opcodes.LONG_TO_DOUBLE, SocializeConstants
            .MASK_USER_CENTER_HIDE_AREA, 92, 254}, new int[]{28, 24, 185, 166, Opcodes
            .XOR_INT_LIT8, 248, Opcodes.INVOKE_VIRTUAL_RANGE, 255, 110, 61}, new int[]{175, 138,
            205, 12, Opcodes.XOR_LONG_2ADDR, 168, 39, 245, 60, 97, 120}, new int[]{41, 153, 158,
            91, 61, 42, 142, Opcodes.AND_INT_LIT16, 97, 178, 100, 242}, new int[]{156, 97, 192,
            SampleTinkerReport.KEY_LOADED_EXCEPTION_DEX, 95, 9, 157, Opcodes.INVOKE_STATIC_RANGE,
            138, 45, 18, Opcodes.USHR_INT_2ADDR, 83, 185}, new int[]{83, 195, 100, 39, Opcodes
            .SUB_LONG_2ADDR, 75, 66, 61, 241, Opcodes.AND_INT_LIT16, 109, Opcodes.INT_TO_LONG,
            94, 254, Opcodes.SHR_INT_LIT8, 48, 90, Opcodes.SUB_LONG_2ADDR}, new int[]{15, 195,
            244, 9, 233, 71, 168, 2, Opcodes.SUB_LONG_2ADDR, 160, 153, 145, SampleTinkerReport
            .KEY_LOADED_EXCEPTION_DEX_CHECK, 79, 108, 82, 27, Opcodes.DIV_DOUBLE, Opcodes
            .USHR_INT_2ADDR, 172}, new int[]{52, Opcodes.DIV_LONG_2ADDR, 88, 205, 109, 39, 176,
            21, 155, Opcodes.USHR_LONG_2ADDR, SampleTinkerReport.KEY_LOADED_UNCAUGHT_EXCEPTION,
            Opcodes.XOR_INT_LIT8, 155, 21, 5, 172, 254, Opcodes.NOT_INT, 12, 181, 184, 96, 50,
            193}, new int[]{Opcodes.DIV_INT_LIT16, 231, 43, 97, 71, 96, 103, Opcodes.DIV_DOUBLE,
            37, 151, Opcodes.REM_FLOAT, 53, 75, 34, 249, SampleTinkerReport.KEY_APPLIED_DEXOPT,
            17, 138, 110, Opcodes.AND_INT_LIT16, 141, Opcodes.FLOAT_TO_LONG, 120, 151, 233, 168,
            93, 255}, new int[]{245, 127, 242, Opcodes.MUL_INT_LIT8, 130, 250, 162, 181, 102,
            120, 84, 179, Opcodes.REM_INT_LIT8, SampleTinkerReport.KEY_LOADED_UNCAUGHT_EXCEPTION,
            80, 182, 229, 18, 2, 4, 68, 33, 101, 137, 95, Opcodes.INVOKE_STATIC_RANGE, 115, 44,
            175, 184, 59, 25, Opcodes.SHR_INT_LIT8, 98, 81, 112}, new int[]{77, 193, 137, 31, 19,
            38, 22, 153, 247, 105, 122, 2, 245, Opcodes.LONG_TO_FLOAT, 242, 8, 175, 95, 100, 9,
            167, 105, Opcodes.OR_INT_LIT16, 111, 57, SampleTinkerReport.KEY_APPLIED_DEXOPT, 21,
            1, SampleTinkerReport.KEY_LOADED_EXCEPTION_DEX_CHECK, 57, 54, 101, 248, 202, 69, 50,
            150, 177, Opcodes.USHR_INT_LIT8, 5, 9, 5}, new int[]{245, 132, 172, Opcodes
            .XOR_INT_LIT8, 96, 32, Opcodes.INVOKE_SUPER_RANGE, 22, 238, Opcodes.LONG_TO_FLOAT,
            238, 231, 205, Opcodes.SUB_LONG_2ADDR, 237, 87, Opcodes.REM_LONG_2ADDR, 106, 16, 147,
            Opcodes.INVOKE_DIRECT_RANGE, 23, 37, 90, Opcodes.REM_FLOAT, 205, Opcodes
            .INT_TO_DOUBLE, 88, 120, 100, 66, 138, Opcodes.USHR_INT_2ADDR, SocializeConstants
            .MASK_USER_CENTER_HIDE_AREA, 82, 44, 176, 87, 187, 147, 160, 175, 69, Opcodes
            .AND_INT_LIT16, 92, SampleTinkerReport.KEY_LOADED_EXCEPTION_DEX_CHECK, Opcodes
            .SHR_INT_LIT8, 19}, new int[]{175, 9, Opcodes.XOR_INT_LIT8, 238, 12, 17, Opcodes
            .REM_INT_LIT8, 208, 100, 29, 175, Opcodes.REM_FLOAT, 230, 192, Opcodes.XOR_INT_LIT16,
            235, 150, 159, 36, Opcodes.XOR_INT_LIT8, 38, 200, 132, 54, 228, 146, Opcodes
            .MUL_INT_LIT8, 234, Opcodes.INVOKE_SUPER_RANGE, 203, 29, 232, 144, 238, 22, 150, 201,
            Opcodes.INVOKE_SUPER_RANGE, 62, 207, 164, 13, 137, 245, 127, 67, 247, 28, 155, 43,
            203, 107, 233, 53, Opcodes.INT_TO_SHORT, 46}, new int[]{242, 93, 169, 50, 144,
            Opcodes.MUL_INT_LIT16, 39, Opcodes.INVOKE_DIRECT_RANGE, 202, Opcodes.SUB_LONG_2ADDR,
            201, Opcodes.MUL_LONG_2ADDR, Opcodes.INT_TO_SHORT, 108, Opcodes.SHR_LONG_2ADDR, 37,
            185, 112, Opcodes.LONG_TO_DOUBLE, 230, 245, 63, Opcodes.USHR_LONG_2ADDR, Opcodes
            .DIV_LONG_2ADDR, 250, 106, 185, Opcodes.AND_INT_LIT8, 175, 64, Opcodes
            .INVOKE_INTERFACE, 71, 161, 44, 147, 6, 27, Opcodes.MUL_INT_LIT8, 51, 63, 87, 10, 40,
            130, Opcodes.SUB_LONG_2ADDR, 17, 163, 31, 176, Opcodes.REM_FLOAT, 4, 107, 232, 7, 94,
            166, Opcodes.SHL_INT_LIT8, Opcodes.NOT_INT, 86, 47, 11, 204}, new int[]{Opcodes
            .REM_INT_LIT8, 228, Opcodes.MUL_DOUBLE, 89, SampleTinkerReport
            .KEY_LOADED_UNCAUGHT_EXCEPTION, 149, 159, 56, 89, 33, 147, 244, 154, 36, 73, 127,
            Opcodes.AND_INT_LIT16, Opcodes.FLOAT_TO_LONG, 248, 180, 234, Opcodes.USHR_LONG_2ADDR,
            158, 177, 68, 122, 93, Opcodes.AND_INT_LIT16, 15, 160, 227, 236, 66, 139, 153, 185,
            202, 167, 179, 25, Opcodes.REM_INT_LIT8, 232, 96, Opcodes.MUL_INT_LIT16, 231, Opcodes
            .FLOAT_TO_LONG, Opcodes.XOR_INT_LIT8, 239, 181, 241, 59, 52, 172, 25, 49, 232,
            Opcodes.DIV_INT_LIT16, Opcodes.MUL_LONG_2ADDR, 64, 54, 108, 153, 132, 63, 96, 103,
            82, Opcodes.USHR_INT_2ADDR}};
    private static final int[]   FACTOR_SETS  = new int[]{5, 7, 10, 11, 12, 14, 18, 20, 24, 28,
            36, 42, 48, 56, 62, 68};
    private static final int[]   LOG          = new int[256];
    private static final int     MODULO_VALUE = 301;

    static {
        int p = 1;
        for (int i = 0; i < 255; i++) {
            ALOG[i] = p;
            LOG[p] = i;
            p *= 2;
            if (p >= 256) {
                p ^= 301;
            }
        }
    }

    private ErrorCorrection() {
    }

    public static String encodeECC200(String codewords, SymbolInfo symbolInfo) {
        if (codewords.length() != symbolInfo.getDataCapacity()) {
            throw new IllegalArgumentException("The number of codewords does not match the " +
                    "selected symbol");
        }
        StringBuilder sb = new StringBuilder(symbolInfo.getDataCapacity() + symbolInfo
                .getErrorCodewords());
        sb.append(codewords);
        int blockCount = symbolInfo.getInterleavedBlockCount();
        if (blockCount == 1) {
            sb.append(createECCBlock(codewords, symbolInfo.getErrorCodewords()));
        } else {
            sb.setLength(sb.capacity());
            int[] dataSizes = new int[blockCount];
            int[] errorSizes = new int[blockCount];
            int[] startPos = new int[blockCount];
            for (int i = 0; i < blockCount; i++) {
                dataSizes[i] = symbolInfo.getDataLengthForInterleavedBlock(i + 1);
                errorSizes[i] = symbolInfo.getErrorLengthForInterleavedBlock(i + 1);
                startPos[i] = 0;
                if (i > 0) {
                    startPos[i] = startPos[i - 1] + dataSizes[i];
                }
            }
            for (int block = 0; block < blockCount; block++) {
                StringBuilder temp = new StringBuilder(dataSizes[block]);
                for (int d = block; d < symbolInfo.getDataCapacity(); d += blockCount) {
                    temp.append(codewords.charAt(d));
                }
                String ecc = createECCBlock(temp.toString(), errorSizes[block]);
                int pos = 0;
                int e = block;
                while (e < errorSizes[block] * blockCount) {
                    int pos2 = pos + 1;
                    sb.setCharAt(symbolInfo.getDataCapacity() + e, ecc.charAt(pos));
                    e += blockCount;
                    pos = pos2;
                }
            }
        }
        return sb.toString();
    }

    private static String createECCBlock(CharSequence codewords, int numECWords) {
        return createECCBlock(codewords, 0, codewords.length(), numECWords);
    }

    private static String createECCBlock(CharSequence codewords, int start, int len, int
            numECWords) {
        int i;
        int table = -1;
        for (i = 0; i < FACTOR_SETS.length; i++) {
            if (FACTOR_SETS[i] == numECWords) {
                table = i;
                break;
            }
        }
        if (table < 0) {
            throw new IllegalArgumentException("Illegal number of error correction codewords " +
                    "specified: " + numECWords);
        }
        int[] poly = FACTORS[table];
        char[] ecc = new char[numECWords];
        for (i = 0; i < numECWords; i++) {
            ecc[i] = '\u0000';
        }
        for (i = start; i < start + len; i++) {
            int m = ecc[numECWords - 1] ^ codewords.charAt(i);
            int k = numECWords - 1;
            while (k > 0) {
                if (m == 0 || poly[k] == 0) {
                    ecc[k] = ecc[k - 1];
                } else {
                    ecc[k] = (char) (ecc[k - 1] ^ ALOG[(LOG[m] + LOG[poly[k]]) % 255]);
                }
                k--;
            }
            if (m == 0 || poly[0] == 0) {
                ecc[0] = '\u0000';
            } else {
                ecc[0] = (char) ALOG[(LOG[m] + LOG[poly[0]]) % 255];
            }
        }
        char[] eccReversed = new char[numECWords];
        for (i = 0; i < numECWords; i++) {
            eccReversed[i] = ecc[(numECWords - i) - 1];
        }
        return String.valueOf(eccReversed);
    }
}
