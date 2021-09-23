package com.grover101.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.grover101.minitwitter.retrofit.request.RequestUserProfile;
import com.grover101.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileViewModel extends AndroidViewModel {
    public ProfileRepository profileRepository;
    public LiveData<ResponseUserProfile> userPerfil;
    public MutableLiveData<String> photoProfile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository();
        userPerfil = profileRepository.getProfile();
        photoProfile = profileRepository.getPhotoProfile();
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {
        profileRepository.updateProfile(requestUserProfile);
    }

    public void uploadPhoto(String photo) {
        profileRepository.uploadPhoto(photo);
    }
}