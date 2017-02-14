package com.tencent.tinker.android.dx.instruction;

import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dex.util.CompareUtils;
import com.tencent.tinker.android.dx.util.Hex;
import com.umeng.socialize.common.SocializeConstants;

import java.io.EOFException;
import java.util.HashSet;
import java.util.Set;

public abstract class InstructionComparator {
    private final InstructionHolder[] insnHolders1;
    private final InstructionHolder[] insnHolders2;
    private final short[]             insns1;
    private final short[]             insns2;
    private final Set<String>         visitedInsnAddrPairs;

    private static class InstructionHolder {
        int  a;
        int  address;
        int  b;
        int  c;
        int  d;
        int  e;
        int  index;
        int  insnFormat;
        long literal;
        int  opcode;
        int  registerCount;
        int  target;

        private InstructionHolder() {
            this.insnFormat = 0;
            this.address = -1;
            this.opcode = -1;
            this.index = 0;
            this.target = 0;
            this.literal = 0;
            this.registerCount = 0;
            this.a = 0;
            this.b = 0;
            this.c = 0;
            this.d = 0;
            this.e = 0;
        }
    }

    private static class FillArrayDataPayloadInstructionHolder extends InstructionHolder {
        Object data;
        int    elementWidth;
        int    size;

        private FillArrayDataPayloadInstructionHolder() {
            super();
            this.data = null;
            this.size = 0;
            this.elementWidth = 0;
        }
    }

    private static class PackedSwitchPayloadInsntructionHolder extends InstructionHolder {
        int   firstKey;
        int[] targets;

        private PackedSwitchPayloadInsntructionHolder() {
            super();
            this.firstKey = 0;
            this.targets = null;
        }
    }

    private static class SparseSwitchPayloadInsntructionHolder extends InstructionHolder {
        int[] keys;
        int[] targets;

        private SparseSwitchPayloadInsntructionHolder() {
            super();
            this.keys = null;
            this.targets = null;
        }
    }

    protected abstract boolean compareField(int i, int i2);

    protected abstract boolean compareMethod(int i, int i2);

    protected abstract boolean compareString(int i, int i2);

    protected abstract boolean compareType(int i, int i2);

    public InstructionComparator(short[] insns1, short[] insns2) {
        this.insns1 = insns1;
        this.insns2 = insns2;
        if (insns1 != null) {
            this.insnHolders1 = readInstructionsIntoHolders(new ShortArrayCodeInput(insns1),
                    insns1.length);
        } else {
            this.insnHolders1 = null;
        }
        if (insns2 != null) {
            this.insnHolders2 = readInstructionsIntoHolders(new ShortArrayCodeInput(insns2),
                    insns2.length);
        } else {
            this.insnHolders2 = null;
        }
        this.visitedInsnAddrPairs = new HashSet();
    }

    private InstructionHolder[] readInstructionsIntoHolders(ShortArrayCodeInput in, int length) {
        in.reset();
        final InstructionHolder[] result = new InstructionHolder[length];
        try {
            new InstructionReader(in).accept(new InstructionVisitor(null) {
                public void visitZeroRegisterInsn(int currentAddress, int opcode, int index, int
                        indexType, int target, long literal) {
                    InstructionHolder insnHolder = new InstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.index = index;
                    insnHolder.target = target;
                    insnHolder.literal = literal;
                    result[currentAddress] = insnHolder;
                }

                public void visitOneRegisterInsn(int currentAddress, int opcode, int index, int
                        indexType, int target, long literal, int a) {
                    InstructionHolder insnHolder = new InstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.index = index;
                    insnHolder.target = target;
                    insnHolder.literal = literal;
                    insnHolder.registerCount = 1;
                    insnHolder.a = a;
                    result[currentAddress] = insnHolder;
                }

                public void visitTwoRegisterInsn(int currentAddress, int opcode, int index, int
                        indexType, int target, long literal, int a, int b) {
                    InstructionHolder insnHolder = new InstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.index = index;
                    insnHolder.target = target;
                    insnHolder.literal = literal;
                    insnHolder.registerCount = 2;
                    insnHolder.a = a;
                    insnHolder.b = b;
                    result[currentAddress] = insnHolder;
                }

                public void visitThreeRegisterInsn(int currentAddress, int opcode, int index, int
                        indexType, int target, long literal, int a, int b, int c) {
                    InstructionHolder insnHolder = new InstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.index = index;
                    insnHolder.target = target;
                    insnHolder.literal = literal;
                    insnHolder.registerCount = 3;
                    insnHolder.a = a;
                    insnHolder.b = b;
                    insnHolder.c = c;
                    result[currentAddress] = insnHolder;
                }

                public void visitFourRegisterInsn(int currentAddress, int opcode, int index, int
                        indexType, int target, long literal, int a, int b, int c, int d) {
                    InstructionHolder insnHolder = new InstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.index = index;
                    insnHolder.target = target;
                    insnHolder.literal = literal;
                    insnHolder.registerCount = 4;
                    insnHolder.a = a;
                    insnHolder.b = b;
                    insnHolder.c = c;
                    insnHolder.d = d;
                    result[currentAddress] = insnHolder;
                }

                public void visitFiveRegisterInsn(int currentAddress, int opcode, int index, int
                        indexType, int target, long literal, int a, int b, int c, int d, int e) {
                    InstructionHolder insnHolder = new InstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.index = index;
                    insnHolder.target = target;
                    insnHolder.literal = literal;
                    insnHolder.registerCount = 5;
                    insnHolder.a = a;
                    insnHolder.b = b;
                    insnHolder.c = c;
                    insnHolder.d = d;
                    insnHolder.e = e;
                    result[currentAddress] = insnHolder;
                }

                public void visitRegisterRangeInsn(int currentAddress, int opcode, int index, int
                        indexType, int target, long literal, int a, int registerCount) {
                    InstructionHolder insnHolder = new InstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.index = index;
                    insnHolder.target = target;
                    insnHolder.literal = literal;
                    insnHolder.registerCount = registerCount;
                    insnHolder.a = a;
                    result[currentAddress] = insnHolder;
                }

                public void visitSparseSwitchPayloadInsn(int currentAddress, int opcode, int[]
                        keys, int[] targets) {
                    SparseSwitchPayloadInsntructionHolder insnHolder = new
                            SparseSwitchPayloadInsntructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.keys = keys;
                    insnHolder.targets = targets;
                    result[currentAddress] = insnHolder;
                }

                public void visitPackedSwitchPayloadInsn(int currentAddress, int opcode, int
                        firstKey, int[] targets) {
                    PackedSwitchPayloadInsntructionHolder insnHolder = new
                            PackedSwitchPayloadInsntructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.firstKey = firstKey;
                    insnHolder.targets = targets;
                    result[currentAddress] = insnHolder;
                }

                public void visitFillArrayDataPayloadInsn(int currentAddress, int opcode, Object
                        data, int size, int elementWidth) {
                    FillArrayDataPayloadInstructionHolder insnHolder = new
                            FillArrayDataPayloadInstructionHolder();
                    insnHolder.insnFormat = InstructionCodec.getInstructionFormat(opcode);
                    insnHolder.address = currentAddress;
                    insnHolder.opcode = opcode;
                    insnHolder.data = data;
                    insnHolder.size = size;
                    insnHolder.elementWidth = elementWidth;
                    result[currentAddress] = insnHolder;
                }
            });
            return result;
        } catch (EOFException e) {
            throw new RuntimeException(e);
        }
    }

    public final boolean compare() {
        boolean z = true;
        this.visitedInsnAddrPairs.clear();
        if (this.insnHolders1 == null && this.insnHolders2 == null) {
            return true;
        }
        if (this.insnHolders1 == null || this.insnHolders2 == null) {
            return false;
        }
        int currAddress1 = 0;
        int currAddress2 = 0;
        int insnHolderCount1 = 0;
        int insnHolderCount2 = 0;
        while (currAddress1 < this.insnHolders1.length && r2 < this.insnHolders2.length) {
            InstructionHolder insnHolder1 = null;
            InstructionHolder insnHolder2 = null;
            while (currAddress1 < this.insnHolders1.length && insnHolder1 == null) {
                int currAddress12 = currAddress1 + 1;
                insnHolder1 = this.insnHolders1[currAddress1];
                currAddress1 = currAddress12;
            }
            if (insnHolder1 == null) {
                break;
            }
            insnHolderCount1++;
            while (currAddress2 < this.insnHolders2.length && insnHolder2 == null) {
                int currAddress22 = currAddress2 + 1;
                insnHolder2 = this.insnHolders2[currAddress2];
                currAddress2 = currAddress22;
            }
            if (insnHolder2 == null) {
                break;
            }
            insnHolderCount2++;
            if (insnHolder1.opcode != insnHolder2.opcode) {
                if (insnHolder1.opcode == 26 && insnHolder2.opcode == 27) {
                    if (!compareString(insnHolder1.index, insnHolder2.index)) {
                        return false;
                    }
                } else if (insnHolder1.opcode != 27 || insnHolder2.opcode != 26) {
                    return false;
                } else {
                    if (!compareString(insnHolder1.index, insnHolder2.index)) {
                        return false;
                    }
                }
            } else if (!isSameInstruction(insnHolder1.address, insnHolder2.address)) {
                return false;
            }
        }
        while (currAddress1 < this.insnHolders1.length) {
            currAddress12 = currAddress1 + 1;
            if (this.insnHolders1[currAddress1] != null) {
                return false;
            }
            currAddress1 = currAddress12;
        }
        while (currAddress2 < this.insnHolders2.length) {
            currAddress22 = currAddress2 + 1;
            if (this.insnHolders2[currAddress2] != null) {
                return false;
            }
            currAddress2 = currAddress22;
        }
        if (insnHolderCount1 != insnHolderCount2) {
            z = false;
        }
        return z;
    }

    public boolean isSameInstruction(int insnAddress1, int insnAddress2) {
        InstructionHolder insnHolder1 = this.insnHolders1[insnAddress1];
        InstructionHolder insnHolder2 = this.insnHolders2[insnAddress2];
        if (insnHolder1 == null && insnHolder2 == null) {
            return true;
        }
        if (insnHolder1 == null || insnHolder2 == null) {
            return false;
        }
        if (insnHolder1.opcode != insnHolder2.opcode) {
            return false;
        }
        int opcode = insnHolder1.opcode;
        int targetCount;
        int i;
        switch (insnHolder1.insnFormat) {
            case 2:
            case 7:
            case 11:
            case 15:
            case 18:
            case 21:
                if (this.visitedInsnAddrPairs.add(insnAddress1 + SocializeConstants
                        .OP_DIVIDER_MINUS + insnAddress2)) {
                    return isSameInstruction(insnHolder1.target, insnHolder2.target);
                }
                return true;
            case 8:
            case 13:
            case 19:
            case 23:
            case 24:
                return compareIndex(opcode, insnHolder1.index, insnHolder2.index);
            case 26:
                FillArrayDataPayloadInstructionHolder specInsnHolder1 =
                        (FillArrayDataPayloadInstructionHolder) insnHolder1;
                FillArrayDataPayloadInstructionHolder specInsnHolder2 =
                        (FillArrayDataPayloadInstructionHolder) insnHolder2;
                if (specInsnHolder1.elementWidth != specInsnHolder2.elementWidth) {
                    return false;
                }
                if (specInsnHolder1.size != specInsnHolder2.size) {
                    return false;
                }
                int elementWidth = specInsnHolder1.elementWidth;
                switch (elementWidth) {
                    case 1:
                        return CompareUtils.uArrCompare((byte[]) ((byte[]) specInsnHolder1.data),
                                (byte[]) ((byte[]) specInsnHolder2.data)) == 0;
                    case 2:
                        return CompareUtils.uArrCompare((short[]) ((short[]) specInsnHolder1
                                .data), (short[]) ((short[]) specInsnHolder2.data)) == 0;
                    case 4:
                        return CompareUtils.uArrCompare((int[]) ((int[]) specInsnHolder1.data),
                                (int[]) ((int[]) specInsnHolder2.data)) == 0;
                    case 8:
                        return CompareUtils.sArrCompare((long[]) ((long[]) specInsnHolder1.data),
                                (long[]) ((long[]) specInsnHolder2.data)) == 0;
                    default:
                        throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
                }
            case 27:
                PackedSwitchPayloadInsntructionHolder specInsnHolder12 =
                        (PackedSwitchPayloadInsntructionHolder) insnHolder1;
                PackedSwitchPayloadInsntructionHolder specInsnHolder22 =
                        (PackedSwitchPayloadInsntructionHolder) insnHolder2;
                if (specInsnHolder12.firstKey != specInsnHolder22.firstKey) {
                    return false;
                }
                if (specInsnHolder12.targets.length != specInsnHolder22.targets.length) {
                    return false;
                }
                targetCount = specInsnHolder12.targets.length;
                for (i = 0; i < targetCount; i++) {
                    if (!isSameInstruction(specInsnHolder12.targets[i], specInsnHolder22
                            .targets[i])) {
                        return false;
                    }
                }
                return true;
            case 28:
                SparseSwitchPayloadInsntructionHolder specInsnHolder13 =
                        (SparseSwitchPayloadInsntructionHolder) insnHolder1;
                SparseSwitchPayloadInsntructionHolder specInsnHolder23 =
                        (SparseSwitchPayloadInsntructionHolder) insnHolder2;
                if (CompareUtils.uArrCompare(specInsnHolder13.keys, specInsnHolder23.keys) != 0) {
                    return false;
                }
                if (specInsnHolder13.targets.length != specInsnHolder23.targets.length) {
                    return false;
                }
                targetCount = specInsnHolder13.targets.length;
                for (i = 0; i < targetCount; i++) {
                    if (!isSameInstruction(specInsnHolder13.targets[i], specInsnHolder23
                            .targets[i])) {
                        return false;
                    }
                }
                return true;
            default:
                if (insnHolder1.literal != insnHolder2.literal) {
                    return false;
                }
                if (insnHolder1.registerCount != insnHolder2.registerCount) {
                    return false;
                }
                if (insnHolder1.a != insnHolder2.a) {
                    return false;
                }
                if (insnHolder1.b != insnHolder2.b) {
                    return false;
                }
                if (insnHolder1.c != insnHolder2.c) {
                    return false;
                }
                if (insnHolder1.d != insnHolder2.d) {
                    return false;
                }
                if (insnHolder1.e != insnHolder2.e) {
                    return false;
                }
                return true;
        }
    }

    private boolean compareIndex(int opcode, int index1, int index2) {
        switch (InstructionCodec.getInstructionIndexType(opcode)) {
            case 2:
                return compareType(index1, index2);
            case 3:
                return compareString(index1, index2);
            case 4:
                return compareMethod(index1, index2);
            case 5:
                return compareField(index1, index2);
            default:
                if (index1 == index2) {
                    return true;
                }
                return false;
        }
    }
}
