package com.tencent.tinker.android.dx.instruction;

import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dx.util.Hex;

import java.io.EOFException;

public final class InstructionReader {
    private final ShortArrayCodeInput codeIn;

    public InstructionReader(ShortArrayCodeInput in) {
        this.codeIn = in;
    }

    public void accept(InstructionVisitor iv) throws EOFException {
        this.codeIn.reset();
        while (this.codeIn.hasMore()) {
            int currentAddress = this.codeIn.cursor();
            int opcodeUnit = this.codeIn.read();
            int opcodeForSwitch = Opcodes.extractOpcodeFromUnit(opcodeUnit);
            int opcode;
            int a;
            int bc;
            int baseAddress;
            int size;
            int[] targets;
            int i;
            switch (opcodeForSwitch) {
                case -1:
                    iv.visitZeroRegisterInsn(currentAddress, opcodeUnit, 0, 1, 0, 0);
                    break;
                case 0:
                case 14:
                    iv.visitZeroRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, (long) InstructionCodec.byte1(opcodeUnit));
                    break;
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
                    iv.visitTwoRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, 0, InstructionCodec.nibble2(opcodeUnit), InstructionCodec
                                    .nibble3(opcodeUnit));
                    break;
                case 2:
                case 5:
                case 8:
                    iv.visitTwoRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, 0, InstructionCodec.byte1(opcodeUnit), this.codeIn.read());
                    break;
                case 3:
                case 6:
                case 9:
                    iv.visitTwoRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, (long) InstructionCodec.byte1(opcodeUnit), this.codeIn.read
                                    (), this.codeIn.read());
                    break;
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
                    iv.visitOneRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, 0, InstructionCodec.byte1(opcodeUnit));
                    break;
                case 18:
                    iv.visitOneRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, (long) ((InstructionCodec.nibble3(opcodeUnit) << 28) >> 28),
                            InstructionCodec.nibble2(opcodeUnit));
                    break;
                case 19:
                case 22:
                    iv.visitOneRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, (long) ((short) this.codeIn.read()), InstructionCodec.byte1
                                    (opcodeUnit));
                    break;
                case 20:
                case 23:
                    iv.visitOneRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, (long) this.codeIn.readInt(), InstructionCodec.byte1
                                    (opcodeUnit));
                    break;
                case 21:
                case 25:
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    iv.visitOneRegisterInsn(currentAddress, opcode, 0, 1, 0, ((long) ((short)
                            this.codeIn.read())) << (opcode == 21 ? 16 : 48), InstructionCodec
                            .byte1(opcodeUnit));
                    break;
                case 24:
                    iv.visitOneRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, this.codeIn.readLong(), InstructionCodec.byte1(opcodeUnit));
                    break;
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
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    iv.visitOneRegisterInsn(currentAddress, opcode, this.codeIn.read(),
                            InstructionCodec.getInstructionIndexType(opcode), 0, 0,
                            InstructionCodec.byte1(opcodeUnit));
                    break;
                case 27:
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    iv.visitOneRegisterInsn(currentAddress, opcode, this.codeIn.readInt(),
                            InstructionCodec.getInstructionIndexType(opcode), 0, 0,
                            InstructionCodec.byte1(opcodeUnit));
                    break;
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
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    iv.visitTwoRegisterInsn(currentAddress, opcode, this.codeIn.read(),
                            InstructionCodec.getInstructionIndexType(opcode), 0, 0,
                            InstructionCodec.nibble2(opcodeUnit), InstructionCodec.nibble3
                                    (opcodeUnit));
                    break;
                case 36:
                case 110:
                case 111:
                case 112:
                case Opcodes.INVOKE_STATIC /*113*/:
                case Opcodes.INVOKE_INTERFACE /*114*/:
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    int e = InstructionCodec.nibble2(opcodeUnit);
                    int registerCount = InstructionCodec.nibble3(opcodeUnit);
                    int index = this.codeIn.read();
                    int abcd = this.codeIn.read();
                    a = InstructionCodec.nibble0(abcd);
                    int b = InstructionCodec.nibble1(abcd);
                    int c = InstructionCodec.nibble2(abcd);
                    int d = InstructionCodec.nibble3(abcd);
                    int indexType = InstructionCodec.getInstructionIndexType(opcode);
                    switch (registerCount) {
                        case 0:
                            iv.visitZeroRegisterInsn(currentAddress, opcode, index, indexType, 0,
                                    0);
                            break;
                        case 1:
                            iv.visitOneRegisterInsn(currentAddress, opcode, index, indexType, 0,
                                    0, a);
                            break;
                        case 2:
                            iv.visitTwoRegisterInsn(currentAddress, opcode, index, indexType, 0,
                                    0, a, b);
                            break;
                        case 3:
                            iv.visitThreeRegisterInsn(currentAddress, opcode, index, indexType,
                                    0, 0, a, b, c);
                            break;
                        case 4:
                            iv.visitFourRegisterInsn(currentAddress, opcode, index, indexType, 0,
                                    0, a, b, c, d);
                            break;
                        case 5:
                            iv.visitFiveRegisterInsn(currentAddress, opcode, index, indexType, 0,
                                    0, a, b, c, d, e);
                            break;
                        default:
                            throw new DexException("bogus registerCount: " + Hex.uNibble
                                    (registerCount));
                    }
                case 37:
                case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
                case Opcodes.INVOKE_SUPER_RANGE /*117*/:
                case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
                case Opcodes.INVOKE_STATIC_RANGE /*119*/:
                case 120:
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    iv.visitRegisterRangeInsn(currentAddress, opcode, this.codeIn.read(),
                            InstructionCodec.getInstructionIndexType(opcode), 0, 0, this.codeIn
                                    .read(), InstructionCodec.byte1(opcodeUnit));
                    break;
                case 38:
                case 43:
                case 44:
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    a = InstructionCodec.byte1(opcodeUnit);
                    int target = currentAddress + this.codeIn.readInt();
                    switch (opcode) {
                        case 43:
                        case 44:
                            this.codeIn.setBaseAddress(target + 1, currentAddress);
                            break;
                    }
                    iv.visitOneRegisterInsn(currentAddress, opcode, 0, 1, target, 0, a);
                    break;
                case 40:
                    iv.visitZeroRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, currentAddress + ((byte) InstructionCodec.byte1(opcodeUnit)), 0);
                    break;
                case 41:
                    iv.visitZeroRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, currentAddress + ((short) this.codeIn.read()), (long)
                                    InstructionCodec.byte1(opcodeUnit));
                    break;
                case 42:
                    iv.visitZeroRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, this.codeIn.readInt() + currentAddress, (long) InstructionCodec
                                    .byte1(opcodeUnit));
                    break;
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
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    a = InstructionCodec.byte1(opcodeUnit);
                    bc = this.codeIn.read();
                    iv.visitThreeRegisterInsn(currentAddress, opcode, 0, 1, 0, 0, a,
                            InstructionCodec.byte0(bc), InstructionCodec.byte1(bc));
                    break;
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                    iv.visitTwoRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, ((short) this.codeIn.read()) + currentAddress, 0,
                            InstructionCodec.nibble2(opcodeUnit), InstructionCodec.nibble3
                                    (opcodeUnit));
                    break;
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                    iv.visitOneRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, ((short) this.codeIn.read()) + currentAddress, 0,
                            InstructionCodec.byte1(opcodeUnit));
                    break;
                case 208:
                case 209:
                case Opcodes.MUL_INT_LIT16 /*210*/:
                case Opcodes.DIV_INT_LIT16 /*211*/:
                case Opcodes.REM_INT_LIT16 /*212*/:
                case Opcodes.AND_INT_LIT16 /*213*/:
                case Opcodes.OR_INT_LIT16 /*214*/:
                case Opcodes.XOR_INT_LIT16 /*215*/:
                    iv.visitTwoRegisterInsn(currentAddress, InstructionCodec.byte0(opcodeUnit),
                            0, 1, 0, (long) ((short) this.codeIn.read()), InstructionCodec
                                    .nibble2(opcodeUnit), InstructionCodec.nibble3(opcodeUnit));
                    break;
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
                    opcode = InstructionCodec.byte0(opcodeUnit);
                    a = InstructionCodec.byte1(opcodeUnit);
                    bc = this.codeIn.read();
                    iv.visitTwoRegisterInsn(currentAddress, opcode, 0, 1, 0, (long) ((byte)
                            InstructionCodec.byte1(bc)), a, InstructionCodec.byte0(bc));
                    break;
                case 256:
                    baseAddress = this.codeIn.baseAddressForCursor();
                    size = this.codeIn.read();
                    int firstKey = this.codeIn.readInt();
                    targets = new int[size];
                    for (i = 0; i < size; i++) {
                        targets[i] = this.codeIn.readInt() + baseAddress;
                    }
                    iv.visitPackedSwitchPayloadInsn(currentAddress, opcodeUnit, firstKey, targets);
                    break;
                case 512:
                    baseAddress = this.codeIn.baseAddressForCursor();
                    size = this.codeIn.read();
                    int[] keys = new int[size];
                    targets = new int[size];
                    for (i = 0; i < size; i++) {
                        keys[i] = this.codeIn.readInt();
                    }
                    for (i = 0; i < size; i++) {
                        targets[i] = this.codeIn.readInt() + baseAddress;
                    }
                    iv.visitSparseSwitchPayloadInsn(currentAddress, opcodeUnit, keys, targets);
                    break;
                case Opcodes.FILL_ARRAY_DATA_PAYLOAD /*768*/:
                    int elementWidth = this.codeIn.read();
                    size = this.codeIn.readInt();
                    switch (elementWidth) {
                        case 1:
                            byte[] array = new byte[size];
                            boolean even = true;
                            i = 0;
                            int value = 0;
                            while (i < size) {
                                if (even) {
                                    value = this.codeIn.read();
                                }
                                array[i] = (byte) (value & 255);
                                value >>= 8;
                                i++;
                                even = !even;
                            }
                            iv.visitFillArrayDataPayloadInsn(currentAddress, opcodeUnit, array,
                                    array.length, 1);
                            break;
                        case 2:
                            short[] array2 = new short[size];
                            for (i = 0; i < size; i++) {
                                array2[i] = (short) this.codeIn.read();
                            }
                            iv.visitFillArrayDataPayloadInsn(currentAddress, opcodeUnit, array2,
                                    array2.length, 2);
                            break;
                        case 4:
                            int[] array3 = new int[size];
                            for (i = 0; i < size; i++) {
                                array3[i] = this.codeIn.readInt();
                            }
                            iv.visitFillArrayDataPayloadInsn(currentAddress, opcodeUnit, array3,
                                    array3.length, 4);
                            break;
                        case 8:
                            long[] array4 = new long[size];
                            for (i = 0; i < size; i++) {
                                array4[i] = this.codeIn.readLong();
                            }
                            iv.visitFillArrayDataPayloadInsn(currentAddress, opcodeUnit, array4,
                                    array4.length, 8);
                            break;
                        default:
                            throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
                    }
                default:
                    throw new IllegalStateException("Unknown opcode: " + Hex.u4(opcodeForSwitch));
            }
        }
    }
}
