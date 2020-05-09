package com.jian.deepintoandroid.sychronizedTest;

import android.util.Log;

public class Class_A {
    private final static String TAG = Class_A.class.getSimpleName();
    private Object mObject = new Object();

    private static Object mStaticObject = new Object();

    public void func_1() {
        for(int i = 0; i < 10000; i++)
            Log.d(TAG, "func_1@" + Thread.currentThread().getName());
    }

    public synchronized void func_2(){
        for(int i = 0; i < 10000; i++)
            Log.d(TAG, "func_2@" + Thread.currentThread().getName());
    }

    public void func_3() {
        synchronized (mObject){
            for(int i = 0; i < 10000; i++)
                Log.d(TAG, "func_3@" + Thread.currentThread().getName());
        }
    }


    public void func_4() {
        synchronized (mStaticObject){
            for(int i = 0; i < 10000; i++)
                Log.d(TAG, "func_4@" + Thread.currentThread().getName());
        }
    }

    public static void func_5(){
        for(int i = 0; i < 10000; i++)
            Log.d(TAG, "func_5@" + Thread.currentThread().getName());
    }

    public synchronized static void func_6(){
        for(int i = 0; i < 10000; i++)
            Log.d(TAG, "func_6@" + Thread.currentThread().getName());
    }

    public synchronized static void func_7(){
        for(int i = 0; i < 10000; i++)
            Log.d(TAG, "func_7@" + Thread.currentThread().getName());
    }


    public void func_8() {
        synchronized (this){
            for(int i = 0; i < 10000; i++)
                Log.d(TAG, "func_8@" + Thread.currentThread().getName());
        }
    }


    public synchronized void func_9(){
        for(int i = 0; i < 10000; i++)
            Log.d(TAG, "func_9@" + Thread.currentThread().getName());
    }

    public synchronized void func_10(){
        for(int i = 0; i < 10000; i++)
            Log.d(TAG, "func_10@" + Thread.currentThread().getName());
    }


    public void func_11() {
        synchronized (this){
            for(int i = 0; i < 10000; i++)
                Log.d(TAG, "func_11@" + Thread.currentThread().getName());
        }
    }


    public void func_12() {
        synchronized (this){
            for(int i = 0; i < 10000; i++)
                Log.d(TAG, "func_12@" + Thread.currentThread().getName());
        }
    }




}
