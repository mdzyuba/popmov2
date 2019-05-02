package com.mdzyuba.popularmovies.service.json;

import android.text.TextUtils;
import android.util.Log;

import com.mdzyuba.popularmovies.BuildConfig;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.MovieCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieParser {

    private static final String TAG = MovieParser.class.getSimpleName();
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String PAGE = "page";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String NULL_STRING = "null";

    @NonNull
    public MovieCollection parseMovieCollection(@Nullable String json) {
        MovieCollection movieCollection = new MovieCollection();
        if (json == null) {
            return movieCollection;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            int page = jsonObject.optInt(PAGE);
            int totalPages = jsonObject.optInt(TOTAL_PAGES);
            JSONArray results = jsonObject.optJSONArray(RESULTS);
            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                Movie movie = parseMovie(results.optJSONObject(i));
                if (movie != null) {
                    movies.add(movie);
                }
            }
            movieCollection.setFurthestPage(page);
            movieCollection.setTotalPages(totalPages);
            movieCollection.setMovieList(movies);
        } catch (JSONException e) {
            Log.d(TAG, "JSON parsing error: " + e.getMessage(), e);
        }
        return movieCollection;
    }

    @Nullable
    private Movie parseMovie(@Nullable JSONObject jsonMovie) {
        if (jsonMovie == null) {
            return null;
        }

        Movie.Builder movieBuilder = new Movie.Builder();
        Integer id = jsonMovie.optInt(ID);
        String title = jsonMovie.optString(TITLE);
        String posterPath = getString(jsonMovie, POSTER_PATH);
        String overview = jsonMovie.optString(OVERVIEW);
        String releaseDate = jsonMovie.optString(RELEASE_DATE);
        String voteAverage = jsonMovie.optString(VOTE_AVERAGE);

        movieBuilder
                .withId(id)
                .withTitle(title)
                .withPosterPath(posterPath)
                .withOverview(overview);

        if (releaseDate != null) {
            movieBuilder.withReleaseDate(toDate(releaseDate));
        }

        if (voteAverage != null) {
            movieBuilder.withVoteAverage(Float.parseFloat(voteAverage));
        }

        return movieBuilder.build();
    }

    /**
     * Retrieves a json object property value and converts "null" to null.
     *
     * In some cases, IMDB returns "null" for a property. This method will return null in this case.
     *
     * @param jsonObject a jsonObject to parse.
     * @param tag a tag to retrieve.
     * @return a value of a tag
     */
    private String getString(@NonNull JSONObject jsonObject, @NonNull String tag) {
        String value = jsonObject.optString(tag);
        if (NULL_STRING.equals(value) || TextUtils.isEmpty(value)) {
            if (BuildConfig.DEBUG && POSTER_PATH.equals(tag)) {
                Log.d(TAG, "The jsonObject has a null poster: " + jsonObject.toString());
            }
            return null;
        }
        return value;
    }

    Date toDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse thee release date: " + date);
        }
        return null;
    }
}
