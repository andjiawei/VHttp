package com.jiawei.httplib.callback;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.jiawei.httplib.exception.OkHttpException;
import com.jiawei.httplib.utils.GsonUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by jiawei on 2017/6/12.
 */

public class JsonCallback<T> extends ICallback<T> {

    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error
    protected final String EMPTY_MSG = "";

    protected final String COOKIE_STORE = "Set-Cookie"; // decide the server it

    private  DisposeDataListener mListener;
    private  Handler mHandler;

    public JsonCallback(DisposeDataListener listener) {
        mListener = listener;
        mHandler = new Handler(Looper.getMainLooper());
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
        final ArrayList<String> cookieLists = handleCookie(response.headers());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // TODO: 2017/6/18 转换线程处理
                handleResponse(result);
                /**
                 * handle the cookie
                 */
                if (mListener instanceof DisposeHandleCookieListener) {
                    ((DisposeHandleCookieListener) mListener).onCookie(cookieLists);
                }
            }
        });
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

    private void handleResponse(String result) {
        if (TextUtils.isEmpty(result)) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            Type type =((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//          Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//            String classname=type.toString().split(" ")[1];
            if (type instanceof Class)
            {
                throw new RuntimeException("Missing type parameter.");
            }
            if (type == String.class) {
//                mListener.onSuccess(result);
            } else {
                T obj= GsonUtils.get().fromJson(result,type);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}
