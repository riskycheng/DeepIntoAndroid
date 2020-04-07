package com.jian.deepintoandroid;

import android.util.Log;

public class MyRunnable implements Runnable {
    private final static String TAG = MyRunnable.class.getSimpleName();

    public MyRunnable(String name) {
    }

    @Override
    public void run() {
        Log.d(TAG, "MyRunnable running...");
        Thread thread = Thread.currentThread();
        Log.e(TAG, thread.getName() + " id = " + thread.getId());
        try {
            Log.d(TAG, "starting looping ...");
            for (int i = 0; i < 20 * 1000; i ++) {
                Log.d(TAG, "index:" + i);
            }
            Log.d(TAG, "stopping looping ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "MyRunnable finished...");
    }
}
