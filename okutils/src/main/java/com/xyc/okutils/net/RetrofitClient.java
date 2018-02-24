package com.xyc.okutils.net;

import com.xyc.okutils.base.ComParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hasee on 2017/12/12.
 */

public class RetrofitClient {
    private Retrofit mRetrofit;
    private CommonParamsInterceptor cpInterceptor;

    public RetrofitClient(String url) {
        mRetrofit = new Retrofit.Builder().baseUrl(url)
                .client(initOkhttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private OkHttpClient initOkhttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (ComParams.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        cpInterceptor = new CommonParamsInterceptor.Builder()

                //.addQuery("phoneType", "android")
                //.addQuery("platform", "33")
                .build();
        builder.addInterceptor(cpInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public void updateQueryParams(Map<String, String> params) {
        Map<String, String> tmpParams = this.cpInterceptor.getQueryMap();
        tmpParams.putAll(params);
        this.cpInterceptor.setQueryMap(tmpParams);
    }

    public void updateHeaderParams(Map<String, String> params) {
        Map<String, String> tmpParams = this.cpInterceptor.getHeaderMap();
        tmpParams.putAll(params);
        this.cpInterceptor.setHeaderMap(tmpParams);
    }

    public void updateParams(Map<String, String> params) {
        Map<String, String> tmpParams = this.cpInterceptor.getParamsMap();
        tmpParams.putAll(params);
        this.cpInterceptor.setParamsMap(tmpParams);
    }

    public void updateListParams(List<String> linesList) {
        List<String> list = new ArrayList<>();
        linesList.addAll(list);
        this.cpInterceptor.setLinesList(list);
    }
    public  void  updateJsonParams(JSONObject params){

        this.cpInterceptor.setJsonParams(params);
    }

    public <T> T create(Class<?> clazz) {
        return (T) mRetrofit.create(clazz);
    }

}
