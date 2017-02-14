package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;

@Subject("java.lang.Thread")
public class CommonNameResolver$ThreadResolver implements IClassSpecificNameResolver {
    public String resolve(IObject obj) throws SnapshotException {
        IObject name = (IObject) obj.resolveValue("name");
        return name != null ? name.getClassSpecificName() : null;
    }
}
