package com.yusong.lovefuyang.ui.login;

import com.yusong.configlibrary.mvp.BasePresenter;
import com.yusong.configlibrary.mvp.BaseView;
import com.yusong.lovefuyang.entity.request.Login;
import com.yusong.lovefuyang.entity.response.LoginResult;

public class LoginContract {
    interface View extends BaseView {
        void loginCallback(LoginResult loginResult);
    }

    interface  Presenter extends BasePresenter<View> {
       void login(Login route);
    }
}
