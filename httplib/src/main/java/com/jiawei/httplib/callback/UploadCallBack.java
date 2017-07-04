package com.jiawei.httplib.callback;

import android.os.Handler;
import android.os.Looper;

import com.jiawei.httplib.cache.WrapResponse;
import com.jiawei.httplib.exception.OkHttpException;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by jiawei on 2017/6/14.
 */

public abstract class UploadCallBack extends ICallback {

    // TODO: 2017/6/14 统一错误码
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int IO_ERROR = -2; // the JSON relative error
    protected final String EMPTY_MSG = "";

    private Handler mHandler;
    public UploadCallBack() {
        mHandler=new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                failure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    public abstract void failure(OkHttpException e);

    @Override
    public void onResponse(Call call, final WrapResponse response) throws IOException {
        final String result = response.getBodyString();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                success(result);
            }
        });
    }

    public abstract void success(String result);

    public abstract void onProgress(float progress);

}
