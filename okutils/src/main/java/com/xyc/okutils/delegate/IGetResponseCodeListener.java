package com.xyc.okutils.delegate;

/**
 * Created by hasee on 2018/1/12.
 */

public interface IGetResponseCodeListener {
    void onSuccessResponse(int code, String response);
    void onFailedResponse(String msg);
}
