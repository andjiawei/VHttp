package com.jiawei.httplib.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by jiawei on 2017/6/19.
 */

public abstract class ICallback<T> {

   public abstract void onFailure(Call call, IOException e);

    public abstract void onResponse(Call call, Response response) throws IOException;
}
