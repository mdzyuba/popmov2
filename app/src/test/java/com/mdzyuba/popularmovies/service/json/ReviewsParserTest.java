package com.mdzyuba.popularmovies.service.json;

import com.mdzyuba.popularmovies.model.Reviews;
import com.mdzyuba.popularmovies.service.TestDataUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ReviewsParserTest {
    private static final String REVIEWS = "reviews.json";

    @Test
    public void parseReviews() throws Exception {
        String json = TestDataUtils.getJsonString(REVIEWS);
        ReviewsParser parser = new ReviewsParser();
        Reviews reviews = parser.parseReviews(json);
        assertEquals(399579, reviews.id);
        assertEquals(1, reviews.page);
        List<Reviews.Review> results = reviews.results;
        assertEquals(8, results.size());
        assertEquals(8, reviews.totalResults);
        Reviews.Review review = results.get(0);
        assertEquals("leeboardman", review.author);
        assertTrue(review.content.startsWith("James Cameron"));
        assertEquals("5c5da23c0e0a2627a7671cb8", review.id);
        assertEquals("https://www.themoviedb.org/review/5c5da23c0e0a2627a7671cb8", review.url);
    }
}