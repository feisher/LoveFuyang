package com.yusong.lovefuyang.api;

import retrofit2.Retrofit;


public class ServiceModule {
    CommonService provideCommonService(Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

}
