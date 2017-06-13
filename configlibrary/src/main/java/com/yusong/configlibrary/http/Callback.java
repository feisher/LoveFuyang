package com.yusong.configlibrary.http;

import android.content.Context;

import com.hss01248.dialog.StyledDialog;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.socks.library.KLog;
import com.yusong.configlibrary.ConfigApplication;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by wenxin
 * contact with yangwenxin711@gmail.com
 */
public abstract class Callback<T> implements Observer<T> {

    private Context mContext;
    private Stateful target;

    public Callback() {

    }

    public Callback(Context context) {
        this.mContext = context;

    }

    public void setTarget(Stateful target) {
        this.target = target;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ConnectException) {
            ConfigApplication.showMessage("当前网络不可用，请检查网络后重试");
        } else if (e instanceof SocketTimeoutException) {
            ConfigApplication.showMessage("请求超时，请稍后重试");
        } else if (e instanceof HttpException) {
            ConfigApplication.showMessage("服务器繁忙");
        } else {
            ConfigApplication.showMessage("服务器繁忙");
        }
//        setState(Constants.STATE_ERROR);
        KLog.e(e.toString());
        onFailure();

    }

    @Override
    public void onSubscribe(Disposable d) {
        StyledDialog.buildLoading().show();
        subscribe(d);
    }

    @Override
    public void onComplete() {
        StyledDialog.dismissLoading();
    }

    @Override
    public void onNext(T t) {
        HttpResult result = (HttpResult) t;
        if (!result.isError()) {//成功
            onSuccess(t);
        }
        StyledDialog.dismissLoading();
    }

    protected abstract void subscribe(Disposable d);

    protected abstract void onFailure();

    protected abstract void onSuccess(T t);
}
