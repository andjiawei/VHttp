package com.jiawei.httplib.callback;

import android.os.Handler;
import android.os.Looper;

import com.jiawei.httplib.exception.OkHttpException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jiawei on 2017/6/14.
 */

public class UploadCallBack implements Callback {

    // TODO: 2017/6/14 统一错误码
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int IO_ERROR = -2; // the JSON relative error
    protected final String EMPTY_MSG = "";
    private final DisposeProgressListener mListener;

    private Handler mHandler;
    public UploadCallBack(DisposeProgressListener listener) {
        mListener =listener;
        mHandler=new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
    }
}
