package org.eclipse.mat.parser.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayInt;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IThreadStack;

class ThreadStackHelper {
    ThreadStackHelper() {
    }

    static HashMapIntObject<IThreadStack> loadThreadsData(ISnapshot snapshot) throws SnapshotException {
        Throwable e;
        Throwable th;
        File f = new File(snapshot.getSnapshotInfo().getPrefix() + "threads");
        if (!f.exists()) {
            return null;
        }
        HashMapIntObject<IThreadStack> threadId2stack = new HashMapIntObject();
        BufferedReader in = null;
        try {
            BufferedReader in2 = new BufferedReader(new FileReader(f));
            try {
                for (String line = in2.readLine(); line != null; line = in2.readLine()) {
                    line = line.trim();
                    if (line.startsWith("Thread")) {
                        long threadAddress = readThreadAddres(line);
                        List<String> lines = new ArrayList();
                        HashMapIntObject<ArrayInt> line2locals = new HashMapIntObject();
                        line = in2.readLine();
                        while (line != null && !line.equals("")) {
                            lines.add(line.trim());
                            line = in2.readLine();
                        }
                        line = in2.readLine();
                        if (line != null && line.trim().startsWith("locals")) {
                            line = in2.readLine();
                            while (line != null && !line.equals("")) {
                                int lineNr = readLineNumber(line);
                                if (lineNr >= 0) {
                                    int objectId = readLocalId(line, snapshot);
                                    ArrayInt arr = (ArrayInt) line2locals.get(lineNr);
                                    if (arr == null) {
                                        arr = new ArrayInt();
                                        line2locals.put(lineNr, arr);
                                    }
                                    arr.add(objectId);
                                }
                                line = in2.readLine();
                            }
                        }
                        if (threadAddress != -1) {
                            int threadId = snapshot.mapAddressToId(threadAddress);
                            threadId2stack.put(threadId, new ThreadStackImpl(threadId, buildFrames(lines, line2locals)));
                        }
                    }
                    if (line == null) {
                        break;
                    }
                }
                if (in2 == null) {
                    return threadId2stack;
                }
                try {
                    in2.close();
                    return threadId2stack;
                } catch (Exception e2) {
                    return threadId2stack;
                }
            } catch (IOException e3) {
                e = e3;
                in = in2;
                try {
                    throw new SnapshotException(e);
                } catch (Throwable th2) {
                    th = th2;
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            throw new SnapshotException(e);
        }
    }

    private static long readThreadAddres(String line) {
        int start = line.indexOf("0x");
        if (start < 0) {
            return -1;
        }
        return new BigInteger(line.substring(start + 2), 16).longValue();
    }

    private static int readLocalId(String line, ISnapshot snapshot) throws SnapshotException {
        int start = line.indexOf("0x");
        return snapshot.mapAddressToId(new BigInteger(line.substring(start + 2, line.indexOf(44, start)), 16).longValue());
    }

    private static int readLineNumber(String line) {
        return Integer.valueOf(line.substring(line.indexOf("line=") + 5)).intValue();
    }

    private static StackFrameImpl[] buildFrames(List<String> lines, HashMapIntObject<ArrayInt> line2locals) {
        int sz = lines.size();
        StackFrameImpl[] frames = new StackFrameImpl[sz];
        for (int i = 0; i < sz; i++) {
            int[] localsIds = null;
            ArrayInt locals = (ArrayInt) line2locals.get(i);
            if (locals != null && locals.size() > 0) {
                localsIds = locals.toArray();
            }
            frames[i] = new StackFrameImpl((String) lines.get(i), localsIds);
        }
        return frames;
    }
}
