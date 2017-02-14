package de.greenrobot.event;

import java.lang.reflect.Method;

final class SubscriberMethod {
    final Class<?> eventType;
    final Method method;
    String methodString;
    final ThreadMode threadMode;

    SubscriberMethod(Method method, ThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public boolean equals(Object other) {
        if (!(other instanceof SubscriberMethod)) {
            return false;
        }
        checkMethodString();
        SubscriberMethod otherSubscriberMethod = (SubscriberMethod) other;
        otherSubscriberMethod.checkMethodString();
        return this.methodString.equals(otherSubscriberMethod.methodString);
    }

    private synchronized void checkMethodString() {
        if (this.methodString == null) {
            StringBuilder builder = new StringBuilder(64);
            builder.append(this.method.getDeclaringClass().getName());
            builder.append('#').append(this.method.getName());
            builder.append('(').append(this.eventType.getName());
            this.methodString = builder.toString();
        }
    }

    public int hashCode() {
        return this.method.hashCode();
    }
}
