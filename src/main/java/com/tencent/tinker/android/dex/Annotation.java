package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;

public final class Annotation extends Item<Annotation> {
    public EncodedValue encodedAnnotation;
    public byte         visibility;

    public Annotation(int off, byte visibility, EncodedValue encodedAnnotation) {
        super(off);
        this.visibility = visibility;
        this.encodedAnnotation = encodedAnnotation;
    }

    public EncodedValueReader getReader() {
        return new EncodedValueReader(this.encodedAnnotation, 29);
    }

    public int getTypeIndex() {
        EncodedValueReader reader = getReader();
        reader.readAnnotation();
        return reader.getAnnotationType();
    }

    public int compareTo(Annotation other) {
        return this.encodedAnnotation.compareTo(other.encodedAnnotation);
    }

    public int byteCountInDex() {
        return this.encodedAnnotation.byteCountInDex() + 1;
    }
}
