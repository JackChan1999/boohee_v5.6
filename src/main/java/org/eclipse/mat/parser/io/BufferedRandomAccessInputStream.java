package org.eclipse.mat.parser.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.SoftReference;
import org.eclipse.mat.collect.HashMapLongObject;

public class BufferedRandomAccessInputStream extends InputStream {
    int bufsize;
    Page current;
    long fileLength;
    HashMapLongObject<SoftReference<Page>> pages;
    RandomAccessFile raf;
    long real_pos;
    long reported_pos;

    private class Page {
        int buf_end;
        byte[] buffer;
        long real_pos_start;

        public Page() {
            this.buffer = new byte[BufferedRandomAccessInputStream.this.bufsize];
        }
    }

    public BufferedRandomAccessInputStream(RandomAccessFile in) throws IOException {
        this(in, 1024);
    }

    public BufferedRandomAccessInputStream(RandomAccessFile in, int bufsize) throws IOException {
        this.pages = new HashMapLongObject();
        this.bufsize = bufsize;
        this.raf = in;
        this.fileLength = in.length();
    }

    public final int read() throws IOException {
        if (this.reported_pos == this.fileLength) {
            return -1;
        }
        if (this.current == null || this.reported_pos - this.current.real_pos_start >= ((long) this.current.buf_end)) {
            this.current = getPage(this.reported_pos);
        }
        byte[] bArr = this.current.buffer;
        long j = this.reported_pos;
        this.reported_pos = 1 + j;
        return bArr[(int) (j - this.current.real_pos_start)] & 255;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        } else {
            if (this.reported_pos == this.fileLength) {
                return -1;
            }
            int copied = 0;
            while (copied < len && this.reported_pos != this.fileLength) {
                if (this.current == null || this.reported_pos - this.current.real_pos_start >= ((long) this.current.buf_end)) {
                    this.current = getPage(this.reported_pos);
                }
                int buf_pos = (int) (this.reported_pos - this.current.real_pos_start);
                int length = Math.min(len - copied, this.current.buf_end - buf_pos);
                System.arraycopy(this.current.buffer, buf_pos, b, off + copied, length);
                this.reported_pos += (long) length;
                copied += length;
            }
            return copied;
        }
    }

    private Page getPage(long pos) throws IOException {
        long key = pos / ((long) this.bufsize);
        SoftReference<Page> r = (SoftReference) this.pages.get(key);
        Page p = r == null ? null : (Page) r.get();
        if (p != null) {
            return p;
        }
        long page_start = key * ((long) this.bufsize);
        if (page_start != this.real_pos) {
            this.raf.seek(page_start);
            this.real_pos = page_start;
        }
        p = new Page();
        int n = this.raf.read(p.buffer);
        if (n >= 0) {
            p.real_pos_start = this.real_pos;
            p.buf_end = n;
            this.real_pos += (long) n;
        }
        this.pages.put(key, new SoftReference(p));
        return p;
    }

    public boolean markSupported() {
        return false;
    }

    public void close() throws IOException {
        this.raf.close();
    }

    public void seek(long pos) throws IOException {
        this.reported_pos = pos;
        this.current = null;
    }

    public long getFilePointer() {
        return this.reported_pos;
    }
}
