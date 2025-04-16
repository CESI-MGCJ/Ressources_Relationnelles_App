package com.example.ressourcesrelationnelles;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ressourcesrelationnelles.services.api.TokenManager;
import com.example.ressourcesrelationnelles.ui.admin.AdminFragment;
import com.example.ressourcesrelationnelles.ui.auth.LoginActivity;
import com.example.ressourcesrelationnelles.ui.home.HomeFragment;
import com.example.ressourcesrelationnelles.ui.profil.ProfilFragment;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ressourcesrelationnelles.ui.ressources.List.RessourceListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    private int currentItemId = R.id.nav_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);

        // Charger la page d'accueil par défaut
        loadFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if(currentItemId == item.getItemId())
                return false;

            currentItemId = item.getItemId();

            TokenManager tokenManager = new TokenManager();

            if (currentItemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (currentItemId == R.id.nav_ressources) {
                selectedFragment = new RessourceListFragment();
            } else if(tokenManager.getToken() == null || tokenManager.getUser() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else if (currentItemId == R.id.nav_profil) {
                selectedFragment = new ProfilFragment();
            } else if (currentItemId == R.id.nav_admin) {
                selectedFragment = new AdminFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}