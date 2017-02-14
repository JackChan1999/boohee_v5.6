package org.eclipse.mat.inspections;

import java.lang.reflect.Modifier;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;

@Subject("java.lang.reflect.Constructor")
public class CommonNameResolver$ConstructorResolver extends CommonNameResolver$AccessibleObjectResolver {
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
        r.append('(');
        ref = (IObject) obj.resolveValue("parameterTypes");
        if (ref instanceof IObjectArray) {
            IObjectArray orefa = (IObjectArray) ref;
            long[] refs = orefa.getReferenceArray();
            for (int i = 0; i < orefa.getLength(); i++) {
                if (i > 0) {
                    r.append(',');
                }
                addClassName(snapshot, refs[i], r);
            }
        }
        r.append(')');
        return r.toString();
    }
}
