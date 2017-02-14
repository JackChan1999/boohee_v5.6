package com.aspsine.multithreaddownload;

import java.io.File;

public class DownloadRequest {
    private CharSequence mDescription;
    private File mFolder;
    private boolean mScannable;
    private CharSequence mTitle;
    private String mUri;

    public static class Builder {
        private CharSequence mDescription;
        private File mFolder;
        private boolean mScannable;
        private CharSequence mTitle;
        private String mUri;

        public Builder setUri(String uri) {
            this.mUri = uri;
            return this;
        }

        public Builder setFolder(File folder) {
            this.mFolder = folder;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public Builder setScannable(boolean scannable) {
            this.mScannable = scannable;
            return this;
        }

        public DownloadRequest build() {
            return new DownloadRequest(this.mUri, this.mFolder, this.mTitle, this.mDescription, this.mScannable);
        }
    }

    private DownloadRequest() {
    }

    private DownloadRequest(String uri, File folder, CharSequence title, CharSequence description, boolean scannable) {
        this.mUri = uri;
        this.mFolder = folder;
        this.mTitle = title;
        this.mDescription = description;
        this.mScannable = scannable;
    }

    public String getUri() {
        return this.mUri;
    }

    public File getFolder() {
        return this.mFolder;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getDescription() {
        return this.mDescription;
    }

    public boolean isScannable() {
        return this.mScannable;
    }
}
