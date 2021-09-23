package com.grover101.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grover101.minitwitter.R;
import com.grover101.minitwitter.common.Constantes;
import com.grover101.minitwitter.common.SharedPreferencesManager;
import com.grover101.minitwitter.data.ProfileViewModel;
import com.grover101.minitwitter.databinding.ActivityDashboardBinding;
import com.grover101.minitwitter.ui.profile.ProfileFragment;
import com.grover101.minitwitter.ui.tweets.NuevoTweetDialogFragment;
import com.grover101.minitwitter.ui.tweets.TweetListFragment;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private ActivityDashboardBinding binding;
    FloatingActionButton fab;
    ImageView ivAvatar;
    ProfileViewModel profileViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    f = TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL);
                    fab.show();
                    break;
                case R.id.navigation_tweets_like:
                    f = TweetListFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                    fab.hide();
                    break;
                case R.id.navigation_profile:
                    f = new ProfileFragment();
                    fab.hide();
                    break;
            }

            if(f != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, f)
                        .commit();
                return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        fab = findViewById(R.id.fab);
        ivAvatar = findViewById(R.id.imageViewToolbarPhoto);

        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_tweets_like, R.id.navigation_profile)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL))
                .commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevoTweetDialogFragment dialogFragment = new NuevoTweetDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "NuevoTweetDialogFragment");
            }
        });

        // Seteamos la imagen del usuario del perfil
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);
        if (!photoUrl.isEmpty())
            Glide.with(this)
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(ivAvatar);
        else
            Glide.with(this)
            .load(R.drawable.ic_baseline_account_circle_24)
            .into(ivAvatar);

        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String photo) {
                Glide.with(DashboardActivity.this)
                        .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(ivAvatar);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constantes.SELECT_PHOTO_GALLERY) {
                if (data != null) {
                    Uri imagenSeleccionada = data.getData(); // content://gallery/photos/..
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imagenSeleccionada, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        // "filename" = filePathColumn[0]
                        int imagenIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String fotoPath = cursor.getString(imagenIndex);
                        profileViewModel.uploadPhoto(fotoPath);
                        cursor.close();
                    }
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        // Invocamos la seleccion de foto de la galeria
        Intent seleccionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(seleccionarFoto, Constantes.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(this, "No se puede seleccionar la fotografia", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}