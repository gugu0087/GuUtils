package com.xyc.okutils.utils;

import android.content.Context;
import android.util.Log;


import com.xyc.okutils.base.ApplicationHolder;
import com.xyc.okutils.base.ComParams;
import com.xyc.okutils.model.NetLogModel;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by hasee on 2018/1/17.
 */

public class NetLogManager {
    public static NetLogManager instance = null;
    private Context mContext;

    private NetLogManager() {
        mContext = ApplicationHolder.getAppContext();
    }

    public static NetLogManager getInstance() {
        if (instance == null) {
            instance = new NetLogManager();
        }
        return instance;
    }


    public void logNetResponse(final NetLogModel netLogModel) {
        ApplicationHolder.getInstance().getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {

                String currentTime = DateUtils.getInstance().getLongToString(DateUtils.getInstance().getSystemLongTime(), DateUtils.DATE_FORMAT_DAY2);
                String formatSystemTime = DateUtils.getInstance().getLongToString(DateUtils.getInstance().getSystemLongTime(), DateUtils.DATE_FORMAT_YEAR2);

                String filePath = FileUtils.getInstance().getFilePath(ComParams.LOG_EXTRA_PATH);
                String fileName = currentTime + ".html";//log日志名，使用时间命名，保证不重复
                String log = "time = " + formatSystemTime + "\n" + "netLogModel=" + netLogModel.toString() + "\n\n";
                writeTxtToFile(log, filePath, fileName);
            }
        });

    }

    public void logdResponse(final String request, final String response) {
        ApplicationHolder.getInstance().getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {

                String currentTime = DateUtils.getInstance().getLongToString(DateUtils.getInstance().getSystemLongTime(), DateUtils.DATE_FORMAT_DAY2);
                String formatSystemTime = DateUtils.getInstance().getLongToString(DateUtils.getInstance().getSystemLongTime(), DateUtils.DATE_FORMAT_YEAR2);

                String filePath = FileUtils.getInstance().getFilePath(ComParams.LOG_EXTRA_PATH);
                String fileName = currentTime + ".html";//log日志名，使用时间命名，保证不重复
                String log = "time=" + formatSystemTime + "\n" + "request_url=" + request + "\n" + "response = " + response + "\n\n";
                writeTxtToFile(log, filePath, fileName);
            }
        });

    }


    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        FileUtils.getInstance().makeFilePath(filePath, fileName);

        String strFilePath = filePath;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath, fileName);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    public void LogNet(final String url, final int code, final String response) {

        ApplicationHolder.getInstance().getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                String currentTime = DateUtils.getInstance().getLongToString(DateUtils.getInstance().getSystemLongTime(), DateUtils.DATE_FORMAT_DAY2);
                String formatSystemTime = DateUtils.getInstance().getLongToString(DateUtils.getInstance().getSystemLongTime(), DateUtils.DATE_FORMAT_YEAR2);

                String filePath = FileUtils.getInstance().getFilePath(ComParams.LOG_EXTRA_PATH);
                String fileName = currentTime + ".html";//log日志名，使用时间命名，保证不重复
                String log = "time=" + formatSystemTime + "\n" + "request_url=" + url + "\n" + "response_code = " + code + "\n" + "response = " + response + "\n\n";
                writeTxtToFile(log, filePath, fileName);
            }
        });
    }

}
