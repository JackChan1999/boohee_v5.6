package org.eclipse.mat.hprof;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.hprof.extension.IRuntimeEnhancer;
import org.eclipse.mat.parser.IObjectReader;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IndexReader.LongIndexReader;
import org.eclipse.mat.parser.model.AbstractArrayImpl;
import org.eclipse.mat.parser.model.ObjectArrayImpl;
import org.eclipse.mat.parser.model.PrimitiveArrayImpl;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;

public class HprofHeapObjectReader implements IObjectReader {
    public static final String VERSION_PROPERTY = "hprof.version";
    private List<IRuntimeEnhancer> enhancers;
    private HprofRandomAccessParser hprofDump;
    private IOne2LongIndex o2hprof;
    private ISnapshot snapshot;

    public void open(ISnapshot snapshot) throws IOException {
        this.snapshot = snapshot;
        this.hprofDump = new HprofRandomAccessParser(new File(snapshot.getSnapshotInfo().getPath()), Version.valueOf((String) snapshot.getSnapshotInfo().getProperty(VERSION_PROPERTY)), snapshot.getSnapshotInfo().getIdentifierSize());
        this.o2hprof = new LongIndexReader(new File(snapshot.getSnapshotInfo().getPrefix() + "o2hprof.index"));
        this.enhancers = new ArrayList();
    }

    public long[] readObjectArrayContent(ObjectArrayImpl array, int offset, int length) throws IOException, SnapshotException {
        Offline info = array.getInfo();
        if (info instanceof Offline) {
            Offline description = info;
            long[] answer = (long[]) description.getLazyReadContent();
            if (answer != null) {
                return (long[]) fragment(array, answer, offset, length);
            }
            answer = this.hprofDump.readObjectArray(description, offset, length);
            if (offset == 0 && length == array.getLength()) {
                description.setLazyReadContent(answer);
            }
            return answer;
        } else if (info instanceof long[]) {
            return (long[]) fragment(array, info, offset, length);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Object readPrimitiveArrayContent(PrimitiveArrayImpl array, int offset, int length) throws IOException, SnapshotException {
        ArrayDescription info = array.getInfo();
        Object content;
        if (info instanceof Offline) {
            Offline description = (Offline) info;
            content = description.getLazyReadContent();
            if (content != null) {
                return fragment(array, content, offset, length);
            }
            content = convert(array, this.hprofDump.readPrimitiveArray(description, offset, length));
            if (offset != 0 || length != array.getLength()) {
                return content;
            }
            description.setLazyReadContent(content);
            return content;
        } else if (!(info instanceof Raw)) {
            return fragment(array, info, offset, length);
        } else {
            content = convert(array, ((Raw) info).getContent());
            array.setInfo(content);
            return fragment(array, content, offset, length);
        }
    }

    private Object convert(PrimitiveArrayImpl array, byte[] content) {
        if (array.getType() == 8) {
            return content;
        }
        int elementSize = IPrimitiveArray.ELEMENT_SIZE[array.getType()];
        Object answer = Array.newInstance(IPrimitiveArray.COMPONENT_TYPE[array.getType()], content.length / elementSize);
        int index = 0;
        for (int ii = 0; ii < content.length; ii += elementSize) {
            switch (array.getType()) {
                case 4:
                    Array.set(answer, index, Boolean.valueOf(content[ii] != (byte) 0));
                    break;
                case 5:
                    Array.set(answer, index, Character.valueOf(readChar(content, ii)));
                    break;
                case 6:
                    Array.set(answer, index, Float.valueOf(readFloat(content, ii)));
                    break;
                case 7:
                    Array.set(answer, index, Double.valueOf(readDouble(content, ii)));
                    break;
                case 9:
                    Array.set(answer, index, Short.valueOf(readShort(content, ii)));
                    break;
                case 10:
                    Array.set(answer, index, Integer.valueOf(readInt(content, ii)));
                    break;
                case 11:
                    Array.set(answer, index, Long.valueOf(readLong(content, ii)));
                    break;
                default:
                    break;
            }
            index++;
        }
        return answer;
    }

    private Object fragment(AbstractArrayImpl array, Object content, int offset, int length) {
        if (offset == 0 && length == array.getLength()) {
            return content;
        }
        Object answer = Array.newInstance(content.getClass().getComponentType(), length);
        System.arraycopy(content, offset, answer, 0, length);
        return answer;
    }

    public IObject read(int objectId, ISnapshot snapshot) throws SnapshotException, IOException {
        return this.hprofDump.read(objectId, this.o2hprof.get(objectId), snapshot);
    }

    public <A> A getAddon(Class<A> addon) throws SnapshotException {
        for (IRuntimeEnhancer enhancer : this.enhancers) {
            A answer = enhancer.getAddon(this.snapshot, addon);
            if (answer != null) {
                return answer;
            }
        }
        return null;
    }

    public void close() throws IOException {
        try {
            this.hprofDump.close();
        } catch (IOException e) {
        }
        try {
            this.o2hprof.close();
        } catch (IOException e2) {
        }
    }

    private short readShort(byte[] data, int offset) {
        return (short) (((data[offset] & 255) << 8) + (data[offset + 1] & 255));
    }

    private char readChar(byte[] data, int offset) {
        return (char) (((data[offset] & 255) << 8) + (data[offset + 1] & 255));
    }

    private int readInt(byte[] data, int offset) {
        return ((((data[offset] & 255) << 24) + ((data[offset + 1] & 255) << 16)) + ((data[offset + 2] & 255) << 8)) + ((data[offset + 3] & 255) << 0);
    }

    private float readFloat(byte[] data, int offset) {
        return Float.intBitsToFloat(readInt(data, offset));
    }

    private long readLong(byte[] data, int offset) {
        return ((((((((((long) data[offset]) & 255) << 56) + (((long) (data[offset + 1] & 255)) << 48)) + (((long) (data[offset + 2] & 255)) << 40)) + (((long) (data[offset + 3] & 255)) << 32)) + (((long) (data[offset + 4] & 255)) << 24)) + ((long) ((data[offset + 5] & 255) << 16))) + ((long) ((data[offset + 6] & 255) << 8))) + ((long) ((data[offset + 7] & 255) << 0));
    }

    private double readDouble(byte[] data, int offset) {
        return Double.longBitsToDouble(readLong(data, offset));
    }
}
