package com.xyc.okutils.net;


import com.xyc.okutils.utils.NetLogManager;

/**
 * Created by hasee on 2017/12/16.
 */

public abstract class LoadRxSubscriber<T> extends RxSubscriber<T> {

    public LoadRxSubscriber() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable ex) {
       // NetLogManager.getInstance().logNetResponse("--",ex.getMessage());
    }

    @Override
    protected void onSuccess(T response) {
        //NetLogManager.getInstance().logNetResponse("--",response.toString());
    }

}
