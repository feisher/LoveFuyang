package com.yusong.lovefuyang.api;


import com.yusong.configlibrary.http.HttpResult;
import com.yusong.lovefuyang.entity.request.Login;
import com.yusong.lovefuyang.entity.response.LoginResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wenxin
 * contact with yangwenxin711@gmail.com
 */
public interface CommonService {

    //登陆
    @POST("/api/v1/user/login.htm")
    Observable<HttpResult<LoginResult>> login(@Body Login route);

}

