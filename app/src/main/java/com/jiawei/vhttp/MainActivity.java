package com.jiawei.vhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jiawei.httplib.callback.DisposeProgressListener;
import com.jiawei.httplib.callback.JsonCallback;
import com.jiawei.httplib.exception.OkHttpException;
import com.jiawei.httplib.okhttp.OkhttpEngine;
import com.jiawei.httplib.okhttp.RequestParams;
import com.jiawei.httplib.other.User;

import java.io.File;

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

            case R.id.get:
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
        String url="http://api.stay4it.com/v1/public/core/";
        RequestParams params= new RequestParams();
        params.put("service","user.getAll");
        OkhttpEngine.get(url, params, null, new JsonCallback<User>() {
            @Override
            public void failure(OkHttpException e) {
                Log.e("111", "failure: 失败" );
            }

            @Override
            protected void success(User obj) {
                Log.e("111", "success: 成功"+obj );
            }
        });
    }
    private void post(){}
    private void download(){}

    private void upload() {
        File file = new File(Environment.getExternalStorageDirectory()+"/download/", "333.zip");
        if (!file.exists())
        {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        OkhttpEngine.uploadFile("http://api.nohttp.net/upload", file, null, new DisposeProgressListener() {
            @Override
            public void onProgress(float progress) {
                Log.e("progress", "onProgress: "+progress );
            }

            @Override
            public void onSuccess(Object responseObj) {
                Log.e("onSuccess", "onSuccess: "+responseObj.toString() );

            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.e("onFailure", "onFailure: "+reasonObj.toString() );
            }
        });
    }
}
