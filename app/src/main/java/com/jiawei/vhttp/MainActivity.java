package com.jiawei.vhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jiawei.httplib.callback.FileCallback;
import com.jiawei.httplib.callback.JsonCallback;
import com.jiawei.httplib.callback.UploadCallBack;
import com.jiawei.httplib.exception.OkHttpException;
import com.jiawei.httplib.okhttp.OkhttpEngine;
import com.jiawei.httplib.okhttp.RequestParams;
import com.jiawei.httplib.other.User;

import java.io.File;

import static com.jiawei.vhttp.R.id.get;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.get).setOnClickListener(this);
        findViewById(R.id.post).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case get:
                get();
                break;
            case R.id.post:
                post();
                break;
            case R.id.download:
                download();
                break;

            case R.id.upload:
                upload();
                break;
        }
    }

    private void get(){
        String url="http://api.nohttp.net/jsonObject";
        RequestParams params= new RequestParams();
        params.put("name","yanzhenjie");
        params.put("pwd","123");
        OkhttpEngine.get(url, params, null, new JsonCallback<User>() {
            @Override
            public void failure(OkHttpException e) {
                Log.e("111", "failure: 失败" );
            }

            @Override
            public void success(User obj) {
                Log.e("111", "success: 成功"+obj );
            }
        });
    }
    private void post(){
        //这里替换为你的url 这个貌似失效了
        String url="http://api.nohttp.net/jsonArray";
        RequestParams params=new RequestParams();
        params.put("name","yanzhenjie");
        params.put("pwd","123");
        OkhttpEngine.post(url, params, null, new JsonCallback<String>() {
            @Override
            public void success(String obj) {
                Log.e("111", "success: "+obj );
            }

            @Override
            public void failure(OkHttpException e) {

            }
        });
    }
    private void download(){
        String url = "http://172.30.68.144/dldir1.qq.com/qqfile/qq/TIM1.1.5/21175/TIM1.1.5.exe";
        //todo 修改路径
        String path = Environment.getExternalStorageDirectory() + File.separator + "test.exe";

        OkhttpEngine.post(url, null, null, new FileCallback(path) {
            @Override
            protected void failure(OkHttpException e) {
                Log.e("222", "failure: " );
            }

            @Override
            protected void success(File file) {
                Log.e("222", "success: " );
            }

            @Override
            protected void progress(int progress) {
                Log.e("222", "progress: " +progress);
            }
        });
    }

    private void upload() {
        File file = new File(Environment.getExternalStorageDirectory()+"/download/", "333.zip");
        if (!file.exists())
        {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        OkhttpEngine.uploadFile("http://api.nohttp.net/upload", file, null, new UploadCallBack() {
            @Override
            public void failure(OkHttpException e) {
                Log.e("111", "failure: ");
            }

            @Override
            public void success(String result) {
                Log.e("111", "success: ");
            }

            @Override
            public void onProgress(float progress) {
                Log.e("111", "onProgress: ");
            }
        });
    }
}
