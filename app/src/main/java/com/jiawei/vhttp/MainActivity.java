package com.jiawei.vhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jiawei.httplib.callback.DisposeProgressListener;
import com.jiawei.httplib.okhttp.OkhttpEngine;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
