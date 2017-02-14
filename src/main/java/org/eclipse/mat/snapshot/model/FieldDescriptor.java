package org.eclipse.mat.snapshot.model;

import java.io.Serializable;

public class FieldDescriptor implements Serializable {
    private static final long serialVersionUID = 2;
    protected String name;
    protected int type;

    public FieldDescriptor(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVerboseSignature() {
        if (this.type == 2) {
            return "ref";
        }
        String t = IPrimitiveArray.TYPE[this.type];
        return t.substring(0, t.length() - 2);
    }
}
