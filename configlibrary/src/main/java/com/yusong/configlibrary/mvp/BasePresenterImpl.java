package com.yusong.configlibrary.mvp;

import com.yusong.configlibrary.http.Callback;
import com.yusong.configlibrary.http.HttpUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**改写MVPPlus插件presenter基类实现和rxJava 结合
 * create by feisher on 2017/6/13 13:13
 * Email：458079442@qq.com
 */
public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V>{
    protected V mView;
    protected CompositeDisposable mCompositeDisposable;
    @Override
    public void attachView(V view) {
        mView=view;
        if (useEvenBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void detachView() {
        if (useEvenBus()) {
            EventBus.getDefault().unregister(this);
        }
        unSubscribe();
        mView=null;
    }
    protected <T> void invoke(Observable<T> observable, Callback<T> callback) {
        HttpUtils.invoke(null, observable, callback);
    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证activity结束时取消所有正在执行的订阅
        }
    }
    protected boolean useEvenBus() {
        return false;
    }
}
