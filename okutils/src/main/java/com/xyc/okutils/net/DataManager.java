package com.xyc.okutils.net;

import android.util.Log;
import android.widget.Toast;

import com.xyc.okutils.base.ApplicationHolder;
import com.xyc.okutils.base.ComParams;
import com.xyc.okutils.delegate.IGetResponseCodeListener;
import com.xyc.okutils.utils.NetLogManager;
import com.xyc.okutils.utils.PreferencesUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hasee on 2017/12/13.
 */

public class DataManager {
    private JSONObject parmas;
    private HashMap<String, String> getParams;

    public static DataManager instance = null;
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    /**
     * 异步发送请求
     *
     * @param
     * @param params
     * @return
     */
    public Request postAsynchronous(String url, JSONObject params) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                params.toString())).build();
        return request;
    }

    public OkHttpClient initOkhttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (ComParams.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }


    public void setPostRequestParams(JSONObject parmas) {
        this.parmas = parmas;
    }

    public void setGetRequestParams(HashMap<String, String> map) {
        getParams = map;
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = null;
        String token = PreferencesUtils.getString(ComParams.USER_TOKEN);
        Log.d("xyc", "addHeaders: token=" + token);
        if (token != null) {
            builder = new Request.Builder()
                    .addHeader("X-Authorization", "bearer " + token);
            return builder;
        }
        return new Request.Builder();
    }

    public void getRequestNetData(final String url, final IGetResponseCodeListener callback) {
        if (url == null) {
            return;
        }
        OkHttpClient okHttpClient = DataManager.getInstance().initOkhttp();

        String tempParams = null;
        final String requestUrl;
        if (getParams != null) {
            if (getParams.size() > 0) {
                Iterator iterator = getParams.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    if (tempParams == null) {
                        tempParams = entry.getKey() + "=" + entry.getValue();
                    } else {
                        tempParams = tempParams + "&" + entry.getKey() + "=" + entry.getValue();
                    }
                }
            }
        }
        String baseUrl = ComParams.URL;
        if (tempParams != null) {
            requestUrl = baseUrl + url + "?" + tempParams;
        } else {
            requestUrl = baseUrl + url;
        }
        Request.Builder builder = addHeaders();
        String s = builder.head().toString();
        Log.d("xyc", "getRequestNetData: s=" + s);
        final Request request = builder.url(requestUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                NetLogManager.getInstance().LogNet(requestUrl, 500, e.getMessage());
                ApplicationHolder.getInstance().postMainRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailedResponse(e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String body = response.body().toString();
                ApplicationHolder.getInstance().postMainRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (response.code() == 200) {
                                callback.onSuccessResponse(200, body);
                            }
                        }
                    }
                });
                NetLogManager.getInstance().LogNet(requestUrl, response.code(), body);
            }
        });


    }

    public void postRequestNetData(final String url, final IGetResponseCodeListener callback) {
        if (parmas == null) {
            Toast.makeText(ApplicationHolder.getAppContext(), "参数为空", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient okHttpClient = DataManager.getInstance().initOkhttp();
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                parmas.toString());
        Request.Builder requestBuilder = new Request.Builder();
        String token = PreferencesUtils.getString(ComParams.USER_TOKEN);
        Log.d("xyc", "intercept: token=" + token);
        if (token != null) {
            requestBuilder.header("X-Authorization", "bearer " + token);
        }
        parmas = null;
        final Request request = requestBuilder
                .url(url)                // 上传url地址
                .post(requestBody)              // post请求体
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                ApplicationHolder.getInstance().postMainRunnable(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailedResponse(e.getMessage());
                        NetLogManager.getInstance().LogNet(url, 500, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String body = response.body().string();
                ApplicationHolder.getInstance().postMainRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            callback.onSuccessResponse(200, body);
                        } else {
                            callback.onFailedResponse(body);
                        }
                        NetLogManager.getInstance().LogNet(url, response.code(), body);
                    }
                });
            }
        });
    }
}
