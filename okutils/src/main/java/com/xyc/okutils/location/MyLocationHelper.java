package com.xyc.okutils.location;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

/**
 * Created by hasee on 2018/1/13.
 */

public class MyLocationHelper extends BDAbstractLocationListener {
     private ILocationSuccessListener mListener;
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if(mListener !=null){
            mListener.locationSuccess(bdLocation);
        }
    }
    public MyLocationHelper(ILocationSuccessListener listener){
        mListener = listener;
    }
}
