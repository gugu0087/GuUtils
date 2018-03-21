package com.xyc.myexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xyc.okutils.manager.ThreadPoolManager;
import com.xyc.okutils.model.NetLogModel;
import com.xyc.okutils.utils.NetLogManager;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tvClick(View view){
  /*      NetLogModel netLogModel = new NetLogModel();
        netLogModel.setCode(200);
        netLogModel.setMethod("post");
        netLogModel.setContent("sdadad");
        NetLogManager.getInstance().logNetResponse(netLogModel,true);*/

       for(int i=0;i<10;i++){
           ThreadPoolManager.getInstance().getSingleThreadPool().execute(new Runnable() {
               @Override
               public void run() {
                   try {
                       Thread.sleep(1000);
                       Log.d("xyc", "run: currentThread="+Thread.currentThread().getName());

                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           });
       }
    }
}
