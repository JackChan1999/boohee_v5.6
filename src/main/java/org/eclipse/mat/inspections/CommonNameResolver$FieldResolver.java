package org.eclipse.mat.inspections;

import java.lang.reflect.Modifier;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;

@Subject("java.lang.reflect.Field")
public class CommonNameResolver$FieldResolver extends CommonNameResolver$AccessibleObjectResolver {
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
        IObject ref = (IObject) obj.resolveValue("type");
        if (ref != null) {
            addClassName(snapshot, ref.getObjectAddress(), r);
            r.append(' ');
        }
        ref = (IObject) obj.resolveValue("clazz");
        if (ref != null) {
            addClassName(snapshot, ref.getObjectAddress(), r);
            r.append('.');
        }
        ref = (IObject) obj.resolveValue("name");
        if (ref == null) {
            return null;
        }
        r.append(ref.getClassSpecificName());
        return r.toString();
    }
}
