package com.mdzyuba.popularmovies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "popular_movie",
        foreignKeys = @ForeignKey(entity = Movie.class,
                                  parentColumns = "id",
                                  childColumns = "movie_id"))
public class PopularMovie {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private final int movieId;

    public PopularMovie(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }
}
