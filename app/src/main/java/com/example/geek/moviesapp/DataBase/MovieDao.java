package com.example.geek.moviesapp.DataBase;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.geek.moviesapp.DataProcess.DataEncap;

import java.util.List;
@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<DataEncap>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE movieId = :id")
    LiveData<DataEncap> loadMovie(String id);

    @Insert
    void insertMovie(DataEncap movies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(DataEncap movies);

    @Delete
    void deleteMovie(DataEncap movies);
}
