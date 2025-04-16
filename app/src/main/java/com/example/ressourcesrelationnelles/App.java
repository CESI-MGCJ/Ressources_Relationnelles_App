package com.example.ressourcesrelationnelles;

import android.app.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {
    public static final Map<Long, String> RELATION_TYPES = Map.of(
            1L, "Soi",
            2L, "Conjoints",
            3L, "Famille",
            4L, "Professionnelle",
            5L, "Amis et communautés",
            6L, "Inconnus"
    );

    public static final Map<Long, String> RESSOURCE_TYPES = Map.of(
            1L, "Activité / Jeu à réaliser",
            2L, "Article",
            3L, "Carte défi",
            4L, "Cours au format PDF",
            5L, "Exercice / Atelier",
            6L, "Fiche de lecture",
            7L, "Jeu en ligne",
            8L, "Vidéo"
    );


    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}

