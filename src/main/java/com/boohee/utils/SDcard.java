package com.boohee.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class SDcard {
    public static final String BOOHEE_DIR   = "boohee";
    public static final String CACHE_DIR    = "cache";
    public static final String DOWNLOAD_DIR = "Download";
    public static final String IMAGES_DIR   = "images";
    static final        String TAG          = SDcard.class.getSimpleName();
    public static final String TEMP_IMAGE   = "temp.jpg";

    public static File getBooheeDir() {
        if (!hasSdcard()) {
            return null;
        }
        File booheeDir = new File(Environment.getExternalStorageDirectory(), BOOHEE_DIR);
        if (booheeDir.exists()) {
            return booheeDir;
        }
        booheeDir.mkdir();
        return booheeDir;
    }

    public static File getImagesDir() {
        File booheeDir = getBooheeDir();
        if (booheeDir == null) {
            return null;
        }
        File imagesDir = new File(booheeDir, IMAGES_DIR);
        if (imagesDir.exists()) {
            return imagesDir;
        }
        imagesDir.mkdir();
        return imagesDir;
    }

    public static File getCacheDir() {
        File booheeDir = getBooheeDir();
        if (booheeDir == null) {
            return null;
        }
        File cacheDir = new File(booheeDir, CACHE_DIR);
        if (cacheDir.exists()) {
            return cacheDir;
        }
        cacheDir.mkdir();
        return cacheDir;
    }

    public static File getTempImage() {
        File booheeDir = getBooheeDir();
        if (booheeDir == null) {
            return null;
        }
        File tempImage = new File(booheeDir, TEMP_IMAGE);
        if (tempImage.exists()) {
            return tempImage;
        }
        try {
            tempImage.createNewFile();
            return tempImage;
        } catch (IOException e) {
            e.printStackTrace();
            return tempImage;
        }
    }

    public static File getDownloadDir() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        File dir = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_DIR);
        if (dir.exists()) {
            return dir;
        }
        dir.mkdir();
        return dir;
    }

    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals("mounted");
    }
}
