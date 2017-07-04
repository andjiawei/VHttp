package com.jiawei.httplib.request;

import com.jiawei.httplib.builder.BaseBuilder;
import com.jiawei.httplib.callback.ICallback;
import com.jiawei.httplib.okhttp.OkhttpEngine;

import okhttp3.Call;

/**
 * Created by jiawei on 2017/6/21.
 *
 * 调用builder时创建
 * Build本为分离{@link OkhttpEngine}参数，但执行回调需同时有Request和Callback
 * 内部需隐藏Request实现细节，只暴露外部Callback即可
 * 如此，需将Request提升为成员变量，这样多线程调用可能出错
 * 这里引入此类，提取出Request实现细节，且和Callback结合。
 *
 * 功能：封装Request 和 Callback
 */
public class RequestCall {

    private final BaseBuilder mBuilder;

    public RequestCall(BaseBuilder request) {
        mBuilder =request;
    }

    public Call execute(final ICallback callback ){
        BaseRequest request = buildCallback(callback);
        Call call = OkhttpEngine.getInstance().execute(request,callback);
        return call;
    }

    /**
     * 讲callback回中的onProgress加入到RequestBody中
     * 由于只有上传文件需要，需要分条件区分
     *
     */
    private BaseRequest buildCallback(final ICallback callback) {
        BaseRequest request = mBuilder.createRequest(callback);
        return request ;
    }
}
