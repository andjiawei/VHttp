package com.jiawei.httplib.callback;

import com.jiawei.httplib.cache.WrapResponse;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by jiawei on 2017/6/19.
 */

public abstract class ICallback<T>{

   public abstract void onFailure(Call call, Exception e);

    public abstract void onResponse(Call call, WrapResponse response) throws IOException;

    public void onProgress(float progress) {
    }
    public void onCache(Object data){};

}
