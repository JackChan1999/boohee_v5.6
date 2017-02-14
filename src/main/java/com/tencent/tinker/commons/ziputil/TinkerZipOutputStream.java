package com.tencent.tinker.commons.ziputil;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.zip.ZipException;

public class TinkerZipOutputStream extends FilterOutputStream implements ZipConstants {
    public static final  byte[] BYTE                    = new byte[0];
    public static final  int    DEFLATED                = 8;
    static final         int    MOD_DATE_CONST          = 18698;
    public static final  int    STORED                  = 0;
    static final         int    TIME_CONST              = 40691;
    private static final byte[] ZIP64_PLACEHOLDER_BYTES = new byte[]{(byte) -1, (byte) -1, (byte)
            -1, (byte) -1};
    private static final int    ZIP_VERSION_2_0         = 20;
    private       boolean               archiveNeedsZip64EocdRecord;
    private       ByteArrayOutputStream cDir;
    private       byte[]                commentBytes;
    private       TinkerZipEntry        currentEntry;
    private       boolean               currentEntryNeedsZip64;
    private       int                   defaultCompressionMethod;
    private final HashSet<String>       entries;
    private       byte[]                entryCommentBytes;
    private final boolean               forceZip64;
    private       byte[]                nameBytes;
    private       long                  offset;

    public TinkerZipOutputStream(OutputStream os) {
        this(os, false);
    }

    public TinkerZipOutputStream(OutputStream os, boolean forceZip64) {
        super(os);
        this.entries = new HashSet();
        this.commentBytes = BYTE;
        this.defaultCompressionMethod = 8;
        this.cDir = new ByteArrayOutputStream();
        this.offset = 0;
        this.forceZip64 = forceZip64;
    }

    static long writeLongAsUint32(OutputStream os, long i) throws IOException {
        os.write((int) (255 & i));
        os.write(((int) (i >> 8)) & 255);
        os.write(((int) (i >> 16)) & 255);
        os.write(((int) (i >> 24)) & 255);
        return i;
    }

    static long writeLongAsUint64(OutputStream os, long i) throws IOException {
        int i1 = (int) i;
        os.write(i1 & 255);
        os.write((i1 >> 8) & 255);
        os.write((i1 >> 16) & 255);
        os.write((i1 >> 24) & 255);
        int i2 = (int) (i >> 32);
        os.write(i2 & 255);
        os.write((i2 >> 8) & 255);
        os.write((i2 >> 16) & 255);
        os.write((i2 >> 24) & 255);
        return i;
    }

    static int writeIntAsUint16(OutputStream os, int i) throws IOException {
        os.write(i & 255);
        os.write((i >> 8) & 255);
        return i;
    }

    public void close() throws IOException {
        if (this.out != null) {
            finish();
            this.out.close();
            this.out = null;
        }
    }

    public void closeEntry() throws IOException {
        checkOpen();
        if (this.currentEntry != null) {
            int flags;
            long curOffset = 30;
            if (this.currentEntry.getMethod() != 0) {
                curOffset = 30 + 16;
                writeLongAsUint32(this.out, ZipConstants.EXTSIG);
                writeLongAsUint32(this.out, this.currentEntry.crc);
                writeLongAsUint32(this.out, this.currentEntry.compressedSize);
                writeLongAsUint32(this.out, this.currentEntry.size);
            }
            if (this.currentEntry.getMethod() == 0) {
                flags = 0;
            } else {
                flags = 8;
            }
            flags |= 2048;
            writeLongAsUint32(this.cDir, ZipConstants.CENSIG);
            writeIntAsUint16(this.cDir, 20);
            writeIntAsUint16(this.cDir, 20);
            writeIntAsUint16(this.cDir, flags);
            writeIntAsUint16(this.cDir, this.currentEntry.getMethod());
            writeIntAsUint16(this.cDir, this.currentEntry.time);
            writeIntAsUint16(this.cDir, this.currentEntry.modDate);
            writeLongAsUint32(this.cDir, this.currentEntry.crc);
            if (this.currentEntry.getMethod() == 8) {
                curOffset += this.currentEntry.getCompressedSize();
            } else {
                curOffset += this.currentEntry.getSize();
            }
            writeLongAsUint32(this.cDir, this.currentEntry.getCompressedSize());
            writeLongAsUint32(this.cDir, this.currentEntry.getSize());
            curOffset += (long) writeIntAsUint16(this.cDir, this.nameBytes.length);
            if (this.currentEntry.extra != null) {
                curOffset += (long) writeIntAsUint16(this.cDir, this.currentEntry.extra.length);
            } else {
                writeIntAsUint16(this.cDir, 0);
            }
            writeIntAsUint16(this.cDir, this.entryCommentBytes.length);
            writeIntAsUint16(this.cDir, 0);
            writeIntAsUint16(this.cDir, 0);
            writeLongAsUint32(this.cDir, 0);
            writeLongAsUint32(this.cDir, this.currentEntry.localHeaderRelOffset);
            this.cDir.write(this.nameBytes);
            this.nameBytes = null;
            if (this.currentEntry.extra != null) {
                this.cDir.write(this.currentEntry.extra);
            }
            this.offset += curOffset;
            if (this.entryCommentBytes.length > 0) {
                this.cDir.write(this.entryCommentBytes);
                this.entryCommentBytes = BYTE;
            }
            this.currentEntry = null;
        }
    }

    public void finish() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream is closed");
        } else if (this.cDir != null) {
            if (this.entries.isEmpty()) {
                throw new ZipException("No entries");
            }
            if (this.currentEntry != null) {
                closeEntry();
            }
            int cdirEntriesSize = this.cDir.size();
            writeLongAsUint32(this.cDir, ZipConstants.ENDSIG);
            writeIntAsUint16(this.cDir, 0);
            writeIntAsUint16(this.cDir, 0);
            if (this.archiveNeedsZip64EocdRecord) {
                writeIntAsUint16(this.cDir, 65535);
                writeIntAsUint16(this.cDir, 65535);
                writeLongAsUint32(this.cDir, -1);
                writeLongAsUint32(this.cDir, -1);
            } else {
                writeIntAsUint16(this.cDir, this.entries.size());
                writeIntAsUint16(this.cDir, this.entries.size());
                writeLongAsUint32(this.cDir, (long) cdirEntriesSize);
                writeLongAsUint32(this.cDir, this.offset);
            }
            writeIntAsUint16(this.cDir, this.commentBytes.length);
            if (this.commentBytes.length > 0) {
                this.cDir.write(this.commentBytes);
            }
            this.cDir.writeTo(this.out);
            this.cDir = null;
        }
    }

    public void putNextEntry(TinkerZipEntry ze) throws IOException {
        if (this.currentEntry != null) {
            closeEntry();
        }
        int method = ze.getMethod();
        if (method == -1) {
            method = this.defaultCompressionMethod;
        }
        if (method == 0) {
            if (ze.getCompressedSize() == -1) {
                ze.setCompressedSize(ze.getSize());
            } else if (ze.getSize() == -1) {
                ze.setSize(ze.getCompressedSize());
            }
            if (ze.getCrc() == -1) {
                throw new ZipException("STORED entry missing CRC");
            } else if (ze.getSize() == -1) {
                throw new ZipException("STORED entry missing size");
            } else if (ze.size != ze.compressedSize) {
                throw new ZipException("STORED entry size/compressed size mismatch");
            }
        }
        checkOpen();
        ze.comment = null;
        ze.extra = null;
        ze.time = TIME_CONST;
        ze.modDate = MOD_DATE_CONST;
        this.nameBytes = ze.name.getBytes(StandardCharsets.UTF_8);
        checkSizeIsWithinShort("Name", this.nameBytes);
        this.entryCommentBytes = BYTE;
        if (ze.comment != null) {
            this.entryCommentBytes = ze.comment.getBytes(StandardCharsets.UTF_8);
            checkSizeIsWithinShort("Comment", this.entryCommentBytes);
        }
        ze.setMethod(method);
        this.currentEntry = ze;
        this.currentEntry.localHeaderRelOffset = this.offset;
        this.entries.add(this.currentEntry.name);
        int flags = (method == 0 ? 0 : 8) | 2048;
        writeLongAsUint32(this.out, ZipConstants.LOCSIG);
        writeIntAsUint16(this.out, 20);
        writeIntAsUint16(this.out, flags);
        writeIntAsUint16(this.out, method);
        writeIntAsUint16(this.out, this.currentEntry.time);
        writeIntAsUint16(this.out, this.currentEntry.modDate);
        if (method == 0) {
            writeLongAsUint32(this.out, this.currentEntry.crc);
            writeLongAsUint32(this.out, this.currentEntry.size);
            writeLongAsUint32(this.out, this.currentEntry.size);
        } else {
            writeLongAsUint32(this.out, 0);
            writeLongAsUint32(this.out, 0);
            writeLongAsUint32(this.out, 0);
        }
        writeIntAsUint16(this.out, this.nameBytes.length);
        if (this.currentEntry.extra != null) {
            writeIntAsUint16(this.out, this.currentEntry.extra.length);
        } else {
            writeIntAsUint16(this.out, 0);
        }
        this.out.write(this.nameBytes);
        if (this.currentEntry.extra != null) {
            this.out.write(this.currentEntry.extra);
        }
    }

    public void setComment(String comment) {
        if (comment == null) {
            this.commentBytes = BYTE;
            return;
        }
        byte[] newCommentBytes = comment.getBytes(StandardCharsets.UTF_8);
        checkSizeIsWithinShort("Comment", newCommentBytes);
        this.commentBytes = newCommentBytes;
    }

    public void write(byte[] buffer, int offset, int byteCount) throws IOException {
        Arrays.checkOffsetAndCount(buffer.length, offset, byteCount);
        if (this.currentEntry == null) {
            throw new ZipException("No active entry");
        } else if (this.currentEntry.getMethod() == 0) {
            this.out.write(buffer, offset, byteCount);
        } else {
            this.out.write(buffer, offset, byteCount);
        }
    }

    private void checkOpen() throws IOException {
        if (this.cDir == null) {
            throw new IOException("Stream is closed");
        }
    }

    private void checkSizeIsWithinShort(String property, byte[] bytes) {
        if (bytes.length > 65535) {
            throw new IllegalArgumentException(property + " too long in UTF-8:" + bytes.length + " bytes");
        }
    }
}
