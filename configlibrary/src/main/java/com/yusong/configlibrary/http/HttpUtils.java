package com.yusong.configlibrary.http;


import com.yusong.configlibrary.ConfigApplication;
import com.yusong.configlibrary.utils.NetworkUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wenxin
 * contact with yangwenxin711@gmail.com
 */
public class HttpUtils {

    public static <T> void invoke(Stateful stateful, Observable<T> observable, Callback<T> callback) {

        if (!NetworkUtils.isConnected()) {
            ConfigApplication.showMessage("网络连接已断开");
//            if (stateful != null) {
//                if (callback.mHandler == null)
//                    stateful.setState(Constants.STATE_ERROR);
//            }
            return;
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
}
