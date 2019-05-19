package com.example.geek.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.geek.moviesapp.DataProcess.DataEncap;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<DataEncap> moviesList;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(List<DataEncap> movies, MovieAdapterOnClickHandler clickHandler) {
        moviesList = new ArrayList<>();
        moviesList.addAll(movies);
        mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void OnMovieClick(DataEncap m);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView MoviePoster;

        MovieViewHolder(View itemView) {
            super(itemView);
            MoviePoster = itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            String moviePosterUrl = moviesList.get(position).getFullImageUrl();
            Picasso.with(MoviePoster.getContext()).load(moviePosterUrl).into(MoviePoster);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.OnMovieClick(moviesList.get(getAdapterPosition()));
        }
    }
    public void updateMovies(List<DataEncap> movies) {
        moviesList.clear();
        moviesList.addAll(movies);
        notifyDataSetChanged();
    }

    }


