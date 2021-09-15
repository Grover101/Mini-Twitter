package com.grover101.minitwitter.retrofit;

import com.grover101.minitwitter.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {
    private static AuthTwitterClient instance = null;
    private AuthTwitterService miniTwitterService;
    private Retrofit retrofit;

    public AuthTwitterClient() {

        // Incluir en la cabeza de la peticion el TOKEN que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)
                .build();
        miniTwitterService = retrofit.create(AuthTwitterService.class);
    }

    // Patron Singleton
    public static AuthTwitterClient getInstance() {
        //return instance == null ? instance = new MiniTwitterClient() : instance;
        if (instance == null) {
            instance = new AuthTwitterClient();
        }
        return instance;
    }

    public AuthTwitterService getAuthTwitterService() {
        return miniTwitterService;
    }
}
