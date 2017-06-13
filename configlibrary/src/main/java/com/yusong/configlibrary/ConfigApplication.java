package com.yusong.configlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.hss01248.dialog.MyActyManager;
import com.hss01248.dialog.StyledDialog;
import com.sdsmdg.tastytoast.TastyToast;
import com.yusong.configlibrary.utils.AppUtils;

import java.io.File;
import java.util.Stack;

/**
 * create by feisher on 2017/6/13
 * Email：458079442@qq.com
 */
public class ConfigApplication extends Application {
    static Context mContext;
    private static ConfigApplication mInstance;
    public static Toast toast;
    public static Stack<Activity> activityStack;
    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance == null)
            mInstance = this;
        mContext = getApplicationContext();
        initLifecycle();
    }

    private void initLifecycle() {
        activityStack = new Stack<>();
        StyledDialog.init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityStack.add(activity);
            }
            @Override
            public void onActivityStarted(Activity activity) {}
            @Override
            public void onActivityResumed(Activity activity) {
                //在这里保存顶层activity的引用(内部以软引用实现)
                MyActyManager.getInstance().setCurrentActivity(activity);
            }
            @Override
            public void onActivityPaused(Activity activity) {}
            @Override
            public void onActivityStopped(Activity activity) {}
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
            @Override
            public void onActivityDestroyed(Activity activity) {
                activityStack.remove(activity);
            }
        });
    }

    public static Context getContext(){
        return mContext;
    }
    public static ConfigApplication getInstance() {
        return mInstance;
    }
    // 遍历所有Activity并finish
    public static void exit() {
        if (!AppUtils.isEmpty(activityStack)) {
            for (Activity activity : activityStack) {
                if (activity != null) {
                    activity.finish();
                }
            }
        }
    }
    public static void showMessage(String message,int type) {
        if (toast != null) {
            toast.cancel();
        }
        toast = TastyToast.makeText(getContext(), message, 0, type);
        toast.show();
    }

    public static void showMessage(String message) {
        showMessage(message,TastyToast.DEFAULT);
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }
}
