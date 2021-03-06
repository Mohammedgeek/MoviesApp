package com.example.geek.moviesapp.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.geek.moviesapp.DataProcess.DataEncap;

@Database(entities = {DataEncap.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "FavoriteList";
    private static MovieDatabase sInstance;

    public static MovieDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d("DB", "Creating the DB");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        MovieDatabase.class,
                        MovieDatabase.DATABASE_NAME).build();
            }
        }
        Log.d("DB", "Getting DB instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();


}
