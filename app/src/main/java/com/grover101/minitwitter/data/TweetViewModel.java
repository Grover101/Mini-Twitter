package com.grover101.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.grover101.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets() { return tweets; }

    public void insertTweet(String mensaje) {
        tweetRepository.createTweet(mensaje);
    }

}
