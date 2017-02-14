package com.qiniu.android.http;

import com.qiniu.android.common.Constants;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.Domain;
import com.qiniu.android.http.CancellationHandler.CancellationException;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.utils.AsyncRun;
import com.qiniu.android.utils.StringMap;
import com.qiniu.android.utils.StringMap.Consumer;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Dns;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xiaomi.account.openauth.utils.Network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

public final class Client {
    public static final String ContentTypeHeader = "Content-Type";
    public static final String DefaultMime       = "application/octet-stream";
    public static final String FormMime          = "application/x-www-form-urlencoded";
    public static final String JsonMime          = "application/json";
    private final UrlConverter converter;
    private final OkHttpClient httpClient;

    private static class IpTag {
        public String ip;

        private IpTag() {
            this.ip = null;
        }
    }

    public Client() {
        this(null, 10, 30, null, null);
    }

    public Client(Proxy proxy, int connectTimeout, int responseTimeout, UrlConverter converter,
                  final DnsManager dns) {
        this.converter = converter;
        this.httpClient = new OkHttpClient();
        if (proxy != null) {
            this.httpClient.setProxy(proxy.toSystemProxy());
        }
        if (dns != null) {
            this.httpClient.setDns(new Dns() {
                public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                    try {
                        InetAddress[] ips = dns.queryInetAdress(new Domain(hostname));
                        if (ips == null) {
                            throw new UnknownHostException(hostname + " resolve failed");
                        }
                        List<InetAddress> l = new ArrayList();
                        Collections.addAll(l, ips);
                        return l;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new UnknownHostException(e.getMessage());
                    }
                }
            });
        }
        this.httpClient.networkInterceptors().add(new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                IpTag tag = (IpTag) request.tag();
                String ip = "";
                try {
                    ip = chain.connection().getSocket().getRemoteSocketAddress().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tag.ip = ip;
                return response;
            }
        });
        this.httpClient.setConnectTimeout((long) connectTimeout, TimeUnit.SECONDS);
        this.httpClient.setReadTimeout((long) responseTimeout, TimeUnit.SECONDS);
        this.httpClient.setWriteTimeout(0, TimeUnit.SECONDS);
    }

    private static String via(Response response) {
        String via = response.header("X-Via", "");
        if (!via.equals("")) {
            return via;
        }
        via = response.header("X-Px", "");
        if (!via.equals("")) {
            return via;
        }
        via = response.header("Fw-Via", "");
        if (via.equals("")) {
            return via;
        }
        return via;
    }

    private static String ctype(Response response) {
        MediaType mediaType = response.body().contentType();
        if (mediaType == null) {
            return "";
        }
        return mediaType.type() + "/" + mediaType.subtype();
    }

    private static JSONObject buildJsonResp(byte[] body) throws Exception {
        return new JSONObject(new String(body, Constants.UTF_8));
    }

    public void asyncSend(final Builder requestBuilder, StringMap headers, final
    CompletionHandler completionHandler) {
        if (headers != null) {
            headers.forEach(new Consumer() {
                public void accept(String key, Object value) {
                    requestBuilder.header(key, value.toString());
                }
            });
        }
        final CompletionHandler complete = new CompletionHandler() {
            public void complete(final ResponseInfo info, final JSONObject response) {
                AsyncRun.run(new Runnable() {
                    public void run() {
                        completionHandler.complete(info, response);
                    }
                });
            }
        };
        requestBuilder.header(Network.USER_AGENT, UserAgent.instance().toString());
        final long start = System.currentTimeMillis();
        this.httpClient.newCall(requestBuilder.tag(new IpTag()).build()).enqueue(new Callback() {
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                long duration = (System.currentTimeMillis() - start) / 1000;
                int statusCode = -1;
                String msg = e.getMessage();
                if (e instanceof CancellationException) {
                    statusCode = -2;
                } else if (e instanceof UnknownHostException) {
                    statusCode = -1003;
                } else if (msg != null && msg.indexOf("Broken pipe") == 0) {
                    statusCode = ResponseInfo.NetworkConnectionLost;
                } else if (e instanceof SocketTimeoutException) {
                    statusCode = -1001;
                } else if (e instanceof ConnectException) {
                    statusCode = -1004;
                }
                URL u = request.url();
                complete.complete(new ResponseInfo(statusCode, "", "", "", u.getHost(), u.getPath
                        (), "", u.getPort(), (double) duration, 0, e.getMessage()), null);
            }

            public void onResponse(Response response) throws IOException {
                Response response2 = response;
                Client.this.onRet(response2, ((IpTag) response.request().tag()).ip, (System
                        .currentTimeMillis() - start) / 1000, complete);
            }
        });
    }

    public void asyncPost(String url, byte[] body, StringMap headers, ProgressHandler
            progressHandler, CompletionHandler completionHandler, UpCancellationSignal c) {
        asyncPost(url, body, 0, body.length, headers, progressHandler, completionHandler, c);
    }

    public void asyncPost(String url, byte[] body, int offset, int size, StringMap headers,
                          ProgressHandler progressHandler, CompletionHandler completionHandler,
                          CancellationHandler c) {
        RequestBody rbody;
        RequestBody rbody2;
        if (this.converter != null) {
            url = this.converter.convert(url);
        }
        if (body == null || body.length <= 0) {
            rbody = RequestBody.create(null, new byte[0]);
        } else {
            rbody = RequestBody.create(MediaType.parse("application/octet-stream"), body, offset,
                    size);
        }
        if (progressHandler != null) {
            rbody2 = new CountingRequestBody(rbody, progressHandler, c);
        } else {
            rbody2 = rbody;
        }
        asyncSend(new Builder().url(url).post(rbody2), headers, completionHandler);
    }

    public void asyncMultipartPost(String url, PostArgs args, ProgressHandler progressHandler,
                                   CompletionHandler completionHandler, CancellationHandler c) {
        RequestBody file;
        if (args.file != null) {
            file = RequestBody.create(MediaType.parse(args.mimeType), args.file);
        } else {
            file = RequestBody.create(MediaType.parse(args.mimeType), args.data);
        }
        asyncMultipartPost(url, args.params, progressHandler, args.fileName, file,
                completionHandler, c);
    }

    private void asyncMultipartPost(String url, StringMap fields, ProgressHandler
            progressHandler, String fileName, RequestBody file, CompletionHandler
            completionHandler, CancellationHandler cancellationHandler) {
        if (this.converter != null) {
            url = this.converter.convert(url);
        }
        final MultipartBuilder mb = new MultipartBuilder();
        mb.addFormDataPart("file", fileName, file);
        fields.forEach(new Consumer() {
            public void accept(String key, Object value) {
                mb.addFormDataPart(key, value.toString());
            }
        });
        mb.type(MediaType.parse("multipart/form-data"));
        RequestBody body = mb.build();
        if (progressHandler != null) {
            body = new CountingRequestBody(body, progressHandler, cancellationHandler);
        }
        asyncSend(new Builder().url(url).post(body), null, completionHandler);
    }

    private void onRet(Response response, String ip, long duration, CompletionHandler complete) {
        int code = response.code();
        String reqId = response.header("X-Reqid");
        reqId = reqId == null ? null : reqId.trim();
        byte[] body = null;
        String error = null;
        try {
            body = response.body().bytes();
        } catch (IOException e) {
            error = e.getMessage();
        }
        JSONObject json = null;
        if (!ctype(response).equals("application/json") || body == null) {
            String str = new String(body);
        } else {
            try {
                json = buildJsonResp(body);
                if (response.code() != 200) {
                    error = json.optString("error", new String(body, Constants.UTF_8));
                }
            } catch (Exception e2) {
                if (response.code() < 300) {
                    error = e2.getMessage();
                }
            }
        }
        URL u = response.request().url();
        final ResponseInfo info = new ResponseInfo(code, reqId, response.header("X-Log"), via
                (response), u.getHost(), u.getPath(), ip, u.getPort(), (double) duration, 0, error);
        final CompletionHandler completionHandler = complete;
        final JSONObject jSONObject = json;
        AsyncRun.run(new Runnable() {
            public void run() {
                completionHandler.complete(info, jSONObject);
            }
        });
    }
}
