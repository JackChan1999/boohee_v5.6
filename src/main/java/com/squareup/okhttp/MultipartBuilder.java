package com.squareup.okhttp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okio.ByteString;

public final class MultipartBuilder {
    public static final  MediaType ALTERNATIVE = MediaType.parse("multipart/alternative");
    private static final byte[]    COLONSPACE  = new byte[]{(byte) 58, (byte) 32};
    private static final byte[]    CRLF        = new byte[]{(byte) 13, (byte) 10};
    private static final byte[]    DASHDASH    = new byte[]{(byte) 45, (byte) 45};
    public static final  MediaType DIGEST      = MediaType.parse("multipart/digest");
    public static final  MediaType FORM        = MediaType.parse("multipart/form-data");
    public static final  MediaType MIXED       = MediaType.parse("multipart/mixed");
    public static final  MediaType PARALLEL    = MediaType.parse("multipart/parallel");
    private final ByteString        boundary;
    private final List<RequestBody> partBodies;
    private final List<Headers>     partHeaders;
    private       MediaType         type;

    public MultipartBuilder() {
        this(UUID.randomUUID().toString());
    }

    public MultipartBuilder(String boundary) {
        this.type = MIXED;
        this.partHeaders = new ArrayList();
        this.partBodies = new ArrayList();
        this.boundary = ByteString.encodeUtf8(boundary);
    }

    public MultipartBuilder type(MediaType type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        } else if (type.type().equals("multipart")) {
            this.type = type;
            return this;
        } else {
            throw new IllegalArgumentException("multipart != " + type);
        }
    }

    public MultipartBuilder addPart(RequestBody body) {
        return addPart(null, body);
    }

    public MultipartBuilder addPart(Headers headers, RequestBody body) {
        if (body == null) {
            throw new NullPointerException("body == null");
        } else if (headers != null && headers.get("Content-Type") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Type");
        } else if (headers == null || headers.get("Content-Length") == null) {
            this.partHeaders.add(headers);
            this.partBodies.add(body);
            return this;
        } else {
            throw new IllegalArgumentException("Unexpected header: Content-Length");
        }
    }

    private static StringBuilder appendQuotedString(StringBuilder target, String key) {
        target.append('\"');
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char ch = key.charAt(i);
            switch (ch) {
                case '\n':
                    target.append("%0A");
                    break;
                case '\r':
                    target.append("%0D");
                    break;
                case '\"':
                    target.append("%22");
                    break;
                default:
                    target.append(ch);
                    break;
            }
        }
        target.append('\"');
        return target;
    }

    public MultipartBuilder addFormDataPart(String name, String value) {
        return addFormDataPart(name, null, RequestBody.create(null, value));
    }

    public MultipartBuilder addFormDataPart(String name, String filename, RequestBody value) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        StringBuilder disposition = new StringBuilder("form-data; name=");
        appendQuotedString(disposition, name);
        if (filename != null) {
            disposition.append("; filename=");
            appendQuotedString(disposition, filename);
        }
        return addPart(Headers.of("Content-Disposition", disposition.toString()), value);
    }

    public RequestBody build() {
        if (!this.partHeaders.isEmpty()) {
            return new MultipartRequestBody(this.type, this.boundary, this.partHeaders, this
                    .partBodies);
        }
        throw new IllegalStateException("Multipart body must have at least one part.");
    }
}
