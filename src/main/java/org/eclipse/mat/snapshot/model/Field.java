package org.eclipse.mat.snapshot.model;

import java.io.Serializable;

public final class Field extends FieldDescriptor implements Serializable {
    private static final long serialVersionUID = 2;
    protected Object value;

    public Field(String name, int type, Object value) {
        super(name, type);
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object object) {
        this.value = object;
    }

    public String toString() {
        return this.type + " " + this.name + ": \t" + this.value;
    }
}
