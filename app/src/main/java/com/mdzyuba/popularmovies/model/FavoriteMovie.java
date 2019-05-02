package com.mdzyuba.popularmovies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movie",
        foreignKeys = @ForeignKey(entity = Movie.class,
                                  parentColumns = "id",
                                  childColumns = "movie_id"))
public class FavoriteMovie {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private int movieId;

    @ColumnInfo(name = "time_ms")
    private long insertTimeMs;

    public FavoriteMovie(int movieId) {
        this.movieId = movieId;
        insertTimeMs = System.currentTimeMillis();
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public long getInsertTimeMs() {
        return insertTimeMs;
    }

    public void setInsertTimeMs(long insertTimeMs) {
        this.insertTimeMs = insertTimeMs;
    }
}
