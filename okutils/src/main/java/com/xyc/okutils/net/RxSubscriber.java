package com.xyc.okutils.net;


import java.util.List;

import rx.Subscriber;

public abstract class RxSubscriber<T> extends Subscriber<T> {
    public int resultSize = 0;

    @Override
    public void onCompleted() {

    }
    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        if (t instanceof List) {
            resultSize = ((List) t).size();
        } else if (t instanceof Class && t != null) {
            resultSize = 1;
        }
        onSuccess(t);
    }

//    /**
//     * 错误回调
//     */
//    pubic void onError(Throwable ex) {
//        onError(ExceptionEngine.handleException(e));
//        //error interactive
//    }

    protected abstract void onSuccess(T response);

}
