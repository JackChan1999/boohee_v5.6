package org.eclipse.mat.inspections;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.extension.IClassSpecificNameResolver;
import org.eclipse.mat.snapshot.extension.Subject;
import org.eclipse.mat.snapshot.model.IObject;

@Subject("java.net.URL")
public class CommonNameResolver$URLResolver implements IClassSpecificNameResolver {
    public String resolve(IObject obj) throws SnapshotException {
        StringBuilder builder = new StringBuilder();
        builder.append(((IObject) obj.resolveValue("protocol")).getClassSpecificName());
        builder.append(":");
        IObject authority = (IObject) obj.resolveValue("authority");
        if (authority != null) {
            builder.append("//");
            builder.append(authority.getClassSpecificName());
        }
        IObject path = (IObject) obj.resolveValue("path");
        if (path != null) {
            builder.append(path.getClassSpecificName());
        }
        IObject query = (IObject) obj.resolveValue("query");
        if (query != null) {
            builder.append("?");
            builder.append(query.getClassSpecificName());
        }
        IObject ref = (IObject) obj.resolveValue("ref");
        if (ref != null) {
            builder.append("#");
            builder.append(ref.getClassSpecificName());
        }
        return builder.toString();
    }
}
