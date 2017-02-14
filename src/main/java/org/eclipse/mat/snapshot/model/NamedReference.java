package org.eclipse.mat.snapshot.model;

import org.eclipse.mat.snapshot.ISnapshot;

public class NamedReference extends ObjectReference {
    private static final long serialVersionUID = 1;
    private String name;

    public NamedReference(ISnapshot snapshot, long address, String name) {
        super(snapshot, address);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
