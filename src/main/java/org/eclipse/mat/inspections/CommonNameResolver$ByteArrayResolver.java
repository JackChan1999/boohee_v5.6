package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;

@Subject("byte[]")
public class CommonNameResolver$ByteArrayResolver implements IClassSpecificNameResolver {
    public String resolve(IObject heapObject) throws SnapshotException {
        IPrimitiveArray arr = (IPrimitiveArray) heapObject;
        byte[] value = (byte[]) arr.getValueArray(0, Math.min(arr.getLength(), 1024));
        if (value == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(value.length);
        int i = 0;
        while (i < value.length) {
            if (value[i] < (byte) 32 || value[i] > (byte) 126) {
                r.append('.');
            } else {
                r.append((char) value[i]);
            }
            i++;
        }
        return r.toString();
    }
}
