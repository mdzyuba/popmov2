package com.mdzyuba.popularmovies.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdzyuba.popularmovies.R;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieApiClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URL;

import androidx.annotation.NonNull;


public class MoviePosterImageUtil {

    private static final String TAG = MoviePosterImageUtil.class.getSimpleName();

    public static void loadImage(@NonNull Picasso picasso,
                                 @NonNull Movie movie,
                                 @NonNull final ImageView imageView,
                                 @NonNull TextView title) {
        MovieApiClient movieApiClient = new MovieApiClient();
        String posterPath = movie.getPosterPath();
        if (posterPath == null) {
            Log.w(TAG, "The poster path is null");
            showPlaceholder(imageView, title, movie);
            return;
        }
        URL imageUrl = movieApiClient.getImageUri(posterPath);
        if (imageUrl == null) {
            showPlaceholder(imageView, title, movie);
            return;
        }
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, "Loading bitmap failed: " + e.getMessage(), e);
                showPlaceholder(imageView, title, movie);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        String path = imageUrl.toString();
        picasso.load(path)
               .placeholder(R.drawable.image_placeholder)
               .into(target);
        imageView.setContentDescription(movie.getTitle());
    }

    private static void showPlaceholder(@NonNull ImageView imageView,
                                        @NonNull TextView title,
                                        @NonNull Movie movie) {
        imageView.setImageResource(R.drawable.image_placeholder);
        updateMoviePosterPlaceholder(title, movie);
    }

    private static void updateMoviePosterPlaceholder(@NonNull TextView title, @NonNull Movie movie) {
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
