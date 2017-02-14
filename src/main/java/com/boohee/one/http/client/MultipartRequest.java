package com.boohee.one.http.client;

import com.alipay.sdk.sys.a;
import com.android.volley.AuthFailureError;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.common.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MultipartRequest extends BaseJsonRequest {
    private final String boundary   = ("apiclient-" + System.currentTimeMillis());
    private final String lineEnd    = "\r\n";
    private final String twoHyphens = "--";

    public class DataPart {
        public byte[] content;
        public String fileName;
        public String type;

        public DataPart(String name, byte[] data) {
            this.fileName = name;
            this.content = data;
        }

        public DataPart(String name, byte[] data, String mimeType) {
            this.fileName = name;
            this.content = data;
            this.type = mimeType;
        }

        public String getFileName() {
            return this.fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getContent() {
            return this.content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public MultipartRequest(String url, JsonParams params, JsonCallback callback) {
        super(1, url, params, callback, callback);
    }

    public String getBodyContentType() {
        return "multipart/form-data;charset=utf-8;boundary=" + this.boundary;
    }

    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }
            Map<String, DataPart> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }
            dos.writeBytes("--" + this.boundary + "--" + "\r\n");
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
            return null;
        }
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return (Map) new Gson().fromJson(getJsonParams().toString(), new
                TypeToken<HashMap<String, String>>() {
        }.getType());
    }

    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String
            encoding) throws IOException {
        try {
            for (Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, (String) entry.getKey(), (String) entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws
            IOException {
        for (Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, (DataPart) entry.getValue(), (String) entry.getKey());
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String
            parameterValue) throws IOException {
        dataOutputStream.writeBytes("--" + this.boundary + "\r\n");
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + a
                .e + "\r\n");
        dataOutputStream.writeBytes("\r\n");
        dataOutputStream.write(parameterValue.getBytes(Constants.UTF_8));
        dataOutputStream.writeBytes("\r\n");
    }

    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String
            inputName) throws IOException {
        dataOutputStream.writeBytes("--" + this.boundary + "\r\n");
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + inputName + "\"; " +
                "filename=\"" + dataFile.getFileName() + a.e + "\r\n");
        if (!(dataFile.getType() == null || dataFile.getType().trim().isEmpty())) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + "\r\n");
        }
        dataOutputStream.writeBytes("\r\n");
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bufferSize = Math.min(fileInputStream.available(), 1048576);
        byte[] buffer = new byte[bufferSize];
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bufferSize = Math.min(fileInputStream.available(), 1048576);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }
        dataOutputStream.writeBytes("\r\n");
    }
}
