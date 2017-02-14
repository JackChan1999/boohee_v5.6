package org.eclipse.mat.snapshot;

public class SnapshotFormat {
    private String[] fileExtensions;
    private String name;

    public SnapshotFormat(String name, String[] fileExtensions) {
        this.fileExtensions = fileExtensions;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String[] getFileExtensions() {
        return this.fileExtensions;
    }
}
