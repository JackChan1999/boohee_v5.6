package com.meiqia.meiqiasdk.model;

import java.util.ArrayList;

public class ImageFolderModel {
    public String coverPath;
    private ArrayList<String> mImages = new ArrayList();
    private boolean mTakePhotoEnabled;
    public  String  name;

    public ImageFolderModel(boolean takePhotoEnabled) {
        this.mTakePhotoEnabled = takePhotoEnabled;
        if (takePhotoEnabled) {
            this.mImages.add("");
        }
    }

    public ImageFolderModel(String name, String coverPath) {
        this.name = name;
        this.coverPath = coverPath;
    }

    public boolean isTakePhotoEnabled() {
        return this.mTakePhotoEnabled;
    }

    public void addLastImage(String imagePath) {
        this.mImages.add(imagePath);
    }

    public ArrayList<String> getImages() {
        return this.mImages;
    }

    public int getCount() {
        return this.mImages.size();
    }
}
