package com.jian.deepintoandroid;

import android.util.Log;
import android.os.Process;

public class MyThread extends Thread {
    private final static String TAG = MyThread.class.getSimpleName();

    public MyThread(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        Log.d(TAG, "running function...");
        Thread thread = Thread.currentThread();
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Log.e(TAG, thread.getName() + " id = " + thread.getId());
        try {
            Log.d(TAG, "starting looping ...");
            for (int i = 0; i < 1 * 10; i ++) {
                Log.d(TAG, "index:" + i);
                Thread.sleep(1000L);
            }
            Log.d(TAG, "stopping looping ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "MyRunnable finished...");
    }
}
