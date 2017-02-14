package org.eclipse.mat.inspections;

import java.lang.reflect.Modifier;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IObject;

@Subject("java.lang.reflect.AccessibleObject")
public class CommonNameResolver$AccessibleObjectResolver implements IClassSpecificNameResolver {
    public String resolve(IObject obj) throws SnapshotException {
        StringBuilder r = new StringBuilder();
        ISnapshot snapshot = obj.getSnapshot();
        Object val = obj.resolveValue("modifiers");
        if (val instanceof Integer) {
            r.append(Modifier.toString(((Integer) val).intValue()));
            if (r.length() > 0) {
                r.append(' ');
            }
        }
        IObject ref = (IObject) obj.resolveValue("clazz");
        if (ref == null) {
            return null;
        }
        addClassName(snapshot, ref.getObjectAddress(), r);
        return r.toString();
    }

    protected void addClassName(ISnapshot snapshot, long addr, StringBuilder r) throws SnapshotException {
        IObject ox = snapshot.getObject(snapshot.mapAddressToId(addr));
        if (ox instanceof IClass) {
            r.append(((IClass) ox).getName());
        }
    }
}
