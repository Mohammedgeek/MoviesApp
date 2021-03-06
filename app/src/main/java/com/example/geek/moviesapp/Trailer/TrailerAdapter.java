package com.example.geek.moviesapp.Trailer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.geek.moviesapp.R;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter {
    public TrailerAdapter(@NonNull Context context, List<Trailer> trailer) {
        super(context, 0, trailer);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);
        }
        Trailer currentTrailerObject = (Trailer) getItem(position);
        TextView mLabel = convertView.findViewById(R.id.trailer_label);
        mLabel.setText(currentTrailerObject.getLabel());

        return convertView;
    }
}
