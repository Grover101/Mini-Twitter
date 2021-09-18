package com.grover101.minitwitter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.grover101.minitwitter.R;
import com.grover101.minitwitter.common.Constantes;
import com.grover101.minitwitter.common.SharedPreferencesManager;
import com.grover101.minitwitter.data.TweetViewModel;

public class NuevoTweetDialogFragment extends DialogFragment implements View.OnClickListener {
    ImageView ivClose, ivAvatar;
    Button btnTwittear;
    EditText etMensaje;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.nuevo_tweet_full_dialog, container, false);

        ivClose = view.findViewById(R.id.imageViewClose);
        ivAvatar = view.findViewById(R.id.imageViewAvatarNewTweet);
        btnTwittear = view.findViewById(R.id.buttonTwittear);
        etMensaje = view.findViewById(R.id.editTextMensaje);

        // Eventos
        btnTwittear.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        // Seteamos la imagen del usuario del perfil
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);
        if (photoUrl.isEmpty())
            Glide.with(getActivity())
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoUrl)
                    .into(ivAvatar);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String mensaje = etMensaje.getText().toString();

        if (id == R.id.buttonTwittear) {
            if (mensaje.isEmpty())
                Toast.makeText(getActivity(), "Debe escribir un texto en el mensaje", Toast.LENGTH_SHORT).show();
            else {
                TweetViewModel tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
                tweetViewModel.insertTweet(mensaje);
                getDialog().dismiss();
            }
        }
        if (id == R.id.imageViewClose) {
            if (!mensaje.isEmpty())
                showDialogConfirm();
            else
                getDialog().dismiss();
        }
    }

    private void showDialogConfirm() {
        // Instancia un AlertDialog.Builder con este constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Metodos del dialog
        builder.setMessage("Desea realmente cancelar el Tweet? El mensaje se borrara")
                .setTitle("Cancelar Tweet");

        // Buttons
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Usuario clickea Ok button
                dialogInterface.dismiss();
                getDialog().dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Usuario cancela el dialogo
                dialogInterface.dismiss();
            }
        });

        // Obtener el AlertDialog de Create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
