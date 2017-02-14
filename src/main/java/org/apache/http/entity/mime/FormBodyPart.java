package org.apache.http.entity.mime;

import com.alipay.sdk.sys.a;
import org.apache.http.entity.mime.content.ContentBody;

public class FormBodyPart {
    private final ContentBody body;
    private final Header header;
    private final String name;

    public FormBodyPart(String name, ContentBody body) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else if (body == null) {
            throw new IllegalArgumentException("Body may not be null");
        } else {
            this.name = name;
            this.body = body;
            this.header = new Header();
            generateContentDisp(body);
            generateContentType(body);
            generateTransferEncoding(body);
        }
    }

    public String getName() {
        return this.name;
    }

    public ContentBody getBody() {
        return this.body;
    }

    public Header getHeader() {
        return this.header;
    }

    public void addField(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Field name may not be null");
        }
        this.header.addField(new MinimalField(name, value));
    }

    protected void generateContentDisp(ContentBody body) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("form-data; name=\"");
        buffer.append(getName());
        buffer.append(a.e);
        if (body.getFilename() != null) {
            buffer.append("; filename=\"");
            buffer.append(body.getFilename());
            buffer.append(a.e);
        }
        addField("Content-Disposition", buffer.toString());
    }

    protected void generateContentType(ContentBody body) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(body.getMimeType());
        if (body.getCharset() != null) {
            buffer.append("; charset=");
            buffer.append(body.getCharset());
        }
        addField("Content-Type", buffer.toString());
    }

    protected void generateTransferEncoding(ContentBody body) {
        addField(MIME.CONTENT_TRANSFER_ENC, body.getTransferEncoding());
    }
}
