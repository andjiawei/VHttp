package com.jiawei.httplib.builder;


import com.jiawei.httplib.callback.ICallback;
import com.jiawei.httplib.request.RequestCall;

import java.util.Map;

import okhttp3.Request;

/**
 * Created by jiawei on 2017/6/21.
 */

public abstract class BaseBuilder {

    String url;
    Object tag;
    Map<String, String> headers;
    Map<String, String> params;

    public BaseBuilder url(String url){
        this.url=url;
        return this;
    }

    public BaseBuilder tag(Object tag){
        this.tag=tag;
        return this;
    }

    public BaseBuilder params(Map<String, String> params){
        this.params=params;
        return this;
    }

    public BaseBuilder header(Map<String, String> headers){
        this.headers=headers;
        return this;
    }

    public RequestCall build() {
        return new RequestCall(this);
    }

    public abstract Request createRequest(ICallback callback);
}
