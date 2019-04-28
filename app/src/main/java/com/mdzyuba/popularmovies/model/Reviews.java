package com.mdzyuba.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class Reviews {

    public final int id;
    public final int page;
    public final int totalPages;
    public final int totalResults;
    public final List<Review> results;

    public Reviews(int id, int page, int totalPages, int totalResults) {
        this.id = id;
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        results = new ArrayList<>();
    }

    public static class Review {
        public final String id;
        public final String author;
        public final String content;
        public final String url;

        public Review(String id, String author, String content, String url) {
            this.id = id;
            this.author = author;
            this.content = content;
            this.url = url;
        }
    }

    public void addReview(Review review) {
        results.add(review);
    }
}
