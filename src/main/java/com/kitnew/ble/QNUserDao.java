package com.kitnew.ble;

import android.content.Context;

import com.kitnew.ble.utils.EncryptUtils;
import com.kitnew.ble.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class QNUserDao {
    static final String USER_FILE_SUFFIX = ".qnu";
    Context context;

    public QNUserDao(Context context) {
        this.context = context;
    }

    public void saveUser(QNUser user) {
        FileUtils.writeStringToFileWithEncrypt(user.formatString(), generateFile(user.id));
    }

    public void deleteUser(String userId) {
        String filename = generateFile(userId);
        if (filename != null) {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public QNUser getById(String userId) {
        return QNUser.newInstance(FileUtils.getStringFromFileWithDecrypt(generateFile(userId)));
    }

    List<QNUser> getAllUser() {
        File[] targetFiles = new File(FileUtils.getDirectPath(this.context)).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(QNUserDao.USER_FILE_SUFFIX);
            }
        });
        List<QNUser> users = new ArrayList();
        if (targetFiles != null) {
            for (File file : targetFiles) {
                String userString = FileUtils.getStringFromFileWithDecrypt(file);
                if (QNUser.newInstance(userString) != null) {
                    users.add(QNUser.newInstance(userString));
                }
            }
        }
        return users;
    }

    String generateFile(String userId) {
        return FileUtils.getDirectPath(this.context) + EncryptUtils.encrypt(userId) +
                USER_FILE_SUFFIX;
    }
}
