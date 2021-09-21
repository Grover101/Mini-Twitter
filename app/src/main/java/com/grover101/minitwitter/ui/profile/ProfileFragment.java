package com.grover101.minitwitter.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grover101.minitwitter.R;
import com.grover101.minitwitter.common.Constantes;
import com.grover101.minitwitter.data.ProfileViewModel;
import com.grover101.minitwitter.retrofit.request.RequestUserProfile;
import com.grover101.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    ImageView ivAvatar;
    EditText etUsername, etEmail, etPassword, etWebsite, etDescripcion;
    Button btnSave, btnChangePassword;
    boolean loadingData = true;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        ivAvatar = v.findViewById(R.id.imageViewAvatarProfile);
        etUsername = v.findViewById(R.id.editTextUsernameProfile);
        etEmail = v.findViewById(R.id.editTextEmailProfile);
        etPassword = v.findViewById(R.id.editTextCurrentPassword);
        etWebsite = v.findViewById(R.id.editTextWebsite);
        etDescripcion = v.findViewById(R.id.editTextDescripcion);
        btnSave = v.findViewById(R.id.buttonSave);
        btnChangePassword = v.findViewById(R.id.buttonChangePassword);

        // Eventos
        btnSave.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String website = etWebsite.getText().toString();
            String password = etPassword.getText().toString();

            if (username.isEmpty())
                etUsername.setError("El nombre de usuario es requrido");
            else if (email.isEmpty())
                etEmail.setError("El email es requerido");
            else if (password.isEmpty())
                etPassword.setError("La contraseña es requerida");
            else {
                RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
                profileViewModel.updateProfile(requestUserProfile);
                Toast.makeText(getActivity(), "Enviando informacion al servidor", Toast.LENGTH_SHORT).show();

                btnSave.setEnabled(false);
            }
        });

        btnChangePassword.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Click on Change Password", Toast.LENGTH_SHORT).show();
        });

        // ViewModel
        profileViewModel.userPerfil.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(ResponseUserProfile responseUserProfile) {
                loadingData = false;
                etUsername.setText(responseUserProfile.getUsername());
                etEmail.setText(responseUserProfile.getEmail());
                etWebsite.setText(responseUserProfile.getWebsite());
                etDescripcion.setText(responseUserProfile.getDescripcion());
                if (!loadingData) {
                    btnSave.setEnabled(true);
                    Toast.makeText(getActivity(), "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                }

                if (!responseUserProfile.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constantes.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .into(ivAvatar);
                }
                else {
                    Glide.with(getActivity())
                            .load(R.drawable.ic_baseline_account_circle_24)
                            .into(ivAvatar);
                }
            }
        });

        return v;
    }

}