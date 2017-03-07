package com.team60.ournews.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.team60.ournews.MyApplication;
import com.team60.ournews.common.Constants;
import com.team60.ournews.listener.DownListener;
import com.team60.ournews.module.connection.RetrofitUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyUtil {

    public static void sendLog(Object object, String message) {
        if (!Constants.IS_DEBUG_MODE)
            Log.d(object.getClass().getName(), message);
    }

    public static SharedPreferences getSharedPreferences(String name) {
        return MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static boolean isLoginName(String loginName) {
        if (loginName != null)
            if (loginName.length() > 5 && loginName.length() < 13)
                return true;
        return false;
    }

    public static boolean isPassword(String password) {
        if (password != null)
            if (password.length() > 5 && password.length() < 13)
                return true;
        return false;
    }

    public static void openKeyBord(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void closeKeyBord(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public static String getPhotoUrl(@NonNull String photoName) {
        return RetrofitUtil.BASE_URL + "downloadImage?name=" + photoName;
    }

    public static void savePhoto(final String photoUrl, final DownListener downListener) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory()
                        .getMainDiskStorageCache().getResource(new SimpleCacheKey(photoUrl));
                File tempPhoto = resource.getFile();
                if (tempPhoto.exists()) {
                    // 首先保存图片
                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
                    String fileName = "OurNews";
                    File appDir = new File(file, fileName);
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }
                    fileName = System.currentTimeMillis() + ".jpg";
                    File currentFile = new File(appDir, fileName);

                    InputStream inputStream = null;
                    FileOutputStream fileOutputStream = null;
                    try {
                        int byteRead;
                        inputStream = new FileInputStream(tempPhoto);
                        fileOutputStream = new FileOutputStream(currentFile);
                        byte[] buffer = new byte[1024];
                        while ((byteRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, byteRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    } finally {
                        if (inputStream != null)
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        if (fileOutputStream != null)
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }

                    subscriber.onNext(null);
                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(MyApplication.getContext().getContentResolver(),
                                currentFile.getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    MyApplication.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(new File(currentFile.getPath()))));
                } else {
                    subscriber.onError(new Exception());
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        downListener.error();
                    }

                    @Override
                    public void onNext(Object o) {
                        downListener.success();
                    }
                });
    }
}
