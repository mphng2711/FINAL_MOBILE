package com.example.purepawapp;

import android.app.Application;

import com.example.purepawapp.data.seed.AppBootstrap;
import com.example.purepawapp.di.ServiceLocator;

import java.util.concurrent.Executors;

public class PurePawApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceLocator.init(this);

        if (BuildConfig.DEBUG) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    AppBootstrap.runIfNeeded();
                } catch (Exception ignored) {
                }
            });
        }
    }
}
