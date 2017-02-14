package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public class AnnotationsDirectory extends Item<AnnotationsDirectory> {
    public int     classAnnotationsOffset;
    public int[][] fieldAnnotations;
    public int[][] methodAnnotations;
    public int[][] parameterAnnotations;

    public AnnotationsDirectory(int off, int classAnnotationsOffset, int[][] fieldAnnotations,
                                int[][] methodAnnotations, int[][] parameterAnnotations) {
        super(off);
        this.classAnnotationsOffset = classAnnotationsOffset;
        this.fieldAnnotations = fieldAnnotations;
        this.methodAnnotations = methodAnnotations;
        this.parameterAnnotations = parameterAnnotations;
    }

    public int compareTo(AnnotationsDirectory other) {
        if (this.classAnnotationsOffset != other.classAnnotationsOffset) {
            return CompareUtils.uCompare(this.classAnnotationsOffset, other.classAnnotationsOffset);
        }
        int fieldsSize = this.fieldAnnotations.length;
        int methodsSize = this.methodAnnotations.length;
        int parameterListSize = this.parameterAnnotations.length;
        int oFieldsSize = other.fieldAnnotations.length;
        int oMethodsSize = other.methodAnnotations.length;
        int oParameterListSize = other.parameterAnnotations.length;
        if (fieldsSize != oFieldsSize) {
            return CompareUtils.sCompare(fieldsSize, oFieldsSize);
        }
        if (methodsSize != oMethodsSize) {
            return CompareUtils.sCompare(methodsSize, oMethodsSize);
        }
        if (parameterListSize != oParameterListSize) {
            return CompareUtils.sCompare(parameterListSize, oParameterListSize);
        }
        int i;
        for (i = 0; i < fieldsSize; i++) {
            int fieldIdx = this.fieldAnnotations[i][0];
            int annotationOffset = this.fieldAnnotations[i][1];
            int othFieldIdx = other.fieldAnnotations[i][0];
            int othAnnotationOffset = other.fieldAnnotations[i][1];
            if (fieldIdx != othFieldIdx) {
                return CompareUtils.uCompare(fieldIdx, othFieldIdx);
            }
            if (annotationOffset != othAnnotationOffset) {
                return CompareUtils.sCompare(annotationOffset, othAnnotationOffset);
            }
        }
        for (i = 0; i < methodsSize; i++) {
            int methodIdx = this.methodAnnotations[i][0];
            annotationOffset = this.methodAnnotations[i][1];
            int othMethodIdx = other.methodAnnotations[i][0];
            othAnnotationOffset = other.methodAnnotations[i][1];
            if (methodIdx != othMethodIdx) {
                return CompareUtils.uCompare(methodIdx, othMethodIdx);
            }
            if (annotationOffset != othAnnotationOffset) {
                return CompareUtils.sCompare(annotationOffset, othAnnotationOffset);
            }
        }
        for (i = 0; i < parameterListSize; i++) {
            methodIdx = this.parameterAnnotations[i][0];
            annotationOffset = this.parameterAnnotations[i][1];
            othMethodIdx = other.parameterAnnotations[i][0];
            othAnnotationOffset = other.parameterAnnotations[i][1];
            if (methodIdx != othMethodIdx) {
                return CompareUtils.uCompare(methodIdx, othMethodIdx);
            }
            if (annotationOffset != othAnnotationOffset) {
                return CompareUtils.sCompare(annotationOffset, othAnnotationOffset);
            }
        }
        return 0;
    }

    public int byteCountInDex() {
        return ((((this.fieldAnnotations.length + this.methodAnnotations.length) + this
                .parameterAnnotations.length) * 2) + 4) * 4;
    }
}
