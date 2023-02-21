package com.example.trackit;

import android.app.Application;

import utils.FirebaseDataManagement;
import utils.MediaDataCollector;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MediaDataCollector.init(this);

        FirebaseDataManagement.init();
    }
}
