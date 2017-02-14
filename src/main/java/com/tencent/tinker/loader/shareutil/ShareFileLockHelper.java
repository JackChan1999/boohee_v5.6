package com.tencent.tinker.loader.shareutil;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

public class ShareFileLockHelper implements Closeable {
    public static final  int    LOCK_WAIT_EACH_TIME = 10;
    public static final  int    MAX_LOCK_ATTEMPTS   = 3;
    private static final String TAG                 = "Tinker.FileLockHelper";
    private final FileLock         fileLock;
    private final FileOutputStream outputStream;

    public void close() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0012 in
list [B:6:0x000d]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r2 = this;
        r0 = r2.fileLock;	 Catch:{ all -> 0x0013 }
        if (r0 == 0) goto L_0x0009;	 Catch:{ all -> 0x0013 }
    L_0x0004:
        r0 = r2.fileLock;	 Catch:{ all -> 0x0013 }
        r0.release();	 Catch:{ all -> 0x0013 }
    L_0x0009:
        r0 = r2.outputStream;
        if (r0 == 0) goto L_0x0012;
    L_0x000d:
        r0 = r2.outputStream;
        r0.close();
    L_0x0012:
        return;
    L_0x0013:
        r0 = move-exception;
        r1 = r2.outputStream;
        if (r1 == 0) goto L_0x001d;
    L_0x0018:
        r1 = r2.outputStream;
        r1.close();
    L_0x001d:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.tinker.loader" +
                ".shareutil.ShareFileLockHelper.close():void");
    }

    private ShareFileLockHelper(File lockFile) throws IOException {
        this.outputStream = new FileOutputStream(lockFile);
        int numAttempts = 0;
        FileLock localFileLock = null;
        Exception saveException = null;
        while (numAttempts < 3) {
            numAttempts++;
            try {
                localFileLock = this.outputStream.getChannel().lock();
                if (localFileLock != null) {
                    break;
                }
                Thread.sleep(10);
            } catch (Exception e) {
                saveException = e;
                Log.e(TAG, "getInfoLock Thread failed time:10");
            }
        }
        if (localFileLock == null) {
            throw new IOException("Tinker Exception:FileLockHelper lock file failed: " + lockFile
                    .getAbsolutePath(), saveException);
        }
        this.fileLock = localFileLock;
    }

    public static ShareFileLockHelper getFileLock(File lockFile) throws IOException {
        return new ShareFileLockHelper(lockFile);
    }
}
