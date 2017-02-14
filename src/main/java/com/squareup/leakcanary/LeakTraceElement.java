package com.squareup.leakcanary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class LeakTraceElement implements Serializable {
    public final String       className;
    public final String       extra;
    public final List<String> fields;
    public final Holder       holder;
    public final String       referenceName;
    public final Type         type;

    public enum Holder {
        OBJECT,
        CLASS,
        THREAD,
        ARRAY
    }

    public enum Type {
        INSTANCE_FIELD,
        STATIC_FIELD,
        LOCAL
    }

    LeakTraceElement(String referenceName, Type type, Holder holder, String className, String
            extra, List<String> fields) {
        this.referenceName = referenceName;
        this.type = type;
        this.holder = holder;
        this.className = className;
        this.extra = extra;
        this.fields = Collections.unmodifiableList(new ArrayList(fields));
    }

    public String toString() {
        String string = "";
        if (this.type == Type.STATIC_FIELD) {
            string = string + "static ";
        }
        if (this.holder == Holder.ARRAY || this.holder == Holder.THREAD) {
            string = string + this.holder.name().toLowerCase(Locale.US) + " ";
        }
        string = string + this.className;
        if (this.referenceName != null) {
            string = string + "." + this.referenceName;
        } else {
            string = string + " instance";
        }
        if (this.extra != null) {
            return string + " " + this.extra;
        }
        return string;
    }

    public String toDetailedString() {
        String string = "* ";
        if (this.holder == Holder.ARRAY) {
            string = string + "Array of";
        } else if (this.holder == Holder.CLASS) {
            string = string + "Class";
        } else {
            string = string + "Instance of";
        }
        string = string + " " + this.className + "\n";
        for (String field : this.fields) {
            string = string + "|   " + field + "\n";
        }
        return string;
    }
}
