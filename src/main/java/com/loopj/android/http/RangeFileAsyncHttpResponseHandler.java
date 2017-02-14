package com.loopj.android.http;

import android.util.Log;

import com.umeng.socialize.common.SocializeConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;

public abstract class RangeFileAsyncHttpResponseHandler extends FileAsyncHttpResponseHandler {
    private static final String  LOG_TAG = "RangeFileAsyncHttpResponseHandler";
    private              boolean append  = false;
    private              long    current = 0;

    public RangeFileAsyncHttpResponseHandler(File file) {
        super(file);
    }

    public void sendResponseMessage(HttpResponse response) throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == 416) {
                if (!Thread.currentThread().isInterrupted()) {
                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), null);
                }
            } else if (status.getStatusCode() >= 300) {
                if (!Thread.currentThread().isInterrupted()) {
                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), null,
                            new HttpResponseException(status.getStatusCode(), status
                                    .getReasonPhrase()));
                }
            } else if (!Thread.currentThread().isInterrupted()) {
                Header header = response.getFirstHeader(AsyncHttpClient.HEADER_CONTENT_RANGE);
                if (header == null) {
                    this.append = false;
                    this.current = 0;
                } else {
                    Log.v(LOG_TAG, "Content-Range: " + header.getValue());
                }
                sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(),
                        getResponseData(response.getEntity()));
            }
        }
    }

    protected byte[] getResponseData(HttpEntity entity) throws IOException {
        if (entity != null) {
            InputStream instream = entity.getContent();
            long contentLength = entity.getContentLength() + this.current;
            FileOutputStream buffer = new FileOutputStream(getTargetFile(), this.append);
            if (instream != null) {
                try {
                    byte[] tmp = new byte[4096];
                    while (this.current < contentLength) {
                        int l = instream.read(tmp);
                        if (l == -1 || Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        this.current += (long) l;
                        buffer.write(tmp, 0, l);
                        sendProgressMessage(this.current, contentLength);
                    }
                    instream.close();
                    buffer.flush();
                    buffer.close();
                } catch (Throwable th) {
                    instream.close();
                    buffer.flush();
                    buffer.close();
                }
            }
        }
        return null;
    }

    public void updateRequestHeaders(HttpUriRequest uriRequest) {
        if (this.mFile.exists() && this.mFile.canWrite()) {
            this.current = this.mFile.length();
        }
        if (this.current > 0) {
            this.append = true;
            uriRequest.setHeader("Range", "bytes=" + this.current + SocializeConstants.OP_DIVIDER_MINUS);
        }
    }
}
