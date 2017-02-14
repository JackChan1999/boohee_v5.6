package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

public class ASMClassLoader extends ClassLoader {
    private static ProtectionDomain DOMAIN = ((ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction<Object>() {
        public Object run() {
            return ASMClassLoader.class.getProtectionDomain();
        }
    }));

    public ASMClassLoader() {
        super(getParentClassLoader());
    }

    public ASMClassLoader(ClassLoader parent) {
        super(parent);
    }

    static ClassLoader getParentClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                contextClassLoader.loadClass(JSON.class.getName());
                return contextClassLoader;
            } catch (ClassNotFoundException e) {
            }
        }
        return JSON.class.getClassLoader();
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClass(name, b, off, len, DOMAIN);
    }

    public boolean isExternalClass(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader == null) {
            return false;
        }
        for (ClassLoader current = this; current != null; current = current.getParent()) {
            if (current == classLoader) {
                return false;
            }
        }
        return true;
    }
}
