package com.example.geek.moviesapp.Trailer;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.example.geek.moviesapp.DataProcess.Connector;

import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.example.geek.moviesapp.MainActivity.API_KEY;
import static com.example.geek.moviesapp.MainActivity.api;

public class TrailerLoader extends AsyncTaskLoader {
    private String movieId;

    private String buildUrl() {
        Uri uri = Uri.parse(api);
        Uri.Builder builder = uri.buildUpon();
        builder.appendPath(movieId);
        builder.appendPath("videos");
        builder.appendQueryParameter("api_key", API_KEY);
        return builder.build().toString();
    }
    public static String createYoutubeThumbnailUrl(String id) {

        String thumbnailUrl;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("img.youtube.com")
                .appendPath("vi")
                .appendPath(id)
                .appendPath("hqdefault.jpg");

        thumbnailUrl = builder.build().toString();

        return thumbnailUrl;
    }



    public TrailerLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        return Connector.extractTrailer(Connector.fetchingData(buildUrl()));
    }
}
