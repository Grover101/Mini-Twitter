package com.grover101.minitwitter.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.grover101.minitwitter.R;
import com.grover101.minitwitter.common.Constantes;
import com.grover101.minitwitter.common.SharedPreferencesManager;
import com.grover101.minitwitter.retrofit.MiniTwitterClient;
import com.grover101.minitwitter.retrofit.MiniTwitterService;
import com.grover101.minitwitter.retrofit.request.RequestLogin;
import com.grover101.minitwitter.retrofit.response.ResponseAuth;
import com.grover101.minitwitter.ui.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvGoSignUp;
    EditText etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    private AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        banner = findViewById(R.id.adView);
        // Solicitud de anuncio para mostrar en el banner
        AdRequest adRequest = new AdRequest.Builder().build();
        // Asociar la solicitud al banner
        banner.loadAd(adRequest);
        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        
        retrofitInit();
        findViews();
        events();
        
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnLogin = findViewById(R.id.buttonLogin);
        tvGoSignUp = findViewById(R.id.textViewGoSignUp);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }
    
    private void events() {
        btnLogin.setOnClickListener(this);
        tvGoSignUp.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewGoSignUp:
                goToSignUp();
        }
    }

    private void goToLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        
        if (email.isEmpty()) etEmail.setError("El email es requerido");
        else if (password.isEmpty()) etPassword.setError("La contraseña es requerida");
        else {
            RequestLogin requestLogin = new RequestLogin(email, password);

            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);
            
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sesion iniciada correctamente", Toast.LENGTH_SHORT).show();

                        // Invocar al metodo SharedPreferencesManger
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USER, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);
                        // Destruimos este Activity para que no se pueda volver.
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Algo fue mal, revise sus datos de acceso", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Problemas de conexion. Intentelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        }
        
    }

    private void goToSignUp() {
        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(i);
        finish();
    }
}