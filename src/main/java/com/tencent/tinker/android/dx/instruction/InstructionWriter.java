package com.tencent.tinker.android.dx.instruction;

import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dx.util.Hex;

public final class InstructionWriter extends InstructionVisitor {
    private final ShortArrayCodeOutput codeOut;
    private final boolean              hasPromoter;
    private final InstructionPromoter  insnPromoter;

    public InstructionWriter(ShortArrayCodeOutput codeOut, InstructionPromoter ipmo) {
        super(null);
        this.codeOut = codeOut;
        this.insnPromoter = ipmo;
        this.hasPromoter = ipmo != null;
    }

    public void visitZeroRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal) {
        if (this.hasPromoter) {
            target = this.insnPromoter.getPromotedAddress(target);
        }
        int relativeTarget;
        switch (opcode) {
            case -1:
            case 0:
            case 14:
                this.codeOut.write((short) opcode);
                return;
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(0,
                        0)), (short) index, InstructionCodec.codeUnit(0, 0, 0, 0));
                return;
            case 40:
                if (this.hasPromoter) {
                    relativeTarget = InstructionCodec.getTarget(target, this.codeOut.cursor());
                    if (relativeTarget == ((byte) relativeTarget)) {
                        this.codeOut.write(InstructionCodec.codeUnit(opcode, relativeTarget & 255));
                        return;
                    } else if (relativeTarget != ((short) relativeTarget)) {
                        this.codeOut.write((short) 42, InstructionCodec.unit0(relativeTarget),
                                InstructionCodec.unit1(relativeTarget));
                        return;
                    } else {
                        this.codeOut.write((short) 41, (short) relativeTarget);
                        return;
                    }
                }
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec
                        .getTargetByte(target, this.codeOut.cursor())));
                return;
            case 41:
                short opcodeUnit;
                if (this.hasPromoter) {
                    relativeTarget = InstructionCodec.getTarget(target, this.codeOut.cursor());
                    if (relativeTarget != ((short) relativeTarget)) {
                        this.codeOut.write((short) 42, InstructionCodec.unit0(relativeTarget),
                                InstructionCodec.unit1(relativeTarget));
                        return;
                    }
                    opcodeUnit = (short) opcode;
                    this.codeOut.write(opcodeUnit, (short) relativeTarget);
                    return;
                }
                opcodeUnit = (short) opcode;
                this.codeOut.write(opcodeUnit, InstructionCodec.getTargetUnit(target, this
                        .codeOut.cursor()));
                return;
            case 42:
                relativeTarget = InstructionCodec.getTarget(target, this.codeOut.cursor());
                this.codeOut.write((short) opcode, InstructionCodec.unit0(relativeTarget),
                        InstructionCodec.unit1(relativeTarget));
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitOneRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                     int target, long literal, int a) {
        if (this.hasPromoter) {
            target = this.insnPromoter.getPromotedAddress(target);
        }
        switch (opcode) {
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
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a));
                return;
            case 18:
                this.codeOut.write(InstructionCodec.codeUnit((short) opcode, InstructionCodec
                        .makeByte(a, InstructionCodec.getLiteralNibble(literal))));
                return;
            case 19:
            case 22:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec
                        .getLiteralUnit(literal));
                return;
            case 20:
            case 23:
                int literalInt = InstructionCodec.getLiteralInt(literal);
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec.unit0
                        (literalInt), InstructionCodec.unit1(literalInt));
                return;
            case 21:
            case 25:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), (short) ((int) (literal
                        >> (opcode == 21 ? 16 : 48))));
                return;
            case 24:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec.unit0
                        (literal), InstructionCodec.unit1(literal), InstructionCodec.unit2
                        (literal), InstructionCodec.unit3(literal));
                return;
            case 26:
                if (this.hasPromoter) {
                    if (index > 65535) {
                        this.codeOut.write(InstructionCodec.codeUnit(27, a), InstructionCodec
                                .unit0(index), InstructionCodec.unit1(index));
                        return;
                    } else {
                        this.codeOut.write(InstructionCodec.codeUnit(opcode, a), (short) index);
                        return;
                    }
                } else if (index > 65535) {
                    throw new DexException("string index out of bound: " + Hex.u4(index) + ", " +
                            "perhaps you need to enable force jumbo mode.");
                } else {
                    this.codeOut.write(InstructionCodec.codeUnit(opcode, a), (short) index);
                    return;
                }
            case 27:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec.unit0
                        (index), InstructionCodec.unit1(index));
                return;
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
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), (short) index);
                return;
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(0,
                        1)), (short) index, InstructionCodec.codeUnit(a, 0, 0, 0));
                return;
            case 38:
            case 43:
            case 44:
                switch (opcode) {
                    case 43:
                    case 44:
                        this.codeOut.setBaseAddress(target, this.codeOut.cursor());
                        break;
                }
                int relativeTarget = InstructionCodec.getTarget(target, this.codeOut.cursor());
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec.unit0
                        (relativeTarget), InstructionCodec.unit1(relativeTarget));
                return;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec
                        .getTargetUnit(target, this.codeOut.cursor()));
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitTwoRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                     int target, long literal, int a, int b) {
        if (this.hasPromoter) {
            target = this.insnPromoter.getPromotedAddress(target);
        }
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
                this.codeOut.write(InstructionCodec.codeUnit((short) opcode, InstructionCodec
                        .makeByte(a, b)));
                return;
            case 2:
            case 5:
            case 8:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec
                        .getBUnit(b));
                return;
            case 3:
            case 6:
            case 9:
                this.codeOut.write((short) opcode, InstructionCodec.getAUnit(a), InstructionCodec
                        .getBUnit(b));
                return;
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
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(a,
                        b)), (short) index);
                return;
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(0,
                        2)), (short) index, InstructionCodec.codeUnit(a, b, 0, 0));
                return;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(a,
                        b)), InstructionCodec.getTargetUnit(target, this.codeOut.cursor()));
                return;
            case 208:
            case 209:
            case Opcodes.MUL_INT_LIT16 /*210*/:
            case Opcodes.DIV_INT_LIT16 /*211*/:
            case Opcodes.REM_INT_LIT16 /*212*/:
            case Opcodes.AND_INT_LIT16 /*213*/:
            case Opcodes.OR_INT_LIT16 /*214*/:
            case Opcodes.XOR_INT_LIT16 /*215*/:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(a,
                        b)), InstructionCodec.getLiteralUnit(literal));
                return;
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
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec
                        .codeUnit(b, InstructionCodec.getLiteralByte(literal)));
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitThreeRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                       int target, long literal, int a, int b, int c) {
        switch (opcode) {
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(0,
                        3)), (short) index, InstructionCodec.codeUnit(a, b, c, 0));
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
                this.codeOut.write(InstructionCodec.codeUnit(opcode, a), InstructionCodec
                        .codeUnit(b, c));
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitFourRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal, int a, int b, int c, int d) {
        switch (opcode) {
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(0,
                        4)), (short) index, InstructionCodec.codeUnit(a, b, c, d));
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitFiveRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal, int a, int b, int c, int d, int e) {
        switch (opcode) {
            case 36:
            case 110:
            case 111:
            case 112:
            case Opcodes.INVOKE_STATIC /*113*/:
            case Opcodes.INVOKE_INTERFACE /*114*/:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, InstructionCodec.makeByte(e,
                        5)), (short) index, InstructionCodec.codeUnit(a, b, c, d));
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitRegisterRangeInsn(int currentAddress, int opcode, int index, int indexType,
                                       int target, long literal, int a, int registerCount) {
        switch (opcode) {
            case 37:
            case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
            case Opcodes.INVOKE_SUPER_RANGE /*117*/:
            case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
            case Opcodes.INVOKE_STATIC_RANGE /*119*/:
            case 120:
                this.codeOut.write(InstructionCodec.codeUnit(opcode, registerCount), (short)
                        index, InstructionCodec.getAUnit(a));
                return;
            default:
                throw new IllegalStateException("unexpected opcode: " + Hex.u2or4(opcode));
        }
    }

    public void visitSparseSwitchPayloadInsn(int currentAddress, int opcode, int[] keys, int[]
            targets) {
        int length;
        int i = 0;
        int baseAddress = this.codeOut.baseAddressForCursor();
        this.codeOut.write((short) opcode);
        this.codeOut.write(InstructionCodec.asUnsignedUnit(targets.length));
        for (int key : keys) {
            this.codeOut.writeInt(key);
        }
        if (this.hasPromoter) {
            length = targets.length;
            while (i < length) {
                this.codeOut.writeInt(this.insnPromoter.getPromotedAddress(targets[i]) -
                        baseAddress);
                i++;
            }
            return;
        }
        length = targets.length;
        while (i < length) {
            this.codeOut.writeInt(targets[i] - baseAddress);
            i++;
        }
    }

    public void visitPackedSwitchPayloadInsn(int currentAddress, int opcode, int firstKey, int[]
            targets) {
        int i = 0;
        int baseAddress = this.codeOut.baseAddressForCursor();
        this.codeOut.write((short) opcode);
        this.codeOut.write(InstructionCodec.asUnsignedUnit(targets.length));
        this.codeOut.writeInt(firstKey);
        int length;
        if (this.hasPromoter) {
            length = targets.length;
            while (i < length) {
                this.codeOut.writeInt(this.insnPromoter.getPromotedAddress(targets[i]) -
                        baseAddress);
                i++;
            }
            return;
        }
        length = targets.length;
        while (i < length) {
            this.codeOut.writeInt(targets[i] - baseAddress);
            i++;
        }
    }

    public void visitFillArrayDataPayloadInsn(int currentAddress, int opcode, Object data, int
            size, int elementWidth) {
        this.codeOut.write((short) opcode);
        this.codeOut.write((short) elementWidth);
        this.codeOut.writeInt(size);
        switch (elementWidth) {
            case 1:
                this.codeOut.write((byte[]) data);
                return;
            case 2:
                this.codeOut.write((short[]) data);
                return;
            case 4:
                this.codeOut.write((int[]) data);
                return;
            case 8:
                this.codeOut.write((long[]) data);
                return;
            default:
                throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
        }
    }
}
