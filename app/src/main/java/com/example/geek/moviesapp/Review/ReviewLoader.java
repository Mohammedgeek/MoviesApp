package com.example.geek.moviesapp.Review;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.example.geek.moviesapp.DataProcess.Connector;
import com.example.geek.moviesapp.Review.Review;

import java.util.List;

import static com.example.geek.moviesapp.MainActivity.API_KEY;
import static com.example.geek.moviesapp.MainActivity.api;

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {
    private String movieId;

    private String buildUrl() {
        Uri uri = Uri.parse(api);
        Uri.Builder builder = uri.buildUpon();
        builder.appendPath(movieId);
        builder.appendPath("reviews");
        builder.appendQueryParameter("api_key", API_KEY);
        return builder.build().toString();
    }

    public ReviewLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public List<Review> loadInBackground() {
        return Connector.extractReviews(Connector.fetchingData(buildUrl()));
    }
}