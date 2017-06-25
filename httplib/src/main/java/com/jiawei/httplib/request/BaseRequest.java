package com.jiawei.httplib.request;

import com.jiawei.httplib.callback.ICallback;

import okhttp3.Request;

/**
 * Created by jiawei on 2017/6/23.
 */

public class BaseRequest {
    
    //真正的request
    public Request mRequest;

    public Request createRequest(ICallback callback) {
        return mRequest;
    }
}
