package org.apache.http.entity.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Header implements Iterable<MinimalField> {
    private final Map<String, List<MinimalField>> fieldMap = new HashMap();
    private final List<MinimalField> fields = new LinkedList();

    public void addField(MinimalField field) {
        if (field != null) {
            String key = field.getName().toLowerCase(Locale.US);
            List<MinimalField> values = (List) this.fieldMap.get(key);
            if (values == null) {
                values = new LinkedList();
                this.fieldMap.put(key, values);
            }
            values.add(field);
            this.fields.add(field);
        }
    }

    public List<MinimalField> getFields() {
        return new ArrayList(this.fields);
    }

    public MinimalField getField(String name) {
        if (name == null) {
            return null;
        }
        List<MinimalField> list = (List) this.fieldMap.get(name.toLowerCase(Locale.US));
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (MinimalField) list.get(0);
    }

    public List<MinimalField> getFields(String name) {
        if (name == null) {
            return null;
        }
        List<MinimalField> list = (List) this.fieldMap.get(name.toLowerCase(Locale.US));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList(list);
    }

    public int removeFields(String name) {
        if (name == null) {
            return 0;
        }
        List<MinimalField> removed = (List) this.fieldMap.remove(name.toLowerCase(Locale.US));
        if (removed == null || removed.isEmpty()) {
            return 0;
        }
        this.fields.removeAll(removed);
        return removed.size();
    }

    public void setField(MinimalField field) {
        if (field != null) {
            List<MinimalField> list = (List) this.fieldMap.get(field.getName().toLowerCase(Locale.US));
            if (list == null || list.isEmpty()) {
                addField(field);
                return;
            }
            list.clear();
            list.add(field);
            int firstOccurrence = -1;
            int index = 0;
            Iterator<MinimalField> it = this.fields.iterator();
            while (it.hasNext()) {
                if (((MinimalField) it.next()).getName().equalsIgnoreCase(field.getName())) {
                    it.remove();
                    if (firstOccurrence == -1) {
                        firstOccurrence = index;
                    }
                }
                index++;
            }
            this.fields.add(firstOccurrence, field);
        }
    }

    public Iterator<MinimalField> iterator() {
        return Collections.unmodifiableList(this.fields).iterator();
    }

    public String toString() {
        return this.fields.toString();
    }
}
