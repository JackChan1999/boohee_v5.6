package com.qiniu.android.http;

import com.qiniu.android.http.CancellationHandler.CancellationException;
import com.qiniu.android.utils.AsyncRun;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class CountingRequestBody extends RequestBody {
    private static final int SEGMENT_SIZE = 2048;
    private final RequestBody         body;
    private final CancellationHandler cancellationHandler;
    private final ProgressHandler     progress;

    protected final class CountingSink extends ForwardingSink {
        private int bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        public void write(Buffer source, long byteCount) throws IOException {
            if (CountingRequestBody.this.cancellationHandler == null && CountingRequestBody.this
                    .progress == null) {
                super.write(source, byteCount);
            } else if (CountingRequestBody.this.cancellationHandler == null ||
                    !CountingRequestBody.this.cancellationHandler.isCancelled()) {
                super.write(source, byteCount);
                this.bytesWritten = (int) (((long) this.bytesWritten) + byteCount);
                if (CountingRequestBody.this.progress != null) {
                    AsyncRun.run(new Runnable() {
                        public void run() {
                            try {
                                CountingRequestBody.this.progress.onProgress(CountingSink.this
                                        .bytesWritten, (int) CountingRequestBody.this
                                        .contentLength());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
                throw new CancellationException();
            }
        }
    }

    public CountingRequestBody(RequestBody body, ProgressHandler progress, CancellationHandler
            cancellationHandler) {
        this.body = body;
        this.progress = progress;
        this.cancellationHandler = cancellationHandler;
    }

    public long contentLength() throws IOException {
        return this.body.contentLength();
    }

    public MediaType contentType() {
        return this.body.contentType();
    }

    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(new CountingSink(sink));
        this.body.writeTo(bufferedSink);
        bufferedSink.flush();
    }
}
