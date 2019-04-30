package com.mdzyuba.popularmovies.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movie",
        foreignKeys = @ForeignKey(entity = Movie.class,
                                  parentColumns = "id",
                                  childColumns = "movie_id"),
        indices = {@Index("movie_id")})
public class FavoriteMovie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    public FavoriteMovie(int movieId) {
        this.movieId = movieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
