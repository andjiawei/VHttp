package com.jiawei.httplib.request;

import com.jiawei.httplib.builder.BaseBuilder;
import com.jiawei.httplib.callback.ICallback;
import com.jiawei.httplib.okhttp.OkhttpEngine;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by jiawei on 2017/6/21.
 *
 * 调用builder时创建
 * Build本为分离{@link OkhttpEngine}参数，但执行回调需同时有Request和Callback
 * 内部需隐藏Request实现细节，只暴露外部Callback即可
 * 如此，需将Request提升为成员变量，这样多线程调用可能出错
 * 这里引入此类，提取出Request实现细节，且和Callback结合。
 *
 * 功能：封装Request 和 Callback 且可利用Call暴露更多方法
 */
public class RequestCall {

    private final BaseBuilder mRequest;

    public RequestCall(BaseBuilder request) {
        mRequest =request;
    }

    public Call execute(final ICallback callback ){
        Request request = buildCallback(callback);
        Call call = OkhttpEngine.getInstance().execute(request,callback);
        return call;
    }

    /**
     * 讲callback回中的onProgress加入到RequestBody中
     * 由于只有上传文件需要，需要分条件区分
     * todo 区分使用条件
     *
     */
    private Request buildCallback(final ICallback callback) {
        Request request =mRequest.createRequest(callback);
        return request ;
    }
}
