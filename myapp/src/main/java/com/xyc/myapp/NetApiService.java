package com.xyc.myapp;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hasee on 2017/12/12.
 */

public interface NetApiService {

    @FormUrlEncoded
    @POST("sign/login")
    Observable<User> login(@Field("loginName") String loginName, @Field("password") String password);
    // 商户信息相关接口
    @GET("shopinfo")
    Observable<CommonModel<ShopInfo>> getAllShopInfo();
}
