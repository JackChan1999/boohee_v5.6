package org.eclipse.mat.parser.index;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyObjectsIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2OneIndex;
import org.eclipse.mat.parser.index.IndexReader.IntIndex1NSortedReader;
import org.eclipse.mat.parser.index.IndexReader.IntIndexReader;
import org.eclipse.mat.parser.index.IndexReader.LongIndexReader;

public class IndexManager {
    public IOne2OneIndex a2s;
    public IOne2OneIndex domIn;
    public IOne2ManyIndex domOut;
    public IOne2LongIndex idx;
    public IOne2ManyObjectsIndex inbound;
    public IOne2OneIndex o2c;
    public IOne2LongIndex o2ret;
    public IOne2ManyIndex outbound;

    private abstract class Visitor {
        abstract void visit(Index index, IIndexReader iIndexReader) throws IOException;

        private Visitor() {
        }

        void doIt() throws IOException {
            try {
                for (Index index : Index.values()) {
                    visit(index, IndexManager.this.getReader(index));
                }
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    public enum Index {
        INBOUND("inbound", InboundReader.class),
        OUTBOUND("outbound", IntIndex1NSortedReader.class),
        O2CLASS("o2c", IntIndexReader.class),
        IDENTIFIER("idx", LongIndexReader.class),
        A2SIZE("a2s", IntIndexReader.class),
        DOMINATED("domOut", IntIndex1NReader.class),
        O2RETAINED("o2ret", LongIndexReader.class),
        DOMINATOR("domIn", IntIndexReader.class);
        
        public String filename;
        Class<? extends IIndexReader> impl;

        private Index(String filename, Class<? extends IIndexReader> impl) {
            this.filename = filename;
            this.impl = impl;
        }

        public File getFile(String prefix) {
            return new File(this.filename + ".index");
        }
    }

    public void setReader(Index index, IIndexReader reader) {
        try {
            getClass().getField(index.filename).set(this, reader);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public IIndexReader getReader(Index index) {
        try {
            return (IIndexReader) getClass().getField(index.filename).get(this);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public void init(final String prefix) throws IOException {
        new Visitor() {
            void visit(Index index, IIndexReader reader) throws IOException {
                if (reader == null) {
                    try {
                        if (index.getFile(prefix).exists()) {
                            IndexManager.this.setReader(index, (IIndexReader) index.impl.getConstructor(new Class[]{File.class}).newInstance(new Object[]{indexFile}));
                        }
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e2) {
                        throw new RuntimeException(e2);
                    } catch (IllegalAccessException e3) {
                        throw new RuntimeException(e3);
                    } catch (InvocationTargetException e4) {
                        throw new IOException(MessageFormat.format("{0}: {1}", new Object[]{cause.getClass().getName(), e4.getCause().getMessage()}), e4.getCause());
                    } catch (RuntimeException e5) {
                        throw new IOException(e5);
                    }
                }
            }
        }.doIt();
    }

    public IOne2ManyIndex inbound() {
        return this.inbound;
    }

    public IOne2ManyIndex outbound() {
        return this.outbound;
    }

    public IOne2OneIndex o2class() {
        return this.o2c;
    }

    public IOne2ManyObjectsIndex c2objects() {
        return this.inbound;
    }

    public IOne2LongIndex o2address() {
        return this.idx;
    }

    public IOne2OneIndex a2size() {
        return this.a2s;
    }

    public IOne2ManyIndex dominated() {
        return this.domOut;
    }

    public IOne2LongIndex o2retained() {
        return this.o2ret;
    }

    public IOne2OneIndex dominator() {
        return this.domIn;
    }

    public void close() throws IOException {
        new Visitor() {
            void visit(Index index, IIndexReader reader) throws IOException {
                if (reader != null) {
                    reader.close();
                    IndexManager.this.setReader(index, null);
                }
            }
        }.doIt();
    }

    public void delete() throws IOException {
        new Visitor() {
            void visit(Index index, IIndexReader reader) throws IOException {
                if (reader != null) {
                    reader.close();
                    reader.delete();
                    IndexManager.this.setReader(index, null);
                }
            }
        }.doIt();
    }
}
