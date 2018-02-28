package com.xyc.myexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xyc.okutils.manager.ThreadPoolManager;
import com.xyc.okutils.model.NetLogModel;
import com.xyc.okutils.utils.NetLogManager;
import com.xyc.okutils.views.GuProgressBarView;

public class MainActivity extends AppCompatActivity {

    private GuProgressBarView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (GuProgressBarView) findViewById(R.id.guProgressBar);
      //  progressView.setCurrentProDrawable(R.drawable.progress_view);
        progressView.setCurrentText("60%");
        progressView.setPercent(0.60);
    }

    public void tvClick(View view){
        NetLogModel netLogModel = new NetLogModel();
        netLogModel.setCode(200);
        netLogModel.setMethod("post");
        netLogModel.setContent("sdadad");
        NetLogManager.getInstance().logNetResponse(netLogModel,true);

        ThreadPoolManager.getInstance().getFixedThreadPool(5).execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
