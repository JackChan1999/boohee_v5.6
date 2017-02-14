package com.squareup.leakcanary;

final class Preconditions {
    static <T> T checkNotNull(T instance, String name) {
        if (instance != null) {
            return instance;
        }
        throw new NullPointerException(name + " must not be null");
    }

    private Preconditions() {
        throw new AssertionError();
    }
}
