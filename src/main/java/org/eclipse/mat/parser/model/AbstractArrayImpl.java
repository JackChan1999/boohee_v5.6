package org.eclipse.mat.parser.model;

import org.eclipse.mat.snapshot.model.IArray;

public abstract class AbstractArrayImpl extends AbstractObjectImpl implements IArray {
    private static final long serialVersionUID = 2;
    private Object info;
    protected int length;

    public AbstractArrayImpl(int objectId, long address, ClassImpl classInstance, int length) {
        super(objectId, address, classInstance);
        this.length = length;
    }

    public Object getInfo() {
        return this.info;
    }

    public void setInfo(Object content) {
        this.info = content;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int i) {
        this.length = i;
    }

    protected StringBuffer appendFields(StringBuffer buf) {
        return super.appendFields(buf).append(";length=").append(this.length);
    }

    public String getTechnicalName() {
        StringBuilder builder = new StringBuilder(256);
        String name = getClazz().getName();
        int p = name.indexOf(91);
        if (p < 0) {
            builder.append(name);
        } else {
            builder.append(name.subSequence(0, p + 1)).append(getLength()).append(name.substring(p + 1));
        }
        builder.append(" @ 0x");
        builder.append(Long.toHexString(getObjectAddress()));
        return builder.toString();
    }
}
