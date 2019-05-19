package com.example.geek.moviesapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.geek.moviesapp.DataProcess.Connector;
import com.example.geek.moviesapp.DataProcess.DataEncap;
import com.example.geek.moviesapp.R;

import java.util.List;

import static com.example.geek.moviesapp.MainActivity.API_KEY;
import static com.example.geek.moviesapp.MainActivity.api;

public class MovieLoader extends AsyncTaskLoader<List<DataEncap>> {

    private static String mSortBy;

    public MovieLoader(Context context) {
        super(context);
    }

    private String buildUrl() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = sharedPref.getString(
                getContext().getString(R.string.sort_by_key),
                getContext().getString(R.string.sort_by_popular_value));
        Uri uri = Uri.parse(api);
        Uri.Builder builder = uri.buildUpon();
        builder.appendPath(sortBy);
        builder.appendQueryParameter("api_key", API_KEY);
        return builder.build().toString();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<DataEncap> loadInBackground() {
        return Connector.jsonProcess(Connector.fetchingData(buildUrl()));
    }
}
