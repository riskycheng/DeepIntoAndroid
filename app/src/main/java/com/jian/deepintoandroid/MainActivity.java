package com.jian.deepintoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStart = findViewById(R.id.btnStart);
    }

    /**
     * callable and future task
     */
    Callable<String> myCallable = new Callable<String>() {
        @Override
        public String call() throws Exception {
            Log.d(TAG, "myCallable enters...");
            Thread.sleep(8000L);
            Log.d(TAG, "myCallable exits...");
            return "callable-ended";
        }
    };

    FutureTask<String> futureTask = new FutureTask<>(myCallable);

    public void BtnClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                Log.d(TAG, "btnStart clicked @Thread : " + Thread.currentThread().getName());

                //create executor Pool
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                //submit and execute
                Future<String> future = executorService.submit(myCallable);

                try {
                    String result = future.get();
                    Log.d(TAG, "got result externally from future >>> " + result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnCapture:
                break;
        }
    }
}
