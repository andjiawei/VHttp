package com.jiawei.httplib.callback;

/**
 * 监听下载进度
 */
public interface DisposeProgressListener extends DisposeDataListener {
	void onProgress(float progress);
}
