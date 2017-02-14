package com.squareup.leakcanary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LeakTrace implements Serializable {
    public final List<LeakTraceElement> elements;

    LeakTrace(List<LeakTraceElement> elements) {
        this.elements = Collections.unmodifiableList(new ArrayList(elements));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.elements.size(); i++) {
            LeakTraceElement element = (LeakTraceElement) this.elements.get(i);
            sb.append("* ");
            if (i == 0) {
                sb.append("GC ROOT ");
            } else if (i == this.elements.size() - 1) {
                sb.append("leaks ");
            } else {
                sb.append("references ");
            }
            sb.append(element).append("\n");
        }
        return sb.toString();
    }

    public String toDetailedString() {
        String string = "";
        for (LeakTraceElement element : this.elements) {
            string = string + element.toDetailedString();
        }
        return string;
    }
}
