package com.xyc.okutils.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nanchen.compresshelper.CompressHelper;
import com.xyc.okutils.base.ApplicationHolder;

import java.io.File;

/**
 * Created by hasee on 2018/2/6.
 */

public class FileUtils {
    private  Context mContext = ApplicationHolder.getAppContext();
    private static FileUtils instance = null;

    private FileUtils() {

    }

    public static FileUtils getInstance() {
        if (instance == null) {
            instance = new FileUtils();
        }

        return instance;
    }

    // 生成文件
    public  File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    private  void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public  String getFilePath(String extraPath) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {//如果外部储存可用
            File externalFilesDir =mContext.getExternalFilesDir(extraPath);
            if (externalFilesDir != null) {
                return externalFilesDir.getPath();//获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/com.waka.workspace.logtofile/files/Logs/log_2016-03-14_16-15-09.log
            }
            return null;
        } else {
            return mContext.getFilesDir().getPath();//直接存在/data/data里，非root手机是看不到的
        }
    }
    public static File compressImg(File oldFile) {
        long lengths = oldFile.length();
        File newFile = null;
        if (lengths > 2048000) {
            newFile = CompressHelper.getDefault(ApplicationHolder.getAppContext()).compressToFile(oldFile);
        } else {
            newFile = oldFile;
        }
        return newFile;
    }

}
