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
import com.jiawei.httplib.request.CacheMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;

import static com.jiawei.vhttp.R.id.get;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkhttpEngine.getInstance().init(getApplicationContext());
        findViewById(R.id.get).setOnClickListener(this);
        findViewById(R.id.post).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
        findViewById(R.id.json).setOnClickListener(this);
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
            case R.id.json:
                json();
                break;
        }
    }

    //todo 接口待测试
    private void json() {
        String url="http://api.nohttp.net/upload";
        OkhttpEngine.postString().mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content("{'user':'jiawei'}")
                .url(url).tag(this)
                .build().execute(new JsonCallback<String>() {
            @Override
            public void success(String obj) {
                Log.e("postString", "success: "+obj.toString());
            }

            @Override
            public void failure(OkHttpException e) {
                Log.e("postString", "failure: "+e.toString());
            }
        });
    }

    private void get(){
        String url="http://v.juhe.cn/weather/index";
        Map<String,String> params= new HashMap<>();
        //key=f5098008cfbfa83e7bfa222693077cdf
        params.put("cityname","郑州");
        params.put("key","f5098008cfbfa83e7bfa222693077cdf");
        OkhttpEngine.get().url(url).params(params).tag(this).cache(CacheMode.DefaultCache).cacheKey("defaultKey")
                .build().execute(new JsonCallback<String>() {
            @Override
            public void success(String obj) {
                Log.e("get", "success: "+obj.toString());
            }

            @Override
            public void failure(OkHttpException e) {
                Log.e("get", "failure: "+e.toString());
            }

            @Override
            public void onCache(Object data) {
                Log.e(TAG, "onCache: "+data.toString() );
            }
        });

    }
    private void post(){
        //这里替换为你的url 这个貌似失效了
        String url="http://v.juhe.cn/weather/uni";
        Map<String,String> params=new HashMap<>();
        params.put("key","f5098008cfbfa83e7bfa222693077cdf");
        OkhttpEngine.post().url(url).params(params).tag(this).cache(CacheMode.DefaultCache).cacheKey("postKey")
                .build().execute(new JsonCallback<String>() {
            @Override
            public void success(String obj) {
                Log.e("post", "success: "+obj.toString());
            }

            @Override
            public void failure(OkHttpException e) {
                Log.e("post", "failure: "+e.toString());
            }

            @Override
            public void onCache(Object data) {
                Log.e(TAG, "onCache: "+data.toString());
            }
        });
    }
    private void download(){
        String url = "http://gdown.baidu.com/data/wisegame/41a04ccb443cd61a/QQ_692.apk";
        //todo 修改路径
        String path = Environment.getExternalStorageDirectory() + File.separator + "test.exe";

        OkhttpEngine.post().url(url).tag(this).build().execute(new FileCallback(path) {
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
        File file2 = new File(Environment.getExternalStorageDirectory()+"/download/", "444.zip");
        if (!file.exists()||!file2.exists())
        {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }

        OkhttpEngine.post().addFile(file).addFile(file2).tag(this).url(url).build()
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

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: " );
        OkhttpEngine.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
