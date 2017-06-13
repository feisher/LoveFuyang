package com.yusong.lovefuyang.ui.login;
import com.yusong.configlibrary.mvp.MVPBaseActivity;
import com.yusong.lovefuyang.entity.request.Login;
import com.yusong.lovefuyang.entity.response.LoginResult;
/**登陆页面   （框架使用案例）
 * create by feisher on 2017/6/13 13:33
 * Email：458079442@qq.com
 */
public class LoginActivity extends MVPBaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {

    @Override
    protected int layoutId() {
        return 0;
    }

    @Override
    public void initData() {
        mPresenter.login(new Login("17130044331","666666"));

    }

    @Override
    public void loginCallback(LoginResult loginResult) {

    }
}
