package com.mdzyuba.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class Reviews {

    private int id;
    private int page;
    private int totalPages;
    private int totalResults;
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

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void addReviews(Reviews reviews) {
        if (reviews.page <= page) {
            return;
        }
        id = reviews.id;
        page = reviews.page;
        results.addAll(reviews.results);
        totalResults = results.size();
    }

    public boolean canGetMorePages() {
        return page < totalPages;
    }
}
