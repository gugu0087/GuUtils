package com.xyc.okutils.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by hasee on 2018/4/10.
 */

public class SnackbarUtils {

    private static Snackbar snackbar;
    private static int actionColor;

    public static void setSnackbarActionColor(int color) {
        actionColor = color;
    }

    public static void showSnackBar(View view, String title, String btnTip, final View.OnClickListener clickListener) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, title, Snackbar.LENGTH_SHORT);
        }
        snackbar.setAction(btnTip, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view);
            }
        });
        if(actionColor!=0){
            snackbar.setActionTextColor(actionColor);
        }
        snackbar.show();
    }

    public static void dismissSnack() {
        if (snackbar == null) {
            return;
        }
        snackbar.dismiss();
    }

}
