package com.example.geek.moviesapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.geek.moviesapp.DataProcess.DataEncap;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<DataEncap>>, MovieAdapter.MovieAdapterOnClickHandler {


    RecyclerView moviesRecyclerView;


    MovieAdapter movieAdapter;


    ItemOffsetDecoration itemDecoration;

    NetworkInfo networkInfo;
    private static final int LOADER_ID = 0;


    public static String api = "https://api.themoviedb.org/3/movie";
    // put your own key to be able to run the app.
    public static final String API_KEY = "your_key";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.recview);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_portrait);
            moviesRecyclerView.setPadding
                    (getDimens(R.dimen.item_offset_portrait), getDimens(R.dimen.item_offset_portrait),
                            getDimens(R.dimen.item_offset_portrait), getDimens(R.dimen.item_offset_portrait));
        } else {
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_landscape);
            moviesRecyclerView.setPadding
                    (getDimens(R.dimen.item_offset_landscape), getDimens(R.dimen.item_offset_landscape),
                            getDimens(R.dimen.item_offset_landscape), getDimens(R.dimen.item_offset_landscape));
        }
        moviesRecyclerView.addItemDecoration(itemDecoration);

        movieAdapter = new MovieAdapter(new ArrayList<DataEncap>(), this);
        moviesRecyclerView.setAdapter(movieAdapter);

        if (networkInfo != null && networkInfo.isConnected()) {

            // getLoaderManager().initLoader(LOADER_ID, null, MainActivity.this).forceLoad();
            getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this).forceLoad();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (id) {
            case R.id.pupolar_action:
                editor.putString(getString(R.string.sort_by_key), getString(R.string.sort_by_popular_value));
                editor.apply();
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                break;
            case R.id.top_rated_action:
                editor.putString(getString(R.string.sort_by_key), getString(R.string.sort_by_rated_value));
                editor.apply();
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                break;
        }

        return true;
    }

    private int getDimens(int dimenSize) {
        return getResources().getDimensionPixelSize(dimenSize);
    }

    @Override
    public Loader<List<DataEncap>> onCreateLoader(int id, Bundle args) {

        return new MovieLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<DataEncap>> loader, List<DataEncap> data) {
        if (data != null && !data.isEmpty()) {
            movieAdapter.updateMovies(data);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<DataEncap>> loader) {

    }

    @Override
    public void OnMovieClick(DataEncap m) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieDetails", m);
        startActivity(intent);

    }
}

