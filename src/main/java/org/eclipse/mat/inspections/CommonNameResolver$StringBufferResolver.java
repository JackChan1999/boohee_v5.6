package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subjects;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.snapshot.model.PrettyPrinter;

@Subjects({"java.lang.StringBuffer", "java.lang.StringBuilder"})
public class CommonNameResolver$StringBufferResolver implements IClassSpecificNameResolver {
    public String resolve(IObject obj) throws SnapshotException {
        Integer count = (Integer) obj.resolveValue("count");
        if (count == null) {
            return null;
        }
        if (count.intValue() == 0) {
            return "";
        }
        IPrimitiveArray charArray = (IPrimitiveArray) obj.resolveValue("value");
        if (charArray != null) {
            return PrettyPrinter.arrayAsString(charArray, 0, count.intValue(), 1024);
        }
        return null;
    }
}
