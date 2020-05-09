package com.jian.deepintoandroid.sychronizedTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jian.deepintoandroid.R;

public class SynchronziedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronzied);
        Class_A class_a = new Class_A();

        new Thread(new Runnable() {
            @Override
            public void run() {

                class_a.func_5();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                class_a.func_1();
            }
        }).start();
    }
}
