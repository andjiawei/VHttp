package com.jiawei.httplib.callback;

/**
 * okhttp的callback回调封装
 */
public interface DisposeDataListener {

	/**
	 * 请求成功回调事件处理
	 */
	void onSuccess(Object responseObj);

	/**
	 * 请求失败回调事件处理
	 */
	void onFailure(Object reasonObj);

}
