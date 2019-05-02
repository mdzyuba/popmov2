package com.mdzyuba.popularmovies.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdzyuba.popularmovies.R;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieApiClient;
import com.squareup.picasso.Picasso;

import java.net.URL;

import androidx.annotation.NonNull;

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    public static void loadImage(Picasso picasso, Movie movie, ImageView imageView) {
        MovieApiClient movieApiClient = new MovieApiClient();
        String posterPath = movie.getPosterPath();
        if (posterPath == null) {
            Log.w(TAG, "The poster path is null");
            imageView.setImageResource(R.drawable.image_placeholder);
            return;
        }
        URL imageUrl = movieApiClient.getImageUri(posterPath);
        if (imageUrl != null) {
            String path = imageUrl.toString();
            picasso.load(path)
                   .placeholder(R.drawable.image_placeholder)
                   .into(imageView);
            imageView.setContentDescription(movie.getTitle());
        }
    }

    public static void updateMoviePosterPlaceholder(@NonNull TextView title, @NonNull Movie movie) {
        String movieTitle = movie.getTitle();
        if (movie.getPosterPath() == null && movieTitle != null) {
            title.setText(movieTitle.trim());
            title.setVisibility(View.VISIBLE);
        } else {
            title.setText("");
            title.setVisibility(View.GONE);
        }
    }
}
