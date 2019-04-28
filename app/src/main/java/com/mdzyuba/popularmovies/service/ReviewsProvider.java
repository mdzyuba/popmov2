package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Reviews;
import com.mdzyuba.popularmovies.service.json.ReviewsParser;

import java.net.URL;

public class ReviewsProvider {
    private final NetworkDataProvider networkDataProvider;

    public ReviewsProvider(NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
    }

    public Reviews getReviews(int movieId, int page) throws Exception {
        MovieApiClient client = new MovieApiClient();
        URL url = client.buildGetReviewsUrl(movieId, page);
        String json = networkDataProvider.getResponseFromHttpUrl(url);
        ReviewsParser parser = new ReviewsParser();
        Reviews reviews = parser.parseReviews(json);
        return reviews;
    }
}
