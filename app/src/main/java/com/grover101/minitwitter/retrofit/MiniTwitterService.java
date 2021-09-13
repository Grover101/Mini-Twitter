package com.grover101.minitwitter.retrofit;

import com.grover101.minitwitter.RequestLogin;
import com.grover101.minitwitter.RequestSignup;
import com.grover101.minitwitter.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {
    @POST("/auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("/auth/signup")
    Call<ResponseAuth> doSignUp(@Body RequestSignup requestSignup);
}
