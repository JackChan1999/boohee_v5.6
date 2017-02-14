package org.eclipse.mat.snapshot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ExcludedReferencesDescriptor {
    private Set<String> fields;
    private int[] objectIds;

    public ExcludedReferencesDescriptor(int[] objectIds, Set<String> fields) {
        this.fields = fields;
        this.objectIds = objectIds;
        Arrays.sort(this.objectIds);
    }

    public ExcludedReferencesDescriptor(int[] objectIds, String... fields) {
        this(objectIds, new HashSet(Arrays.asList(fields)));
    }

    public Set<String> getFields() {
        return this.fields;
    }

    public boolean contains(int objectId) {
        return Arrays.binarySearch(this.objectIds, objectId) >= 0;
    }

    public int[] getObjectIds() {
        return this.objectIds;
    }
}
