package com.mdzyuba.popularmovies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "top_movie",
        foreignKeys = @ForeignKey(entity = Movie.class,
                                  parentColumns = "id",
                                  childColumns = "movie_id"))
public class TopMovie {
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private final int movieId;

    public TopMovie(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }
}
