package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.PrettyPrinter;

@Subject("java.lang.String")
public class CommonNameResolver$StringResolver implements IClassSpecificNameResolver {
    public String resolve(IObject obj) throws SnapshotException {
        return PrettyPrinter.objectAsString(obj, 1024);
    }
}
