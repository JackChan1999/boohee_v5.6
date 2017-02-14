package org.eclipse.mat.snapshot.model;

import org.eclipse.mat.SnapshotException;

public final class PrettyPrinter {
    public static String objectAsString(IObject stringObject, int limit) throws SnapshotException {
        Integer count = (Integer) stringObject.resolveValue("count");
        if (count == null) {
            return null;
        }
        if (count.intValue() == 0) {
            return "";
        }
        IPrimitiveArray charArray = (IPrimitiveArray) stringObject.resolveValue("value");
        if (charArray == null) {
            return null;
        }
        Integer offset = (Integer) stringObject.resolveValue("offset");
        if (offset != null) {
            return arrayAsString(charArray, offset.intValue(), count.intValue(), limit);
        }
        return null;
    }

    public static String arrayAsString(IPrimitiveArray charArray, int offset, int count, int limit) {
        if (charArray.getType() != 5) {
            return null;
        }
        int contentToRead;
        char[] value;
        int length = charArray.getLength();
        if (count <= limit) {
            contentToRead = count;
        } else {
            contentToRead = limit;
        }
        if (contentToRead > length - offset) {
            contentToRead = length - offset;
        }
        if (offset == 0 && length == contentToRead) {
            value = (char[]) charArray.getValueArray();
        } else {
            value = (char[]) charArray.getValueArray(offset, contentToRead);
        }
        if (value == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(value.length);
        for (char val : value) {
            if (val < ' ' || val >= '') {
                result.append("\\u").append(String.format("%04x", new Object[]{Integer.valueOf(65535 & val)}));
            } else {
                result.append(val);
            }
        }
        if (limit < count) {
            result.append("...");
        }
        return result.toString();
    }

    private PrettyPrinter() {
    }
}
