package com.xyc.okutils.net;

import android.text.TextUtils;

import com.xyc.okutils.base.ComParams;
import com.xyc.okutils.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


public class CommonParamsInterceptor implements Interceptor {
    boolean isExit  = true;
    Map<String, String> queryMap = new HashMap<>();
    Map<String, String> paramsMap = new HashMap<>();
    Map<String, String> headerMap = new HashMap<>();
    List<String> linesList = new ArrayList<>();
    JSONObject jsonParams = new JSONObject();

    public JSONObject getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(JSONObject jsonParams) {
        this.jsonParams = jsonParams;
    }

    public Map<String, String> getQueryMap() {
        return queryMap;
    }


    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public List<String> getLinesList() {
        return linesList;
    }

    public void setLinesList(List<String> linesList) {
        this.linesList = linesList;
    }

    public CommonParamsInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();
        Request.Builder requestBuilder = oldRequest.newBuilder();

        Headers.Builder headerBuilder = oldRequest.headers().newBuilder();

        if (headerMap.size() > 0) {
            Iterator iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }

        if (linesList.size() > 0) {
            for (String line : linesList) {
                headerBuilder.add(line);
            }
            requestBuilder.headers(headerBuilder.build());
        }

        if (queryMap.size() > 0) {
            oldRequest = injectIntoUrl(oldRequest.url().newBuilder(), requestBuilder, queryMap);
        }

        // process post body inject
        if (paramsMap.size() > 0) {
            JSONObject params = new JSONObject();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                try {
                    params.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    params.toString());
            requestBuilder.post(requestBody);
        }

        if (jsonParams != null && jsonParams.length() > 0) {
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonParams.toString());
            requestBuilder.post(requestBody);
        }
        String token = PreferencesUtils.getString(ComParams.USER_TOKEN);

        if (token != null) {
            requestBuilder.header("X-Authorization", "bearer " + token);
        }
        oldRequest = requestBuilder.build();
        headerMap.clear();
        linesList.clear();
        queryMap.clear();
        paramsMap.clear();
        jsonParams = null;

        return chain.proceed(oldRequest);
    }

    private boolean injectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
            return false;
        }
        return true;
    }

    private Request injectIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }

        return null;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class Builder {

        CommonParamsInterceptor interceptor;

        public Builder() {
            interceptor = new CommonParamsInterceptor();
        }

        public Builder add(String key, String value) {
            interceptor.paramsMap.put(key, value);
            return this;
        }

        public Builder addMap(Map<String, String> paramsMap) {
            interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public Builder addHeader(String key, String value) {
            interceptor.headerMap.put(key, value);
            return this;
        }

        public Builder addHeaderMap(Map<String, String> headerMap) {
            interceptor.headerMap.putAll(headerMap);
            return this;
        }

        public Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            interceptor.linesList.add(headerLine);
            return this;
        }

        public Builder addHeaderLinesList(List<String> headerLinesList) {
            for (String headerLine : headerLinesList) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                interceptor.linesList.add(headerLine);
            }
            return this;
        }

        public Builder addQuery(String key, String value) {
            interceptor.queryMap.put(key, value);
            return this;
        }

        public Builder addQueryMap(Map<String, String> queryMap) {
            interceptor.queryMap.putAll(queryMap);
            return this;
        }

        public CommonParamsInterceptor build() {
            return interceptor;
        }

    }
}
