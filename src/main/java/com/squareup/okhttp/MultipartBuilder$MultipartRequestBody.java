package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;

import java.io.IOException;
import java.util.List;

import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

final class MultipartBuilder$MultipartRequestBody extends RequestBody {
    private final ByteString boundary;
    private long contentLength = -1;
    private final MediaType         contentType;
    private final List<RequestBody> partBodies;
    private final List<Headers>     partHeaders;

    public MultipartBuilder$MultipartRequestBody(MediaType type, ByteString boundary,
                                                 List<Headers> partHeaders, List<RequestBody>
                                                         partBodies) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.boundary = boundary;
        this.contentType = MediaType.parse(type + "; boundary=" + boundary.utf8());
        this.partHeaders = Util.immutableList(partHeaders);
        this.partBodies = Util.immutableList(partBodies);
    }

    public MediaType contentType() {
        return this.contentType;
    }

    public long contentLength() throws IOException {
        long result = this.contentLength;
        if (result != -1) {
            return result;
        }
        result = writeOrCountBytes(null, true);
        this.contentLength = result;
        return result;
    }

    private long writeOrCountBytes(BufferedSink sink, boolean countBytes) throws IOException {
        long byteCount = 0;
        Buffer byteCountBuffer = null;
        if (countBytes) {
            byteCountBuffer = new Buffer();
            sink = byteCountBuffer;
        }
        int partCount = this.partHeaders.size();
        for (int p = 0; p < partCount; p++) {
            Headers headers = (Headers) this.partHeaders.get(p);
            RequestBody body = (RequestBody) this.partBodies.get(p);
            sink.write(MultipartBuilder.access$000());
            sink.write(this.boundary);
            sink.write(MultipartBuilder.access$100());
            if (headers != null) {
                int headerCount = headers.size();
                for (int h = 0; h < headerCount; h++) {
                    sink.writeUtf8(headers.name(h)).write(MultipartBuilder.access$200())
                            .writeUtf8(headers.value(h)).write(MultipartBuilder.access$100());
                }
            }
            MediaType contentType = body.contentType();
            if (contentType != null) {
                sink.writeUtf8("Content-Type: ").writeUtf8(contentType.toString()).write
                        (MultipartBuilder.access$100());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                sink.writeUtf8("Content-Length: ").writeDecimalLong(contentLength).write
                        (MultipartBuilder.access$100());
            } else if (countBytes) {
                byteCountBuffer.clear();
                return -1;
            }
            sink.write(MultipartBuilder.access$100());
            if (countBytes) {
                byteCount += contentLength;
            } else {
                ((RequestBody) this.partBodies.get(p)).writeTo(sink);
            }
            sink.write(MultipartBuilder.access$100());
        }
        sink.write(MultipartBuilder.access$000());
        sink.write(this.boundary);
        sink.write(MultipartBuilder.access$000());
        sink.write(MultipartBuilder.access$100());
        if (countBytes) {
            byteCount += byteCountBuffer.size();
            byteCountBuffer.clear();
        }
        return byteCount;
    }

    public void writeTo(BufferedSink sink) throws IOException {
        writeOrCountBytes(sink, false);
    }
}
