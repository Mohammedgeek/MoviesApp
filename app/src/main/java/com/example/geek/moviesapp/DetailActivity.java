package com.example.geek.moviesapp;

import android.app.LoaderManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geek.moviesapp.DataBase.AppExecutor;
import com.example.geek.moviesapp.DataBase.MovieDatabase;
import com.example.geek.moviesapp.DataProcess.DataEncap;
import com.example.geek.moviesapp.Review.Review;
import com.example.geek.moviesapp.Review.ReviewAdapter;
import com.example.geek.moviesapp.Review.ReviewLoader;
import com.example.geek.moviesapp.Trailer.Trailer;
import com.example.geek.moviesapp.Trailer.TrailerAdapter;
import com.example.geek.moviesapp.Trailer.TrailerLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView mMovieTitle;
    TextView mOverview;
    TextView mRateAverage;
    TextView mReleaseDate;
    ImageView mThumbnail;
    Intent intent;
    DataEncap movies;

    ListView trailerList;

    ListView reviewList;

    CheckBox favoriteCheckBox;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;
    private String movieId;
    MovieDatabase movieDb;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovieTitle = findViewById(R.id.movie_title);
        mOverview = findViewById(R.id.overview);
        mRateAverage = findViewById(R.id.movie_rate);
        mReleaseDate = findViewById(R.id.release_date);
        mThumbnail = findViewById(R.id.movie_thumbnail);
        trailerList = findViewById(R.id.trailer_list_view);
        reviewList = findViewById(R.id.review_list_view);

        favoriteCheckBox = findViewById(R.id.favorite_checkbox);


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
        movieId = movies.getMovieId();
        Picasso.with(this).load(moviePosterUrl).into(mThumbnail);
        List<Trailer> trailers = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(DetailActivity.this, trailers);
        trailerList.setAdapter(trailerAdapter);

        List<Review> reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(DetailActivity.this, reviews);
        reviewList.setAdapter(reviewAdapter);

        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailerLoader).forceLoad();
        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, reviewLoader).forceLoad();

        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = (Trailer) parent.getItemAtPosition(position);
                trailerPlayer(trailer.getKey());
            }
        });
        movieDb = MovieDatabase.getsInstance(getApplicationContext());

        favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteCheckBox.isChecked()) {
                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDb.movieDao().insertMovie(movies);
                        }
                    });
                    Toast toast = Toast.makeText
                            (DetailActivity.this, "Movie has been added to fav", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDb.movieDao().deleteMovie(movies);
                        }
                    });
                    Toast toast = Toast.makeText
                            (DetailActivity.this, "Movie has been removed from fav", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        checkIfFav(movieId);
    }

    private void checkIfFav(String id) {
        LiveData<DataEncap> movie = movieDb.movieDao().loadMovie(id);
        movie.observe(this, new Observer<DataEncap>() {
            @Override
            public void onChanged(@Nullable DataEncap movies) {
                if (movies == null)
                    favoriteCheckBox.setChecked(false);
                else favoriteCheckBox.setChecked(true);
            }
        });
    }

    private void trailerPlayer(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }

    }
    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoader = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new TrailerLoader(DetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            if (data != null && !data.isEmpty()) {
                trailerAdapter.addAll(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };
    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoader = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewLoader(DetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            if (data != null && !data.isEmpty()) {
                reviewAdapter.addAll(data);
            }

        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };


}
