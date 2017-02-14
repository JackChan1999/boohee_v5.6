package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.snapshot.model.PrettyPrinter;

@Subject("char[]")
public class CommonNameResolver$CharArrayResolver implements IClassSpecificNameResolver {
    public String resolve(IObject heapObject) throws SnapshotException {
        IPrimitiveArray charArray = (IPrimitiveArray) heapObject;
        return PrettyPrinter.arrayAsString(charArray, 0, charArray.getLength(), 1024);
    }
}
