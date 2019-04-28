package com.mdzyuba.popularmovies.service.json;

import com.mdzyuba.popularmovies.model.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReviewsParser {

    public static final String ID = "id";
    public static final String PAGE = "page";
    public static final String TOTAL_PAGES = "total_pages";
    public static final String TOTAL_RESULTS = "total_results";
    public static final String RESULTS = "results";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String URL = "url";

    public Reviews parseReviews(String json) throws JSONException {
        if (json == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(json);

        Integer id = jsonObject.optInt(ID, 0);
        Integer page = jsonObject.optInt(PAGE, 0);
        Integer totalPages = jsonObject.optInt(TOTAL_PAGES, 0);
        Integer totalResults = jsonObject.optInt(TOTAL_RESULTS, 0);

        Reviews reviews = new Reviews(id, page, totalPages, totalResults);

        JSONArray results = jsonObject.optJSONArray(RESULTS);
        for (int i = 0; i < results.length(); i++) {
            Reviews.Review review = parseReview(results.optJSONObject(i));
            reviews.addReview(review);
        }
        return reviews;
    }

    private Reviews.Review parseReview(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        String id = jsonObject.optString(ID);
        String author = jsonObject.optString(AUTHOR);
        String content = jsonObject.optString(CONTENT);
        String url = jsonObject.optString(URL);
        Reviews.Review review = new Reviews.Review(id, author, content, url);
        return review;
    }
}
