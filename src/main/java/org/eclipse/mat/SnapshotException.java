package org.eclipse.mat;

import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.util.MessageUtil;

public class SnapshotException extends Exception {
    private static final long serialVersionUID = 1;

    public SnapshotException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnapshotException(String message) {
        super(message);
    }

    public SnapshotException(Throwable cause) {
        super(cause);
    }

    public SnapshotException(Messages messages) {
        super(MessageUtil.format(messages, new Object[0]));
    }

    public static final SnapshotException rethrow(Throwable e) {
        if (e instanceof RuntimeException) {
            if (((RuntimeException) e).getCause() instanceof SnapshotException) {
                return (SnapshotException) ((RuntimeException) e).getCause();
            }
            throw ((RuntimeException) e);
        } else if (e instanceof SnapshotException) {
            return (SnapshotException) e;
        } else {
            return new SnapshotException(e);
        }
    }
}
