package com.example.geek.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geek.moviesapp.DataProcess.DataEncap;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView mMovieTitle;
    TextView mOverview;
    TextView mRateAverage;
    TextView mReleaseDate;
    ImageView mThumbnail;
    Intent intent;
    DataEncap movies;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovieTitle = findViewById(R.id.movie_title);
        mOverview = findViewById(R.id.overview);
        mRateAverage = findViewById(R.id.movie_rate);
        mReleaseDate = findViewById(R.id.release_date);
        mThumbnail = findViewById(R.id.movie_thumbnail);

        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("movieDetails")) {
                Bundle bundle = intent.getExtras();
                assert bundle != null;
                movies = bundle.getParcelable("movieDetails");
            }
        }
        assert movies != null;
        mMovieTitle.setText(movies.getTitle());
        mOverview.setText(movies.getOverview());
        mRateAverage.setText(movies.getRate());
        mReleaseDate.setText(movies.getReleaseDate());
        String moviePosterUrl = movies.getFullImageUrl();
        Picasso.with(this).load(moviePosterUrl).into(mThumbnail);
    }
}
