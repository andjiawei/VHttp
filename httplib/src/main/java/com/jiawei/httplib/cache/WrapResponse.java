package com.jiawei.httplib.cache;

import okhttp3.Response;

/**
 * Created by jiawei on 2017/7/4.
 * 缓存时需要保存数据,但Response未序列化，body.string多次调用会抛异常，因此
 * 封装此类，好做缓存，且回调给外部
 */

public class WrapResponse {

    private Response mResponse;
    private String bodyString;

    public WrapResponse() {
    }

    public WrapResponse(Response response, String bodyString) {
        mResponse = response;
        this.bodyString = bodyString;
    }

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

    public String getBodyString() {
        return bodyString;
    }

    public void setBodyString(String bodyString) {
        this.bodyString = bodyString;
    }
}
