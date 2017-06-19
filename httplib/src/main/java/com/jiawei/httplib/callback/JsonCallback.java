package com.jiawei.httplib.callback;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.jiawei.httplib.exception.OkHttpException;
import com.jiawei.httplib.utils.GsonUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by jiawei on 2017/6/12.
 */

public abstract class JsonCallback<T> extends ICallback<T> {

    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error
    protected final String EMPTY_MSG = "";

    protected final String COOKIE_STORE = "Set-Cookie"; // decide the server it

    private  Handler mHandler;

    public JsonCallback() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                failure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    public abstract void failure(OkHttpException e) ;

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
//                if (mListener instanceof DisposeHandleCookieListener) {
//                    ((DisposeHandleCookieListener) mListener).onCookie(cookieLists);
//                }
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
            failure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
//            Type type =((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
          Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (type == String.class) {
                success((T)result);
            } else {
                T obj= GsonUtils.get().fromJson(result,type);
                if (obj != null) {
                    success(obj);
                } else {
                    failure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            failure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }

    protected abstract void success(T obj);
}
