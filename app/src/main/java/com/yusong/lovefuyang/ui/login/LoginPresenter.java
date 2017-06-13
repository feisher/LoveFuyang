package com.yusong.lovefuyang.ui.login;


import com.yusong.configlibrary.http.Callback;
import com.yusong.configlibrary.http.HttpResult;
import com.yusong.configlibrary.mvp.BasePresenterImpl;
import com.yusong.lovefuyang.api.ServiceManager;
import com.yusong.lovefuyang.entity.request.Login;
import com.yusong.lovefuyang.entity.response.LoginResult;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void login(Login route) {
        invoke(ServiceManager.getInstance()
                .getCommonService()
                .login(route), new Callback<HttpResult<LoginResult>>() {
            @Override
            protected void subscribe(Disposable d) {
                addSubscribe(d);
            }
            @Override
            protected void onFailure() {

            }

            @Override
            protected void onSuccess(HttpResult<LoginResult> result) {
                mView.loginCallback(result.getResults());
            }
        });
    }
}
