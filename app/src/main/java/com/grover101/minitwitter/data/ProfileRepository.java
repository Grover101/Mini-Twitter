package com.grover101.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.grover101.minitwitter.common.Constantes;
import com.grover101.minitwitter.common.MyApp;
import com.grover101.minitwitter.common.SharedPreferencesManager;
import com.grover101.minitwitter.retrofit.AuthTwitterClient;
import com.grover101.minitwitter.retrofit.AuthTwitterService;
import com.grover101.minitwitter.retrofit.request.RequestCreateTweet;
import com.grover101.minitwitter.retrofit.request.RequestUserProfile;
import com.grover101.minitwitter.retrofit.response.Like;
import com.grover101.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.grover101.minitwitter.retrofit.response.ResponseUserProfile;
import com.grover101.minitwitter.retrofit.response.Tweet;
import com.grover101.minitwitter.retrofit.response.TweetDeleted;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<ResponseUserProfile> userProfile;
    MutableLiveData<String> photoProfile;

    ProfileRepository(){
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
        if (photoProfile == null)
            photoProfile = new MutableLiveData<>();
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {
        if (userProfile == null)
            userProfile = new MutableLiveData<>();

        Call<ResponseUserProfile> call = authTwitterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful())
                    userProfile.setValue(response.body());
                else
                    Toast.makeText(MyApp.getContext(), "Algo fue mal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion. Intentelo de nuevo", Toast.LENGTH_SHORT).show();

            }
        });
        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {
        Call<ResponseUserProfile> call = authTwitterService.updateProfile(requestUserProfile);

        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful())
                    userProfile.setValue(response.body());
                else
                    Toast.makeText(MyApp.getContext(), "Algo fue mal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion. Intentelo de nuevo", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void uploadPhoto(String photoPath) {
        File file = new File(photoPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = authTwitterService.uploadProfilePhoto(requestBody);

        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                }
                else
                    Toast.makeText(MyApp.getContext(), "Algo fue mal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion. Intentelo de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
