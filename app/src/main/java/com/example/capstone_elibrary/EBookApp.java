package com.example.capstone_elibrary;

import android.app.Application;
import android.content.Context;

public class EBookApp extends Application {

    private BookDb db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = new BookDb(this);
    }

    public static BookDb getDB(Context context) {
        return ((EBookApp)context.getApplicationContext()).db;
    }


    @Override
    public void onTerminate() {
        if (db!=null) db.close();

        super.onTerminate();
    }
}