package com.tencent.tinker.android.dx.instruction;

import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dx.util.Hex;
import com.tencent.tinker.android.utils.SparseIntArray;

public final class InstructionPromoter extends InstructionVisitor {
    private final SparseIntArray addressMap             = new SparseIntArray();
    private       int            currentPromotedAddress = 0;

    public InstructionPromoter() {
        super(null);
    }

    private void mapAddressIfNeeded(int currentAddress) {
        if (currentAddress != this.currentPromotedAddress) {
            this.addressMap.append(currentAddress, this.currentPromotedAddress);
        }
    }

    public int getPromotedAddress(int currentAddress) {
        int index = this.addressMap.indexOfKey(currentAddress);
        return index < 0 ? currentAddress : this.addressMap.valueAt(index);
    }

    public int getPromotedAddressCount() {
        return this.addressMap.size();
    }

    public void visitZeroRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal) {
        mapAddressIfNeeded(currentAddress);
        switch (opcode) {
            case -1:
            case 0:
            case 14:
                this.currentPromotedAddress++;
                return;
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.currentPromotedAddress += 3;
                return;
            case 40:
                byte relativeTarget = InstructionCodec.getTarget(target, this
                        .currentPromotedAddress);
                if (relativeTarget == ((byte) relativeTarget)) {
                    this.currentPromotedAddress++;
                    return;
                } else if (relativeTarget != ((short) relativeTarget)) {
                    this.currentPromotedAddress += 3;
                    return;
                } else {
                    this.currentPromotedAddress += 2;
                    return;
                }
            case 41:
                short relativeTarget2 = InstructionCodec.getTarget(target, this
                        .currentPromotedAddress);
                if (relativeTarget2 != ((short) relativeTarget2)) {
                    this.currentPromotedAddress += 3;
                    return;
                } else {
                    this.currentPromotedAddress += 2;
                    return;
                }
            case 42:
                this.currentPromotedAddress += 3;
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitOneRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                     int target, long literal, int a) {
        mapAddressIfNeeded(currentAddress);
        switch (opcode) {
            case 10:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 29:
            case 30:
            case 39:
                this.currentPromotedAddress++;
                return;
            case 19:
            case 21:
            case 22:
            case 25:
            case 28:
            case 31:
            case 34:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
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
                this.currentPromotedAddress += 2;
                return;
            case 20:
            case 23:
            case 36:
            case 38:
            case 43:
            case 44:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.currentPromotedAddress += 3;
                return;
            case 24:
                this.currentPromotedAddress += 5;
                return;
            case 26:
                if (index > 65535) {
                    this.currentPromotedAddress += 3;
                    return;
                } else {
                    this.currentPromotedAddress += 2;
                    return;
                }
            case 27:
                this.currentPromotedAddress += 3;
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitTwoRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                     int target, long literal, int a, int b) {
        mapAddressIfNeeded(currentAddress);
        switch (opcode) {
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
                this.currentPromotedAddress++;
                return;
            case 2:
            case 5:
            case 8:
                this.currentPromotedAddress += 2;
                return;
            case 3:
            case 6:
            case 9:
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.currentPromotedAddress += 3;
                return;
            case 32:
            case 35:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
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
                this.currentPromotedAddress += 2;
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitThreeRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                       int target, long literal, int a, int b, int c) {
        mapAddressIfNeeded(currentAddress);
        switch (opcode) {
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.currentPromotedAddress += 3;
                return;
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
                this.currentPromotedAddress += 2;
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitFourRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal, int a, int b, int c, int d) {
        mapAddressIfNeeded(currentAddress);
        switch (opcode) {
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.currentPromotedAddress += 3;
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitFiveRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal, int a, int b, int c, int d, int e) {
        mapAddressIfNeeded(currentAddress);
        switch (opcode) {
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.currentPromotedAddress += 3;
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitRegisterRangeInsn(int currentAddress, int opcode, int index, int indexType,
                                       int target, long literal, int a, int registerCount) {
        mapAddressIfNeeded(currentAddress);
        switch (opcode) {
            case 37:
            case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
            case Opcodes.INVOKE_SUPER_RANGE /*117*/:
            case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
            case Opcodes.INVOKE_STATIC_RANGE /*119*/:
            case 120:
                this.currentPromotedAddress += 3;
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitSparseSwitchPayloadInsn(int currentAddress, int opcode, int[] keys, int[]
            targets) {
        mapAddressIfNeeded(currentAddress);
        this.currentPromotedAddress += 2;
        this.currentPromotedAddress += keys.length * 2;
        this.currentPromotedAddress += targets.length * 2;
    }

    public void visitPackedSwitchPayloadInsn(int currentAddress, int opcode, int firstKey, int[]
            targets) {
        mapAddressIfNeeded(currentAddress);
        this.currentPromotedAddress += 4;
        this.currentPromotedAddress += targets.length * 2;
    }

    public void visitFillArrayDataPayloadInsn(int currentAddress, int opcode, Object data, int
            size, int elementWidth) {
        mapAddressIfNeeded(currentAddress);
        this.currentPromotedAddress += 4;
        switch (elementWidth) {
            case 1:
                int length = ((byte[]) data).length;
                this.currentPromotedAddress += (length >> 1) + (length & 1);
                return;
            case 2:
                this.currentPromotedAddress += ((short[]) data).length * 1;
                return;
            case 4:
                this.currentPromotedAddress += ((int[]) data).length * 2;
                return;
            case 8:
                this.currentPromotedAddress += ((long[]) data).length * 4;
                return;
            default:
                throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
        }
    }
}
