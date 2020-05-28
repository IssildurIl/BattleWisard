package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(SplashScreenLayout.this)
                .withFullScreen()
                .withTargetActivity(StartActivity.class)
                .withSplashTimeOut(4000)
                //.withBackgroundColor(Color.parseColor("#8093A4"))
                .withBackgroundResource(R.drawable.intro3);
        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
