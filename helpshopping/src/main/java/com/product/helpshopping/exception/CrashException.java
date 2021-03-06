package com.product.helpshopping.exception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.product.common.utils.LogUtils;
import com.product.helpshopping.BaseApplication;
import com.product.helpshopping.ui.activity.SplashActivity;
import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 处理未捕获异常，提高application的友好度
 *
 * @author Peng Yi
 */
public class CrashException implements UncaughtExceptionHandler {
    public static final String TAG = "CrashException";
    private static final long RESTART_DELAY = 100L;
    private static CrashException sInstance;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashException() {
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public synchronized static CrashException getInstance() {
        if (sInstance == null) {
            sInstance = new CrashException();
        }
        return sInstance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            killProcess();
        }
    }

    /**
     * 自定义收集和处理异常
     *
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        LogUtils.i(TAG, "handleException");
        MobclickAgent.reportError(mContext, ex);
        ex.printStackTrace();

        // 重新启动应用
        int requestCode = 0;
        PendingIntent pIntent = PendingIntent.getActivity(mContext, requestCode,
                new Intent(mContext, SplashActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + RESTART_DELAY, pIntent);

        ((BaseApplication) mContext.getApplicationContext()).exitApp(true);
        // AppManager.getInstance().appExit(mContext, true);
        return true;
    }

    private void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
