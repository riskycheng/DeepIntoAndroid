package com.jian.deepintoandroid;

import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Main2Activity extends AppCompatActivity {
    private final static String TAG = Main2Activity.class.getSimpleName();

    private Button mBtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main);
        mBtn = findViewById(R.id.button);

        // 1. 通过Runnable使用线程池
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        executorService1.submit(runnable);
        // or
        executorService1.execute(runnable);


        Callable<Integer> mCallable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000L);
                }
                return 666;
            }
        };

        // 2. 通过Callable直接使用线程池
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService2.submit(mCallable);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int result = future.get();
                    Log.e(TAG, "result from Future-Callable is >>> " + result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



        // 3. 创建FutureTask用于线程池
        FutureTask<Integer> futureTask = new FutureTask<>(mCallable);
        ExecutorService executorService3 = Executors.newSingleThreadExecutor();
        executorService3.submit(futureTask);
        try {
            int result = futureTask.get();
            Log.e(TAG, "result from FutureTask is >>> " + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 4. 自定义FutureTask
        MyFutureTask myFutureTask = new MyFutureTask(mCallable);
        ExecutorService executorService4 = Executors.newSingleThreadExecutor();
        executorService4.submit(myFutureTask);
    }

    private class MyFutureTask extends FutureTask<Integer> {

        public MyFutureTask(Callable<Integer> callable) {
            super(callable);
        }

        @Override
        protected void done() {
            super.done();
            try {
                Log.e(TAG, "MyFutureTask >>> finish computation >>> " + this.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
