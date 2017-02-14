package com.tencent.tinker.android.dx.util;

import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dx.instruction.InstructionPromoter;
import com.tencent.tinker.android.dx.instruction.InstructionReader;
import com.tencent.tinker.android.dx.instruction.InstructionVisitor;
import com.tencent.tinker.android.dx.instruction.InstructionWriter;
import com.tencent.tinker.android.dx.instruction.ShortArrayCodeInput;
import com.tencent.tinker.android.dx.instruction.ShortArrayCodeOutput;

public final class InstructionTransformer {
    private final IndexMap indexMap;

    private final class InstructionTransformVisitor extends InstructionVisitor {
        InstructionTransformVisitor(InstructionVisitor iv) {
            super(iv);
        }

        public void visitZeroRegisterInsn(int currentAddress, int opcode, int index, int
                indexType, int target, long literal) {
            super.visitZeroRegisterInsn(currentAddress, opcode, transformIndexIfNeeded(index,
                    indexType), indexType, target, literal);
        }

        public void visitOneRegisterInsn(int currentAddress, int opcode, int index, int
                indexType, int target, long literal, int a) {
            super.visitOneRegisterInsn(currentAddress, opcode, transformIndexIfNeeded(index,
                    indexType), indexType, target, literal, a);
        }

        public void visitTwoRegisterInsn(int currentAddress, int opcode, int index, int
                indexType, int target, long literal, int a, int b) {
            super.visitTwoRegisterInsn(currentAddress, opcode, transformIndexIfNeeded(index,
                    indexType), indexType, target, literal, a, b);
        }

        public void visitThreeRegisterInsn(int currentAddress, int opcode, int index, int
                indexType, int target, long literal, int a, int b, int c) {
            super.visitThreeRegisterInsn(currentAddress, opcode, transformIndexIfNeeded(index,
                    indexType), indexType, target, literal, a, b, c);
        }

        public void visitFourRegisterInsn(int currentAddress, int opcode, int index, int
                indexType, int target, long literal, int a, int b, int c, int d) {
            super.visitFourRegisterInsn(currentAddress, opcode, transformIndexIfNeeded(index,
                    indexType), indexType, target, literal, a, b, c, d);
        }

        public void visitFiveRegisterInsn(int currentAddress, int opcode, int index, int
                indexType, int target, long literal, int a, int b, int c, int d, int e) {
            super.visitFiveRegisterInsn(currentAddress, opcode, transformIndexIfNeeded(index,
                    indexType), indexType, target, literal, a, b, c, d, e);
        }

        public void visitRegisterRangeInsn(int currentAddress, int opcode, int index, int
                indexType, int target, long literal, int a, int registerCount) {
            super.visitRegisterRangeInsn(currentAddress, opcode, transformIndexIfNeeded(index,
                    indexType), indexType, target, literal, a, registerCount);
        }

        private int transformIndexIfNeeded(int index, int indexType) {
            switch (indexType) {
                case 2:
                    return InstructionTransformer.this.indexMap.adjustTypeIdIndex(index);
                case 3:
                    return InstructionTransformer.this.indexMap.adjustStringIndex(index);
                case 4:
                    return InstructionTransformer.this.indexMap.adjustMethodIdIndex(index);
                case 5:
                    return InstructionTransformer.this.indexMap.adjustFieldIdIndex(index);
                default:
                    return index;
            }
        }
    }

    public InstructionTransformer(IndexMap indexMap) {
        this.indexMap = indexMap;
    }

    public short[] transform(short[] encodedInstructions) throws DexException {
        ShortArrayCodeOutput out = new ShortArrayCodeOutput(encodedInstructions.length);
        InstructionPromoter ipmo = new InstructionPromoter();
        InstructionWriter iw = new InstructionWriter(out, ipmo);
        InstructionReader ir = new InstructionReader(new ShortArrayCodeInput(encodedInstructions));
        try {
            ir.accept(new InstructionTransformVisitor(ipmo));
            ir.accept(new InstructionTransformVisitor(iw));
            return out.getArray();
        } catch (Throwable e) {
            throw new DexException(e);
        }
    }
}
