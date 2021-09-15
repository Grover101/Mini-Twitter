package com.grover101.minitwitter.retrofit;

import com.grover101.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthTwitterService {
    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();
}
