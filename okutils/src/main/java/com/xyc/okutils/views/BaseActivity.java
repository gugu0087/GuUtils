package com.xyc.okutils.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xyc.okutils.MyApplication;
import com.xyc.okutils.R;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by hasee on 2017/12/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ImageView ivGoBack;
    private TextView tvTitle;
    private TextView tvNext;
    private LinearLayout llCenterView;
    private View mAddView;
    private RelativeLayout llBaseTitleLayout;
    private View tvTitleBar;
    private boolean needTranslucent = true;
    private View errorView;
    private ImageView ivNext;

    protected abstract void initHeader();

    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        initHeaderView();
        boolean isGet = checkPermissionAllGranted(permissions);
        checkPermission(isGet);
    }

    private void checkPermission(boolean isGet) {//ACCESS_COARSE_LOCATION
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.CALL_PHONE, "打电话权限", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "相机权限", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "位置权限", R.drawable.permission_ic_location));
       /*  if(!isGet){
             permissionItems.add(new PermissionItem(Manifest.permission.SYSTEM_ALERT_WINDOW,"悬浮窗权限",R.drawable.permission_ic_phone));
             permissionItems.add(new PermissionItem(Manifest.permission.WRITE_SETTINGS,"系统设置权限",R.drawable.permission_ic_phone));
         }*/

        HiPermission.create(BaseActivity.this)
                .title("权限申请")
                .permissions(permissionItems)
                .msg("权限申请")
                .animStyle(R.style.PermissionAnimScale)
                .style(R.style.PermissionDefaultBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.d("xyc", "onClose: 1");
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        Log.d("xyc", "onDeny:1 ");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        Log.d("xyc", "onGuarantee:1 ");
                    }
                });
    }

    String[] permissions = new String[]{
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WRITE_SETTINGS
    };

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    private void initHeaderView() {
        ivGoBack = (ImageView) findViewById(R.id.ivGoBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvNext = (TextView) findViewById(R.id.tvNext);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        llCenterView = (LinearLayout) findViewById(R.id.llCenterView);
        llBaseTitleLayout = (RelativeLayout) findViewById(R.id.llBaseTitleLayout);

        tvTitleBar = findViewById(R.id.tvTitleBar);

        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setCenterView(int layout) {
        llCenterView.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        mAddView = inflater.inflate(layout, null);
        llCenterView.addView(mAddView, new ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        initHeader();
        translucentStatus(this, mTitleColor);
    }

    private int mTitleColor = 0;

    protected void addCurrentActivity(Activity context) {
        MyApplication.getApp().addActivity(context);
    }

    protected void setHeaderTitleBarColor(int color) {
        mTitleColor = color;
    }

    protected void setLeftIconVisibility(int visibility) {
        if (ivGoBack == null) {
            return;
        }
        ivGoBack.setVisibility(visibility);
    }

    protected void setHeaderVisibility(int visibility) {
        if (llBaseTitleLayout == null) {
            return;
        }
        llBaseTitleLayout.setVisibility(visibility);
    }

    protected void setRightIconVisibility(int visibility) {
        if (tvNext == null) {
            return;
        }
        tvNext.setVisibility(visibility);
    }

    protected void setRightImgVisibility(int visibility) {
        if (ivNext == null) {
            return;
        }
        ivNext.setVisibility(View.VISIBLE);
    }

    protected void setRightImage(int resId) {
        if (ivNext != null) {
            ivNext.setImageResource(resId);
        }
    }

    protected void setHeader_rightImageListener(View.OnClickListener listener) {
        if (ivNext != null) {
            ivNext.setOnClickListener(listener);
        }
    }

    protected void setHeader_LeftClickListener(View.OnClickListener listener) {
        if (ivGoBack != null) {
            ivGoBack.setOnClickListener(listener);
        }
    }

    protected void setHeader_RightClickListener(View.OnClickListener listener) {
        if (tvNext != null) {
            tvNext.setOnClickListener(listener);
        }

    }

    protected void setHeader_RightText(String rightText) {
        if (tvNext == null) {
            return;
        }
        tvNext.setText(rightText);
    }

    protected void setHeaderTitle(String title) {
        if (tvTitle == null || title == null) {
            return;
        }
        tvTitle.setText(title);
    }

    protected void translucentStatus(Activity activity, int statusColor) {
        if (!getIsNeedTranslucent()) {
            return;
        }
        if (statusColor == 0) {
            statusColor = activity.getResources().getColor(R.color.main_blue_color);
        }
        //当前手机版本为5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean getIsNeedTranslucent() {
        return needTranslucent;
    }

    public void setIsNeedTranslucent(boolean needTranslucent) {
        this.needTranslucent = needTranslucent;
    }
}
