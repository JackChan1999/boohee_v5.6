package com.squareup.leakcanary;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class ExcludedRefs implements Serializable {
    public final Map<String, Set<String>> excludeFieldMap;
    public final Map<String, Set<String>> excludeStaticFieldMap;
    public final Set<String>              excludedThreads;

    public static final class Builder {
        private final Map<String, Set<String>> excludeFieldMap       = new LinkedHashMap();
        private final Map<String, Set<String>> excludeStaticFieldMap = new LinkedHashMap();
        private final Set<String>              excludedThreads       = new LinkedHashSet();

        public Builder instanceField(String className, String fieldName) {
            Preconditions.checkNotNull(className, "className");
            Preconditions.checkNotNull(fieldName, "fieldName");
            Set<String> excludedFields = (Set) this.excludeFieldMap.get(className);
            if (excludedFields == null) {
                excludedFields = new LinkedHashSet();
                this.excludeFieldMap.put(className, excludedFields);
            }
            excludedFields.add(fieldName);
            return this;
        }

        public Builder staticField(String className, String fieldName) {
            Preconditions.checkNotNull(className, "className");
            Preconditions.checkNotNull(fieldName, "fieldName");
            Set<String> excludedFields = (Set) this.excludeStaticFieldMap.get(className);
            if (excludedFields == null) {
                excludedFields = new LinkedHashSet();
                this.excludeStaticFieldMap.put(className, excludedFields);
            }
            excludedFields.add(fieldName);
            return this;
        }

        public Builder thread(String threadName) {
            Preconditions.checkNotNull(threadName, "threadName");
            this.excludedThreads.add(threadName);
            return this;
        }

        public ExcludedRefs build() {
            return new ExcludedRefs(this.excludeFieldMap, this.excludeStaticFieldMap, this
                    .excludedThreads);
        }
    }

    private ExcludedRefs(Map<String, Set<String>> excludeFieldMap, Map<String, Set<String>>
            excludeStaticFieldMap, Set<String> excludedThreads) {
        this.excludeFieldMap = Collections.unmodifiableMap(new LinkedHashMap(excludeFieldMap));
        this.excludeStaticFieldMap = Collections.unmodifiableMap(new LinkedHashMap
                (excludeStaticFieldMap));
        this.excludedThreads = Collections.unmodifiableSet(new LinkedHashSet(excludedThreads));
    }
}
