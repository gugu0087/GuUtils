package com.xyc.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xyc.okutils.views.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView login;
    private TextView getAllShop;

    @Override
    protected void initHeader() {
        setHeaderVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterView(R.layout.activity_main);
        login = (TextView) findViewById(R.id.login);
        getAllShop = (TextView) findViewById(R.id.getAllShop);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetManager.getInstance().login("test0117BDM","a123456");
            }
        });
        getAllShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetManager.getInstance().getAllShopInfo(0,-1,-1,null);
            }
        });
    }
}
