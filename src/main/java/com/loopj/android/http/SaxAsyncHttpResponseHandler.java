package com.loopj.android.http;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SaxAsyncHttpResponseHandler<T extends DefaultHandler> extends
        AsyncHttpResponseHandler {
    private static final String LOG_TAG = "SaxAsyncHttpResponseHandler";
    private              T      handler = null;

    public abstract void onFailure(int i, Header[] headerArr, T t);

    public abstract void onSuccess(int i, Header[] headerArr, T t);

    public SaxAsyncHttpResponseHandler(T t) {
        if (t == null) {
            throw new Error("null instance of <T extends DefaultHandler> passed to constructor");
        }
        this.handler = t;
    }

    protected byte[] getResponseData(HttpEntity entity) throws IOException {
        SAXException e;
        Throwable th;
        ParserConfigurationException e2;
        if (entity != null) {
            InputStream instream = entity.getContent();
            InputStreamReader inputStreamReader = null;
            if (instream != null) {
                try {
                    XMLReader rssReader = SAXParserFactory.newInstance().newSAXParser()
                            .getXMLReader();
                    rssReader.setContentHandler(this.handler);
                    InputStreamReader inputStreamReader2 = new InputStreamReader(instream, "UTF-8");
                    try {
                        rssReader.parse(new InputSource(inputStreamReader2));
                        AsyncHttpClient.silentCloseInputStream(instream);
                        if (inputStreamReader2 != null) {
                            try {
                                inputStreamReader2.close();
                            } catch (IOException e3) {
                            }
                        }
                    } catch (SAXException e4) {
                        e = e4;
                        inputStreamReader = inputStreamReader2;
                        try {
                            Log.e(LOG_TAG, "getResponseData exception", e);
                            AsyncHttpClient.silentCloseInputStream(instream);
                            if (inputStreamReader != null) {
                                try {
                                    inputStreamReader.close();
                                } catch (IOException e5) {
                                }
                            }
                            return null;
                        } catch (Throwable th2) {
                            th = th2;
                            AsyncHttpClient.silentCloseInputStream(instream);
                            if (inputStreamReader != null) {
                                try {
                                    inputStreamReader.close();
                                } catch (IOException e6) {
                                }
                            }
                            throw th;
                        }
                    } catch (ParserConfigurationException e7) {
                        e2 = e7;
                        inputStreamReader = inputStreamReader2;
                        Log.e(LOG_TAG, "getResponseData exception", e2);
                        AsyncHttpClient.silentCloseInputStream(instream);
                        if (inputStreamReader != null) {
                            try {
                                inputStreamReader.close();
                            } catch (IOException e8) {
                            }
                        }
                        return null;
                    } catch (Throwable th3) {
                        th = th3;
                        inputStreamReader = inputStreamReader2;
                        AsyncHttpClient.silentCloseInputStream(instream);
                        if (inputStreamReader != null) {
                            inputStreamReader.close();
                        }
                        throw th;
                    }
                } catch (SAXException e9) {
                    e = e9;
                    Log.e(LOG_TAG, "getResponseData exception", e);
                    AsyncHttpClient.silentCloseInputStream(instream);
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    return null;
                } catch (ParserConfigurationException e10) {
                    e2 = e10;
                    Log.e(LOG_TAG, "getResponseData exception", e2);
                    AsyncHttpClient.silentCloseInputStream(instream);
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        onSuccess(statusCode, headers, this.handler);
    }

    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onSuccess(statusCode, headers, this.handler);
    }
}
