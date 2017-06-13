package com.yusong.lovefuyang.entity.request;

/**
 * create by feisher on 2017/6/13
 * Email：458079442@qq.com
 */
public class Login {
    String mobile;
    String pwd;

    public Login(String mobile, String pwd) {
        this.mobile = mobile;
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
