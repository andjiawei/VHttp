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
import com.jiawei.httplib.other.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.jiawei.vhttp.R.id.get;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="MainActivity";

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
        Map<String,String> params= new HashMap<>();
        params.put("name","yanzhenjie");
        params.put("pwd","123");
        OkhttpEngine.get().url(url).params(params).tag(this)
                .build().execute(new JsonCallback<User>() {
            @Override
            public void success(User obj) {
                Log.e("get", "success: "+obj.toString());
            }

            @Override
            public void failure(OkHttpException e) {
                Log.e("get", "failure: "+e.toString());
            }
        });

    }
    private void post(){
        //这里替换为你的url 这个貌似失效了
        String url="http://api.nohttp.net/jsonArray";
        Map<String,String> params=new HashMap<>();
        params.put("name","yanzhenjie");
        params.put("pwd","123");
        OkhttpEngine.post().url(url).params(params)
                .build().execute(new JsonCallback<String>() {
            @Override
            public void success(String obj) {
                Log.e("post", "success: "+obj.toString());
            }

            @Override
            public void failure(OkHttpException e) {
                Log.e("post", "failure: "+e.toString());
            }
        });
    }
    private void download(){
        String url = "https://qd.myapp.com/myapp/qqteam/AndroidQQi/qq_5.2.0.6068_android_r24710_GuanWang_537051119_release.apk";
        //todo 修改路径
        String path = Environment.getExternalStorageDirectory() + File.separator + "test.exe";

        OkhttpEngine.post().url(url).build().execute(new FileCallback(path) {
            @Override
            protected void failure(OkHttpException e) {
                Log.e("222", "failure: " +e.toString());
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
        String url="http://api.nohttp.net/upload";
        File file = new File(Environment.getExternalStorageDirectory()+"/download/", "333.zip");
        if (!file.exists())
        {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }

        OkhttpEngine.post().file(file).url(url).build()
                .execute(new UploadCallBack() {
                    @Override
                    public void failure(OkHttpException e) {
                        Log.e(TAG, "failure: "+e.toString() );
                    }

                    @Override
                    public void success(String result) {
                        Log.e(TAG, "success: "+result );

                    }

                    @Override
                    public void onProgress(float progress) {
                        Log.e(TAG, "success: "+progress );
                    }
                });
    }
}
