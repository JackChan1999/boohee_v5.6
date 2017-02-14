package com.tencent.tinker.android.dx.instruction;

public class InstructionVisitor {
    private final InstructionVisitor prevIv;

    public InstructionVisitor(InstructionVisitor iv) {
        this.prevIv = iv;
    }

    public void visitZeroRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal) {
        if (this.prevIv != null) {
            this.prevIv.visitZeroRegisterInsn(currentAddress, opcode, index, indexType, target,
                    literal);
        }
    }

    public void visitOneRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                     int target, long literal, int a) {
        if (this.prevIv != null) {
            this.prevIv.visitOneRegisterInsn(currentAddress, opcode, index, indexType, target,
                    literal, a);
        }
    }

    public void visitTwoRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                     int target, long literal, int a, int b) {
        if (this.prevIv != null) {
            this.prevIv.visitTwoRegisterInsn(currentAddress, opcode, index, indexType, target,
                    literal, a, b);
        }
    }

    public void visitThreeRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                       int target, long literal, int a, int b, int c) {
        if (this.prevIv != null) {
            this.prevIv.visitThreeRegisterInsn(currentAddress, opcode, index, indexType, target,
                    literal, a, b, c);
        }
    }

    public void visitFourRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal, int a, int b, int c, int d) {
        if (this.prevIv != null) {
            this.prevIv.visitFourRegisterInsn(currentAddress, opcode, index, indexType, target,
                    literal, a, b, c, d);
        }
    }

    public void visitFiveRegisterInsn(int currentAddress, int opcode, int index, int indexType,
                                      int target, long literal, int a, int b, int c, int d, int e) {
        if (this.prevIv != null) {
            this.prevIv.visitFiveRegisterInsn(currentAddress, opcode, index, indexType, target,
                    literal, a, b, c, d, e);
        }
    }

    public void visitRegisterRangeInsn(int currentAddress, int opcode, int index, int indexType,
                                       int target, long literal, int a, int registerCount) {
        if (this.prevIv != null) {
            this.prevIv.visitRegisterRangeInsn(currentAddress, opcode, index, indexType, target,
                    literal, a, registerCount);
        }
    }

    public void visitSparseSwitchPayloadInsn(int currentAddress, int opcode, int[] keys, int[]
            targets) {
        if (this.prevIv != null) {
            this.prevIv.visitSparseSwitchPayloadInsn(currentAddress, opcode, keys, targets);
        }
    }

    public void visitPackedSwitchPayloadInsn(int currentAddress, int opcode, int firstKey, int[]
            targets) {
        if (this.prevIv != null) {
            this.prevIv.visitPackedSwitchPayloadInsn(currentAddress, opcode, firstKey, targets);
        }
    }

    public void visitFillArrayDataPayloadInsn(int currentAddress, int opcode, Object data, int
            size, int elementWidth) {
        if (this.prevIv != null) {
            this.prevIv.visitFillArrayDataPayloadInsn(currentAddress, opcode, data, size,
                    elementWidth);
        }
    }
}
