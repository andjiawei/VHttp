package com.jiawei.httplib.builder;

import com.jiawei.httplib.callback.ICallback;
import com.jiawei.httplib.request.BaseRequest;
import com.jiawei.httplib.request.GetRequest;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * Created by jiawei on 2017/6/21.
 * get的请求参数
 */

public class GetBuilder extends BaseBuilder<GetBuilder> {

    @Override
    public BaseRequest createRequest(ICallback callback) {
        BaseRequest getRequest=new GetRequest();
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();

        Request request = new Request.Builder().
                url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .tag(tag)
                .headers(mHeader)
                .build();
        getRequest.mRequest=request;
        getRequest.mCacheMode=mMode;
        getRequest.cacheKey=cacheKey;
        return getRequest;
    }

}
