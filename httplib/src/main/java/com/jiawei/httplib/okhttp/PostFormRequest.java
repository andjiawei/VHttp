package com.jiawei.httplib.okhttp;


import com.jiawei.httplib.callback.DisposeProgressListener;

import okhttp3.RequestBody;

/**
 * Created by jiawei on 2017/6/14.
 */

public class PostFormRequest  {

    public PostFormRequest(){

    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final DisposeProgressListener listener)
    {
        if (listener == null) return requestBody;
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener()
        {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength)
            {
                // TODO: 切换到主线程
                listener.onProgress((int) (bytesWritten * 1.0f / contentLength/contentLength));

            }
        });
        return countingRequestBody;
    }
}
