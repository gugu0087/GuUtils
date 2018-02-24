package com.xyc.myapp;

import android.util.Log;

import com.baidu.android.bbalbs.common.util.CommonParam;
import com.xyc.okutils.base.ComParams;
import com.xyc.okutils.delegate.IGetResponseCodeListener;
import com.xyc.okutils.net.DataManager;
import com.xyc.okutils.net.LoadRxSubscriber;
import com.xyc.okutils.net.RetrofitClient;
import com.xyc.okutils.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hasee on 2018/2/24.
 */

public class NetManager {
    private static NetManager instance = null;
    private static DataManager dataManager= null;
    private RetrofitClient mRetrofitClient;
    private NetApiService apiService;
    private NetManager(){
        mRetrofitClient = new RetrofitClient(CommonUrl.URL);
        apiService = mRetrofitClient.create(NetApiService.class);
    }
    public static NetManager getInstance(){
        if(instance ==null){
            instance =  new NetManager();
        }
        if(dataManager == null){
            dataManager = new DataManager();
        }
        return instance;
    }
    public void updateQueryParams(Map<String, String> params) {
        mRetrofitClient.updateQueryParams(params);
    }

    public void updateHeaderParams(Map<String, String> params) {
        mRetrofitClient.updateHeaderParams(params);
    }

    public void updateParamsMap(Map<String, String> paramsMap) {
        mRetrofitClient.updateParams(paramsMap);
    }

    public void updateListParams(List<String> linesList) {
        mRetrofitClient.updateListParams(linesList);
    }

    public void updateJsonParams(JSONObject params) {
        mRetrofitClient.updateJsonParams(params);
    }

    public NetApiService getApiService() {
        return apiService;
    }

    public void login(String loginName, String password){
        JSONObject map = new JSONObject();
        try {
            map.put("loginName",loginName);
            map.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataManager.setPostRequestParams(map);
        dataManager.postRequestNetData(CommonUrl.login_url, new IGetResponseCodeListener() {
            @Override
            public void onSuccessResponse(int code, String response) {
                Log.d("xyc", "onSuccessResponse: response="+response);
                try {
                    JSONObject object = new JSONObject(response);
                    String token = object.getString("token");
                    Log.d("xyc", "onSuccessResponse: token="+token);
                    PreferencesUtils.putString(ComParams.USER_TOKEN,token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailedResponse(String msg) {
                Log.d("xyc", "onSuccessResponse: msg="+msg);
            }
        });
    }
    public void getAllShopInfo(int pageNumber, int creatorIds, int statuses, String name){
        Map<String, String> map = new HashMap<>();
        if (creatorIds != -1) {
            map.put("creatorIds", String.valueOf(creatorIds));
        }
        if (statuses != -1) {
            map.put("statuses", String.valueOf(statuses));
        }
        map.put("pageNumber", String.valueOf(pageNumber));
        map.put("name", name);
        map.put("pageSize", String.valueOf(20));
        updateQueryParams(map);
        apiService.getAllShopInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadRxSubscriber<CommonModel<ShopInfo>>() {
                    @Override
                    public void onError(Throwable ex) {
                        super.onError(ex);
                        Log.d("xyc", "onSuccessResponse: msg="+ex.getMessage());
                    }

                    @Override
                    protected void onSuccess(CommonModel<ShopInfo> response) {
                        super.onSuccess(response);
                        Log.d("xyc", "onSuccessResponse: ShopInfo="+response);
                    }
                });
    }
}
