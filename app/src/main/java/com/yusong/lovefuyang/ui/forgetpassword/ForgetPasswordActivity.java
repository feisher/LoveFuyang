package com.yusong.lovefuyang.ui.forgetpassword;

import com.yusong.configlibrary.mvp.MVPBaseActivity;
import com.yusong.lovefuyang.R;

public class ForgetPasswordActivity extends MVPBaseActivity<ForgetPasswordContract.View, ForgetPasswordPresenter> implements ForgetPasswordContract.View {

    @Override
    protected int layoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initData() {

    }
}
