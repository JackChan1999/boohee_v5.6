package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;

@Subject("java.lang.ThreadGroup")
public class CommonNameResolver$ThreadGroupResolver implements IClassSpecificNameResolver {
    public String resolve(IObject object) throws SnapshotException {
        IObject nameString = (IObject) object.resolveValue("name");
        if (nameString == null) {
            return null;
        }
        return nameString.getClassSpecificName();
    }
}
