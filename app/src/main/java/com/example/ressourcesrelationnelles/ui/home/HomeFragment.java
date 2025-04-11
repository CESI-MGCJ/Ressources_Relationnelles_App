package com.example.ressourcesrelationnelles.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ressourcesrelationnelles.R;
import com.example.ressourcesrelationnelles.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gonfle le fichier XML pour ce fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}