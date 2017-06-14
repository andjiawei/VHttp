package com.jiawei.httplib.okhttp;

import com.jiawei.httplib.callback.DisposeDataHandle;
import com.jiawei.httplib.callback.DisposeProgressListener;
import com.jiawei.httplib.callback.FileCallback;
import com.jiawei.httplib.callback.JsonCallback;
import com.jiawei.httplib.callback.UploadCallBack;
import com.jiawei.httplib.cookie.SimpleCookieJar;
import com.jiawei.httplib.https.HttpsUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jiawei on 2017/6/9.
 * <p>
 * okhttp的代理封装类
 */

public class OkhttpEngine {
    private static final int TIME_OUT = 30;

    private static OkHttpClient mOkHttpClient;

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

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 指定cilent信任指定证书
     *
     * @param certificates
     */
    public static void setCertificates(InputStream... certificates) {
        mOkHttpClient.newBuilder().sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null)).build();
    }

    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new JsonCallback(handle));
        return call;
    }

    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new JsonCallback(handle));
        return call;
    }

    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new FileCallback(handle));
        return call;
    }

    public static Call uploadFile(String url, File file, Map<String, String> params, final DisposeProgressListener listener) {
        //todo 抽取内容 FormBody
        RequestBody formBody;
        if (file == null) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder, params);
            formBody = builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder, params);

            RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())),file);
            builder.addFormDataPart(file.getName(), file.getName(), fileBody);
            formBody = builder.build();
        }

        CountingRequestBody countingRequestBody = new CountingRequestBody(formBody, new CountingRequestBody.Listener()
        {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength)
            {
                // TODO: 切换到主线程
                listener.onProgress(bytesWritten * 1.0f / contentLength);

            }
        });

        Request request = new Request.Builder()
                .url(url)
                .post(countingRequestBody)
                .build();


        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new UploadCallBack(listener));
        return call;
    }

    private static String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try
        {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private static void addParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private static void addParams(FormBody.Builder builder, Map<String, String> params) {

        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }

    }

}
