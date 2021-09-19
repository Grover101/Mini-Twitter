package com.grover101.minitwitter.data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.grover101.minitwitter.retrofit.response.Tweet;
import com.grover101.minitwitter.ui.BottomModalTweetFragment;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets() { return tweets; }

    public void openDialogTweetMenu(Context ctx, int idTweet) {
        BottomModalTweetFragment dialogTweet = BottomModalTweetFragment.newInstance(idTweet);
        dialogTweet.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "BottomModalFragment");
    }

    public LiveData<List<Tweet>> getFavTweets() { return tweetRepository.getFavTweets(); }

    public LiveData<List<Tweet>> getNewTweets() { return tweetRepository.getAllTweets(); }

    public LiveData<List<Tweet>> getNewFavTweets() {
        getNewTweets();
        return getFavTweets();
    }

    public void insertTweet(String mensaje) { tweetRepository.createTweet(mensaje); }

    public void deleteTweet(int idTweet) { tweetRepository.deleteTweet(idTweet); }

    public void likeTweet(int idTweet) { tweetRepository.LikeTweet(idTweet); }
}
