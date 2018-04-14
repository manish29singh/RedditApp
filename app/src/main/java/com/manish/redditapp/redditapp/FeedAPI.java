package com.manish.redditapp.redditapp;

import com.manish.redditapp.redditapp.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by manish on 4/14/2018.
 */

public interface FeedAPI {
    String BASE_URL = "https://www.reddit.com/r/";

    @GET("EarthPorn/.rss")
    Call<Feed> getFeed();
}
