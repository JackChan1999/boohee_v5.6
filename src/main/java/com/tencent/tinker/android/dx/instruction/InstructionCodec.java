package com.tencent.tinker.android.dx.instruction;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;

import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dx.util.Hex;

public final class InstructionCodec {
    public static final int INDEX_TYPE_FIELD_REF                = 5;
    public static final int INDEX_TYPE_METHOD_REF               = 4;
    public static final int INDEX_TYPE_NONE                     = 1;
    public static final int INDEX_TYPE_STRING_REF               = 3;
    public static final int INDEX_TYPE_TYPE_REF                 = 2;
    public static final int INDEX_TYPE_UNKNOWN                  = 0;
    public static final int INSN_FORMAT_00X                     = 1;
    public static final int INSN_FORMAT_10T                     = 2;
    public static final int INSN_FORMAT_10X                     = 3;
    public static final int INSN_FORMAT_11N                     = 4;
    public static final int INSN_FORMAT_11X                     = 5;
    public static final int INSN_FORMAT_12X                     = 6;
    public static final int INSN_FORMAT_20T                     = 7;
    public static final int INSN_FORMAT_21C                     = 8;
    public static final int INSN_FORMAT_21H                     = 9;
    public static final int INSN_FORMAT_21S                     = 10;
    public static final int INSN_FORMAT_21T                     = 11;
    public static final int INSN_FORMAT_22B                     = 12;
    public static final int INSN_FORMAT_22C                     = 13;
    public static final int INSN_FORMAT_22S                     = 14;
    public static final int INSN_FORMAT_22T                     = 15;
    public static final int INSN_FORMAT_22X                     = 16;
    public static final int INSN_FORMAT_23X                     = 17;
    public static final int INSN_FORMAT_30T                     = 18;
    public static final int INSN_FORMAT_31C                     = 19;
    public static final int INSN_FORMAT_31I                     = 20;
    public static final int INSN_FORMAT_31T                     = 21;
    public static final int INSN_FORMAT_32X                     = 22;
    public static final int INSN_FORMAT_35C                     = 23;
    public static final int INSN_FORMAT_3RC                     = 24;
    public static final int INSN_FORMAT_51L                     = 25;
    public static final int INSN_FORMAT_FILL_ARRAY_DATA_PAYLOAD = 26;
    public static final int INSN_FORMAT_PACKED_SWITCH_PAYLOAD   = 27;
    public static final int INSN_FORMAT_SPARSE_SWITCH_PAYLOAD   = 28;
    public static final int INSN_FORMAT_UNKNOWN                 = 0;

    private InstructionCodec() {
        throw new UnsupportedOperationException();
    }

    public static short codeUnit(int lowByte, int highByte) {
        if ((lowByte & InputDeviceCompat.SOURCE_ANY) != 0) {
            throw new IllegalArgumentException("bogus lowByte");
        } else if ((highByte & InputDeviceCompat.SOURCE_ANY) == 0) {
            return (short) ((highByte << 8) | lowByte);
        } else {
            throw new IllegalArgumentException("bogus highByte");
        }
    }

    public static short codeUnit(int nibble0, int nibble1, int nibble2, int nibble3) {
        if ((nibble0 & -16) != 0) {
            throw new IllegalArgumentException("bogus nibble0");
        } else if ((nibble1 & -16) != 0) {
            throw new IllegalArgumentException("bogus nibble1");
        } else if ((nibble2 & -16) != 0) {
            throw new IllegalArgumentException("bogus nibble2");
        } else if ((nibble3 & -16) == 0) {
            return (short) ((((nibble1 << 4) | nibble0) | (nibble2 << 8)) | (nibble3 << 12));
        } else {
            throw new IllegalArgumentException("bogus nibble3");
        }
    }

    public static int makeByte(int lowNibble, int highNibble) {
        if ((lowNibble & -16) != 0) {
            throw new IllegalArgumentException("bogus lowNibble");
        } else if ((highNibble & -16) == 0) {
            return (highNibble << 4) | lowNibble;
        } else {
            throw new IllegalArgumentException("bogus highNibble");
        }
    }

    public static short asUnsignedUnit(int value) {
        if ((SupportMenu.CATEGORY_MASK & value) == 0) {
            return (short) value;
        }
        throw new IllegalArgumentException("bogus unsigned code unit");
    }

    public static short unit0(int value) {
        return (short) value;
    }

    public static short unit1(int value) {
        return (short) (value >> 16);
    }

    public static short unit0(long value) {
        return (short) ((int) value);
    }

    public static short unit1(long value) {
        return (short) ((int) (value >> 16));
    }

    public static short unit2(long value) {
        return (short) ((int) (value >> 32));
    }

    public static short unit3(long value) {
        return (short) ((int) (value >> 48));
    }

    public static int byte0(int value) {
        return value & 255;
    }

    public static int byte1(int value) {
        return (value >> 8) & 255;
    }

    public static int nibble0(int value) {
        return value & 15;
    }

    public static int nibble1(int value) {
        return (value >> 4) & 15;
    }

    public static int nibble2(int value) {
        return (value >> 8) & 15;
    }

    public static int nibble3(int value) {
        return (value >> 12) & 15;
    }

    public static int getTargetByte(int target, int baseAddress) {
        byte relativeTarget = getTarget(target, baseAddress);
        if (relativeTarget == ((byte) relativeTarget)) {
            return relativeTarget & 255;
        }
        throw new DexException("Target out of range: " + Hex.s4(relativeTarget) + ", perhaps you " +
                "need to enable force jumbo mode.");
    }

    public static short getTargetUnit(int target, int baseAddress) {
        short relativeTarget = getTarget(target, baseAddress);
        if (relativeTarget == ((short) relativeTarget)) {
            return (short) relativeTarget;
        }
        throw new DexException("Target out of range: " + Hex.s4(relativeTarget) + ", perhaps you " +
                "need to enable force jumbo mode.");
    }

    public static int getTarget(int target, int baseAddress) {
        return target - baseAddress;
    }

    public static int getLiteralByte(long literal) {
        if (literal == ((long) ((byte) ((int) literal)))) {
            return ((int) literal) & 255;
        }
        throw new DexException("Literal out of range: " + Hex.u8(literal));
    }

    public static short getLiteralUnit(long literal) {
        if (literal == ((long) ((short) ((int) literal)))) {
            return (short) ((int) literal);
        }
        throw new DexException("Literal out of range: " + Hex.u8(literal));
    }

    public static int getLiteralInt(long literal) {
        if (literal == ((long) ((int) literal))) {
            return (int) literal;
        }
        throw new DexException("Literal out of range: " + Hex.u8(literal));
    }

    public static int getLiteralNibble(long literal) {
        if (literal >= -8 && literal <= 7) {
            return ((int) literal) & 15;
        }
        throw new DexException("Literal out of range: " + Hex.u8(literal));
    }

    public static short getAUnit(int a) {
        if ((SupportMenu.CATEGORY_MASK & a) == 0) {
            return (short) a;
        }
        throw new DexException("Register A out of range: " + Hex.u8((long) a));
    }

    public static short getBUnit(int b) {
        if ((SupportMenu.CATEGORY_MASK & b) == 0) {
            return (short) b;
        }
        throw new DexException("Register B out of range: " + Hex.u8((long) b));
    }

    public static int getInstructionIndexType(int opcode) {
        switch (opcode) {
            case -1:
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 29:
            case 30:
            case 33:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case Opcodes.NEG_INT /*123*/:
            case Opcodes.NOT_INT /*124*/:
            case Opcodes.NEG_LONG /*125*/:
            case 126:
            case 127:
            case 128:
            case Opcodes.INT_TO_LONG /*129*/:
            case 130:
            case Opcodes.INT_TO_DOUBLE /*131*/:
            case 132:
            case Opcodes.LONG_TO_FLOAT /*133*/:
            case Opcodes.LONG_TO_DOUBLE /*134*/:
            case Opcodes.FLOAT_TO_INT /*135*/:
            case Opcodes.FLOAT_TO_LONG /*136*/:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case Opcodes.INT_TO_SHORT /*143*/:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case Opcodes.REM_FLOAT /*170*/:
            case Opcodes.ADD_DOUBLE /*171*/:
            case 172:
            case Opcodes.MUL_DOUBLE /*173*/:
            case Opcodes.DIV_DOUBLE /*174*/:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case Opcodes.USHR_INT_2ADDR /*186*/:
            case 187:
            case Opcodes.SUB_LONG_2ADDR /*188*/:
            case Opcodes.MUL_LONG_2ADDR /*189*/:
            case Opcodes.DIV_LONG_2ADDR /*190*/:
            case Opcodes.REM_LONG_2ADDR /*191*/:
            case 192:
            case 193:
            case Opcodes.XOR_LONG_2ADDR /*194*/:
            case 195:
            case Opcodes.SHR_LONG_2ADDR /*196*/:
            case Opcodes.USHR_LONG_2ADDR /*197*/:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            case 209:
            case Opcodes.MUL_INT_LIT16 /*210*/:
            case Opcodes.DIV_INT_LIT16 /*211*/:
            case Opcodes.REM_INT_LIT16 /*212*/:
            case Opcodes.AND_INT_LIT16 /*213*/:
            case Opcodes.OR_INT_LIT16 /*214*/:
            case Opcodes.XOR_INT_LIT16 /*215*/:
            case Opcodes.ADD_INT_LIT8 /*216*/:
            case Opcodes.RSUB_INT_LIT8 /*217*/:
            case Opcodes.MUL_INT_LIT8 /*218*/:
            case Opcodes.DIV_INT_LIT8 /*219*/:
            case Opcodes.REM_INT_LIT8 /*220*/:
            case Opcodes.AND_INT_LIT8 /*221*/:
            case Opcodes.OR_INT_LIT8 /*222*/:
            case Opcodes.XOR_INT_LIT8 /*223*/:
            case Opcodes.SHL_INT_LIT8 /*224*/:
            case Opcodes.SHR_INT_LIT8 /*225*/:
            case Opcodes.USHR_INT_LIT8 /*226*/:
            case 256:
            case 512:
            case Opcodes.FILL_ARRAY_DATA_PAYLOAD /*768*/:
                return 1;
            case 26:
            case 27:
                return 3;
            case 28:
            case 31:
            case 32:
            case 34:
            case 35:
            case 36:
            case 37:
                return 2;
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
                return 5;
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
            case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
            case Opcodes.INVOKE_SUPER_RANGE /*117*/:
            case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
            case Opcodes.INVOKE_STATIC_RANGE /*119*/:
            case 120:
                return 4;
            default:
                return 0;
        }
    }

    public static int getInstructionFormat(int opcode) {
        switch (opcode) {
            case -1:
                return 1;
            case 0:
            case 14:
                return 3;
            case 1:
            case 4:
            case 7:
            case 33:
            case Opcodes.NEG_INT /*123*/:
            case Opcodes.NOT_INT /*124*/:
            case Opcodes.NEG_LONG /*125*/:
            case 126:
            case 127:
            case 128:
            case Opcodes.INT_TO_LONG /*129*/:
            case 130:
            case Opcodes.INT_TO_DOUBLE /*131*/:
            case 132:
            case Opcodes.LONG_TO_FLOAT /*133*/:
            case Opcodes.LONG_TO_DOUBLE /*134*/:
            case Opcodes.FLOAT_TO_INT /*135*/:
            case Opcodes.FLOAT_TO_LONG /*136*/:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case Opcodes.INT_TO_SHORT /*143*/:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case Opcodes.USHR_INT_2ADDR /*186*/:
            case 187:
            case Opcodes.SUB_LONG_2ADDR /*188*/:
            case Opcodes.MUL_LONG_2ADDR /*189*/:
            case Opcodes.DIV_LONG_2ADDR /*190*/:
            case Opcodes.REM_LONG_2ADDR /*191*/:
            case 192:
            case 193:
            case Opcodes.XOR_LONG_2ADDR /*194*/:
            case 195:
            case Opcodes.SHR_LONG_2ADDR /*196*/:
            case Opcodes.USHR_LONG_2ADDR /*197*/:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
                return 6;
            case 2:
            case 5:
            case 8:
                return 16;
            case 3:
            case 6:
            case 9:
                return 22;
            case 10:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 29:
            case 30:
            case 39:
                return 5;
            case 18:
                return 4;
            case 19:
            case 22:
                return 10;
            case 20:
            case 23:
                return 20;
            case 21:
            case 25:
                return 9;
            case 24:
                return 25;
            case 26:
            case 28:
            case 31:
            case 34:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
                return 8;
            case 27:
                return 19;
            case 32:
            case 35:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
                return 13;
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                return 23;
            case 37:
            case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
            case Opcodes.INVOKE_SUPER_RANGE /*117*/:
            case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
            case Opcodes.INVOKE_STATIC_RANGE /*119*/:
            case 120:
                return 24;
            case 38:
            case 43:
            case 44:
                return 21;
            case 40:
                return 2;
            case 41:
                return 7;
            case 42:
                return 18;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case Opcodes.REM_FLOAT /*170*/:
            case Opcodes.ADD_DOUBLE /*171*/:
            case 172:
            case Opcodes.MUL_DOUBLE /*173*/:
            case Opcodes.DIV_DOUBLE /*174*/:
            case 175:
                return 17;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
                return 15;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
                return 11;
            case 208:
            case 209:
            case Opcodes.MUL_INT_LIT16 /*210*/:
            case Opcodes.DIV_INT_LIT16 /*211*/:
            case Opcodes.REM_INT_LIT16 /*212*/:
            case Opcodes.AND_INT_LIT16 /*213*/:
            case Opcodes.OR_INT_LIT16 /*214*/:
            case Opcodes.XOR_INT_LIT16 /*215*/:
                return 14;
            case Opcodes.ADD_INT_LIT8 /*216*/:
            case Opcodes.RSUB_INT_LIT8 /*217*/:
            case Opcodes.MUL_INT_LIT8 /*218*/:
            case Opcodes.DIV_INT_LIT8 /*219*/:
            case Opcodes.REM_INT_LIT8 /*220*/:
            case Opcodes.AND_INT_LIT8 /*221*/:
            case Opcodes.OR_INT_LIT8 /*222*/:
            case Opcodes.XOR_INT_LIT8 /*223*/:
            case Opcodes.SHL_INT_LIT8 /*224*/:
            case Opcodes.SHR_INT_LIT8 /*225*/:
            case Opcodes.USHR_INT_LIT8 /*226*/:
                return 12;
            case 256:
                return 27;
            case 512:
                return 28;
            case Opcodes.FILL_ARRAY_DATA_PAYLOAD /*768*/:
                return 26;
            default:
                return 0;
        }
    }
}
