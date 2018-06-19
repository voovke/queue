package com.example.osemenenko.queue;

import android.app.Application;

import io.realm.Realm;

public class QueueApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm (just once per application)
        Realm.init(getApplicationContext());

        try (Realm realm = Realm.getDefaultInstance()) {
            // Persist your data in a transaction
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
        }
    }

}
