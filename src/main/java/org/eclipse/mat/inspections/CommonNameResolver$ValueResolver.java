package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;

@Subjects({"java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Boolean"})
public class CommonNameResolver$ValueResolver implements IClassSpecificNameResolver {
    public String resolve(IObject heapObject) throws SnapshotException {
        return String.valueOf(heapObject.resolveValue("value"));
    }
}
