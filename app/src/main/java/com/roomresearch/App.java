package com.roomresearch;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.roomresearch.db.AppDatabase;

/**
 * Created by dima on 6/30/17.
 */

public class App extends Application {

    public static final String DATABASE_NAME = "database-name";

    private static AppDatabase appDatabase;
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }

    public static AppDatabase getAppDatabase(){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(applicationContext,
                    AppDatabase.class, DATABASE_NAME).addMigrations(AppDatabase.MIGRATION_1_2).build();
        }
        return appDatabase;
    }
}
