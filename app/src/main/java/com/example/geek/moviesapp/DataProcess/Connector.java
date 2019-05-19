package com.example.geek.moviesapp.DataProcess;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.geek.moviesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.geek.moviesapp.MainActivity.API_KEY;
import static com.example.geek.moviesapp.MainActivity.api;

public class Connector  {
    private static final String LOG = Connector.class.getSimpleName();


    private static URL createUrl(String requestedURL) {
        URL url = null;
        try {
            url = new URL(requestedURL);
        } catch (MalformedURLException exception) {
            Log.e(LOG, "Error with creating URL ", exception);
        }
        return url;
    }

    public static String fetchingData(String url) {

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(createUrl(url));
        } catch (IOException e) {
            Log.e(LOG, "Error closing input stream", e);
        }

        return jsonResponse;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG, "Error response code: " + urlConnection.getResponseCode());
                Log.i("URL", url.toString());
            }
        } catch (IOException e) {
            Log.e(LOG, "Problem retrieving the news JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            Log.i("URL", url.toString());
            return jsonResponse;
        }
    }

    private static String readFromStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
    public static List<DataEncap> jsonProcess(String jsonFile) {
        List<DataEncap> movies = new ArrayList<>();


        try {
            JSONObject root = new JSONObject(jsonFile);
            JSONArray results = root.optJSONArray(KeyTags.rootKey);

            for (int i = 0; i < results.length(); i++) {

                JSONObject object = results.getJSONObject(i);
                String posterPath = object.getString(KeyTags.posterKey);
                String movieTitle = object.getString(KeyTags.titleKey);
                String voteAverage = object.getString(KeyTags.voteAverageKey);
                String overview = object.getString(KeyTags.overViewKey);
                String releaseDate = object.getString(KeyTags.releasDateKey);
                String movieId = object.getString(KeyTags.idKey);

                movies.add(new DataEncap(movieId, movieTitle, voteAverage, overview, posterPath, releaseDate));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG, "Problem parsing the movies JSON results", e);
        }
        return movies;
    }


}

