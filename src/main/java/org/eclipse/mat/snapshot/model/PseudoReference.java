package org.eclipse.mat.snapshot.model;

import org.eclipse.mat.snapshot.ISnapshot;

public class PseudoReference extends NamedReference {
    private static final long serialVersionUID = 1;

    public PseudoReference(ISnapshot snapshot, long address, String name) {
        super(snapshot, address, name);
    }
}
