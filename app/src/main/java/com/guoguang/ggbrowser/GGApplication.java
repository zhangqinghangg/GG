package com.guoguang.ggbrowser;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import logUtils.GGLog4j;

/**
 * Created by Administrator on 2019/1/11.
 */

public class GGApplication extends Application {
    private static GGApplication application;
    private static final String TAG = "安卓---GGApplication";
    private static GGLog4j logger = new GGLog4j("黑龙江北安农垦医院.log");

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.i(TAG,"系统异常了:"+e.getMessage());
            restartApp(); //发生崩溃异常时,重启应用
        }
    };
    private void restartApp() {

        logger.i(TAG,"系统准备重启。。。");
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent); // 1秒钟后重启应用

        android.os.Process.killProcess(android.os.Process.myPid());

    }

}
