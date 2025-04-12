package com.example.ressourcesrelationnelles.ui.profil;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ressourcesrelationnelles.MainActivity;
import com.example.ressourcesrelationnelles.R;
import com.example.ressourcesrelationnelles.services.api.TokenManager;
import com.example.ressourcesrelationnelles.ui.auth.SignupActivity;


public class ProfilFragment extends Fragment {

    Button btnDeconnexion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profilView = inflater.inflate(R.layout.fragment_profil, container, false);

        btnDeconnexion = profilView.findViewById(R.id.profil_deconnexion);
        btnDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TokenManager tokenManager = new TokenManager();
                tokenManager.clearToken();
                tokenManager.clearUser();

                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);

            }
        });
        return profilView;
    }
}