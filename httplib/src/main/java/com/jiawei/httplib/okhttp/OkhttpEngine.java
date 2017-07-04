package com.jiawei.httplib.okhttp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.jiawei.httplib.builder.GetBuilder;
import com.jiawei.httplib.builder.PostBuilder;
import com.jiawei.httplib.builder.PostStringBuilder;
import com.jiawei.httplib.cache.CacheEntity;
import com.jiawei.httplib.cache.CacheManager;
import com.jiawei.httplib.cache.WrapResponse;
import com.jiawei.httplib.callback.ICallback;
import com.jiawei.httplib.cookie.SimpleCookieJar;
import com.jiawei.httplib.exception.OkHttpException;
import com.jiawei.httplib.https.HttpsUtils;
import com.jiawei.httplib.request.BaseRequest;
import com.jiawei.httplib.request.RequestCall;
import com.jiawei.httplib.utils.HeaderParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiawei on 2017/6/9.
 * <p>
 * okhttp的代理封装类
 */

public class OkhttpEngine {
    private final String TAG="OkhttpEngine";

    protected static final String COOKIE_STORE = "Set-Cookie"; // decide the server it
    private static final int TIME_OUT = 30;

    private static OkHttpClient mOkHttpClient;
    private static Handler mHandler = new Handler();

    static {
        //使用builder模式 做一些参数配置
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        //验证主机名 这个return true 即使不匹配也不报错 todo 待优化
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        /**
         *  为所有请求添加请求头，看个人需求
         */
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", "Android-Mobile") // 标明发送本次请求的客户端
                        .build();
                return chain.proceed(request);
            }
        });
        //可据此拿到coolie todo 待测试
        okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true);//支持重定向
        /**
         * trust all the https point
         */
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    private Context mContext;

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static OkhttpEngine instance = new OkhttpEngine();

    public static OkhttpEngine getInstance() {
        return instance;
    }

    //数据库需要使用到context
    public void init(Context context){
        mContext =context;
    }

    /**
     * 指定cilent信任指定证书
     *
     * @param certificates
     */
    public static void setCertificates(InputStream... certificates) {
        mOkHttpClient.newBuilder().sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null)).build();
    }


    /**
     * 获取getBuilder
     * 将get所需要的参数交给Builder
     * Request类在{@link RequestCall}
     *
     * @return {@link GetBuilder}
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostBuilder post() {
        return new PostBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostBuilder upload() {
        return null;
    }


    private ArrayList<String> handleCookie(Headers headers) {
        ArrayList<String> tempList = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).equalsIgnoreCase(COOKIE_STORE)) {
                tempList.add(headers.value(i));
            }

        }
        return tempList;
    }

    public Call execute(final BaseRequest request, final ICallback callback) {
        CacheEntity<Object> cacheEntity = CacheManager.getInstance().get(request.cacheKey);
        if(cacheEntity!=null){
            callback.onCache(cacheEntity.getData());
        }else{
            Log.e(TAG, ": 无缓存");
        }
        Call call = mOkHttpClient.newCall(request.mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                WrapResponse wrapResponse=new WrapResponse(response,response.body().string());
                if (response.code() == 404 || response.code() >= 500) {
                    //todo 完善异常信息
                    callback.onFailure(call, new OkHttpException(response.code(),"网络错误"));
                    return;
                }

                switch (request.mCacheMode){
                    case DefaultCache://缓存
                        Log.e(TAG, "DefaultCache: 缓存的小伙伴");
                        saveCache(request,wrapResponse);
                        break;
                    case NoCache://清除
                        Log.e(TAG, "NoCache: 要清除的小伙伴");
                        break;
                    case CacheData:
                        break;
                }

                callback.onResponse(call, wrapResponse);
            }
        });
        return call;
    }

    private void saveCache(BaseRequest request, WrapResponse response) throws IOException {

        CacheEntity<String> cache = HeaderParser.createCacheEntity(response.getResponse().headers(), response.getBodyString(), request.mCacheMode, request.cacheKey);
        if (cache == null) {
            //服务器不需要缓存，移除本地缓存
            CacheManager.getInstance().remove(request.cacheKey);
        } else {
            //缓存命中，更新缓存
            CacheManager.getInstance().replace(request.cacheKey,cache);
        }

    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void cancelAll() {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    public Context getContext() {
        return mContext;
    }
}
