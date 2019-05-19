package com.example.geek.moviesapp.DataProcess;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class DataEncap implements Parcelable {

    private String movieId;
    private String Title;
    private String Rate;
    private String Overview;
    private String posterUrl;
    private String releaseDate;
    private static final String imageBaseURL = "http://image.tmdb.org/t/p/";
    private static final String imageSize = "w185";

    public DataEncap(String movieId, String Title, String Rate, String Overview, String posterUrl, String releaseDate) {
        this.movieId = movieId;
        this.Title = Title;
        this.Rate = Rate;
        this.Overview = Overview;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    public static final Creator<DataEncap> CREATOR = new Creator<DataEncap>() {
        @Override
        public DataEncap createFromParcel(Parcel in) {
            return new DataEncap(in);
        }

        @Override
        public DataEncap[] newArray(int size) {
            return new DataEncap[size];
        }
    };

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return Title;
    }

    public String getRate() {
        return Rate;
    }

    public String getOverview() {
        return Overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getFullImageUrl() {
        return Uri.parse(imageBaseURL).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(this.getPosterUrl()).build().toString();
    }

    private DataEncap(Parcel in) {
        posterUrl = in.readString();
        Title = in.readString();
        Rate = in.readString();
        Overview = in.readString();
        releaseDate = in.readString();
        movieId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(Title);
        dest.writeString(Rate);
        dest.writeString(Overview);
        dest.writeString(releaseDate);
        dest.writeString(movieId);


    }


}

