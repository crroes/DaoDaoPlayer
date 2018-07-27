package com.cross.daodaoplayer;

import android.app.Application;
import android.content.Context;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";


    private static Context context;


    @Override
    public void onCreate() {
         super.onCreate();
        context = getApplicationContext();
//         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }

    /**
     * 全局的context对象
     */
    public static Context getContext(){
        return context;
    }
}
