package com.jiawei.httplib.request;

import okhttp3.Request;

/**
 * Created by jiawei on 2017/6/23.
 */

public class BaseRequest {
    public Request mRequest;
    public CacheMode mCacheMode=CacheMode.NoCache;
    public String cacheKey;
}
