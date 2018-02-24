package com.xyc.okutils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xyc.okutils.delegate.IConfirmClickListener;
import com.xyc.okutils.delegate.IGetSelectHourListener;

import java.util.Calendar;

/**
 * Created by hasee on 2018/2/24.
 */

public class DialogUtils {
    private static DialogUtils instance = null;

    private DialogUtils() {

    }

    public static DialogUtils getInstance() {
        if (instance == null) {
            instance = new DialogUtils();
        }
        return instance;
    }

    public static void showAddRemarksDialog(Context context, final IConfirmClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.no_title_two_btn_dialog, null);
        dialog.setView(view);
        final EditText tvContent = (EditText) view.findViewById(R.id.tvContent);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tvConfirm);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarks = tvContent.getText().toString();
                listener.confirmClick(remarks);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    public static void showTipsDialog(Context context, String title, String content) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.tips_dialog_view, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ImageView tvClose = (ImageView) view.findViewById(R.id.tvClose);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        if (title != null) {
            tvTitle.setText(title);
        }
        if (content != null) {
            tvContent.setText(content);
        }
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    public static void showTimeSelectHourDialog(Context context, final IGetSelectHourListener listener) {
        //自定义控件
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.time_dialog, null);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        Calendar calendar = Calendar.getInstance();
        //初始化时间
        calendar.setTimeInMillis(System.currentTimeMillis());
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(Calendar.MINUTE);
        //设置time布局
        builder.setView(view);
        builder.setTitle("设置时间信息");
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int mHour = timePicker.getCurrentHour();
                int mMinute = timePicker.getCurrentMinute();
                //时间小于10的数字 前面补0 如01:12:00

                listener.getDateTime(mHour, mMinute, 0);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public static void showConfirmDialog(Context context, String content, final View.OnClickListener listener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.show_confirm_dialog, null);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tvConfirm);
        tvContent.setText(content);
        dialog.setView(view);
        dialog.show();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

    }
}
