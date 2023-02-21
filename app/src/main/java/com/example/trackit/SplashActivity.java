package com.example.trackit;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;


public class SplashActivity extends AppCompatActivity {

    private ProgressBar splash_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        findViews();

        startAnimation(splash_progressBar);
    }

    private void startAnimation(View view) {
    }

    private void startApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void findViews() {
        splash_progressBar = findViewById(R.id.splash_progressBar);
    }

}
