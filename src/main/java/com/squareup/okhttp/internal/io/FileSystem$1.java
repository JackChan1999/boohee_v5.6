package com.squareup.okhttp.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okio.Okio;
import okio.Sink;
import okio.Source;

class FileSystem$1 implements FileSystem {
    FileSystem$1() {
    }

    public Source source(File file) throws FileNotFoundException {
        return Okio.source(file);
    }

    public Sink sink(File file) throws FileNotFoundException {
        try {
            return Okio.sink(file);
        } catch (FileNotFoundException e) {
            file.getParentFile().mkdirs();
            return Okio.sink(file);
        }
    }

    public Sink appendingSink(File file) throws FileNotFoundException {
        try {
            return Okio.appendingSink(file);
        } catch (FileNotFoundException e) {
            file.getParentFile().mkdirs();
            return Okio.appendingSink(file);
        }
    }

    public void delete(File file) throws IOException {
        if (!file.delete() && file.exists()) {
            throw new IOException("failed to delete " + file);
        }
    }

    public boolean exists(File file) throws IOException {
        return file.exists();
    }

    public long size(File file) {
        return file.length();
    }

    public void rename(File from, File to) throws IOException {
        delete(to);
        if (!from.renameTo(to)) {
            throw new IOException("failed to rename " + from + " to " + to);
        }
    }

    public void deleteContents(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + directory);
        }
        int length = files.length;
        int i = 0;
        while (i < length) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (file.delete()) {
                i++;
            } else {
                throw new IOException("failed to delete " + file);
            }
        }
    }
}
