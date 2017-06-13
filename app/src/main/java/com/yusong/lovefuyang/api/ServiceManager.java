package com.yusong.lovefuyang.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.socks.library.KLog;
import com.yusong.configlibrary.ConfigApplication;
import com.yusong.configlibrary.utils.Constants;
import com.yusong.configlibrary.utils.SPUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceManager {

    private  CommonService mCommonService;
    private static Gson mGsonDateFormat;
    private static class SingletonHolder {
        private static final ServiceManager INSTANCE = new ServiceManager();
    }
    public static ServiceManager getInstance() {
        mGsonDateFormat = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        return SingletonHolder.INSTANCE;
    }
    public CommonService getCommonService() {
        if (mCommonService == null) {
            synchronized (ServiceManager.class) {
                if (mCommonService == null) {
                    Retrofit mRetrofit = new Retrofit.Builder()
                            .baseUrl(Constants.BASEURL)
                            .client(getOkHttpClient(false))
                            .addConverterFactory(GsonConverterFactory.create(mGsonDateFormat))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                    mCommonService = mRetrofit.create(CommonService.class);
                    return mCommonService;
                }
            }
        }
        return mCommonService;
    }
    public OkHttpClient getOkHttpClient(boolean agent) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(10, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(10, TimeUnit.SECONDS);
        File cacheDir = ConfigApplication.getInstance().getCacheDir();
        File httpCacheDirectory = new File(cacheDir, "OkHttpCache");
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
//        //设置拦截器
        httpClientBuilder.addInterceptor(new CommonHeaderInterceptor());
        httpClientBuilder.addInterceptor(new LoggingInterceptor());
        // 添加证书
        setCertificates(httpClientBuilder, ConfigApplication.getContext());
        httpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return httpClientBuilder.build();
    }

    /**
     * 添加https证书
     */
    public void setCertificates(OkHttpClient.Builder builder, Context context) {
        try {
            List<InputStream> certificates =new ArrayList<>();
            certificates.add(context.getAssets().open("tomcat.crt"));//添加一个证书
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {if (certificate != null) certificate.close();
                } catch (IOException e) {}
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {e.printStackTrace();}
    }
    class CommonHeaderInterceptor implements Interceptor {
        public CommonHeaderInterceptor() {
        }
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.header("Authorization","Bearer "+ SPUtils.get("token",""));
            return chain.proceed(builder.build());
        }
    }
     class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException{
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            //=========发送===========
            Response response = null;
            Request request = chain.request();
            long requestTime = System.currentTimeMillis();//请求发起的时间
            HttpUrl requestUrl=request.url();
            Connection requestConnection=chain.connection();
            Headers requestHeaders=request.headers();

            Buffer requestbuffer = new Buffer();
            parseParams(request.body(), requestbuffer);
            if (request.body() != null) {
                request.body().writeTo(requestbuffer);
            } else {
                KLog.d("request.body() == null");
            }

            //打印发送信息
            KLog.d("请求地址："+requestUrl);
//                KLog.d("requestConnection="+requestConnection);
            KLog.d("请求头："+requestHeaders);
            KLog.d("请求体："+requestbuffer);
            //=========接收===========
            long responseTime = System.currentTimeMillis();//收到响应的时间
            response = chain.proceed(chain.request());
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            HttpUrl responseUrl=response.request().url();
            String content = response.body().string();
            Headers responseHeaders=response.headers();
            long delayTime=responseTime-requestTime;
            //打印接收信息
//            KLog.d("responseUrl="+responseUrl);
//            KLog.d("responseHeaders="+responseHeaders);
            KLog.d("delayTime="+delayTime);
            KLog.json(content);
            return chain.proceed(request);
        }
        @NonNull
        public  String parseParams(RequestBody body, Buffer requestbuffer) throws UnsupportedEncodingException {
            if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
                return URLDecoder.decode(requestbuffer.readUtf8(), "UTF-8");
            }
            return "null";
        }
    }

}
