package com.example.ressourcesrelationnelles.ui.ressources.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.ressourcesrelationnelles.models.entities.Ressource;

import java.util.Objects;

public class RessourceDiffCallback extends DiffUtil.ItemCallback<Ressource> {

    @Override
    public boolean areItemsTheSame(@NonNull Ressource oldItem, @NonNull Ressource newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Ressource oldItem, @NonNull Ressource newItem) {
        return oldItem.title.equals(newItem.title)
                && Objects.equals(oldItem.category, newItem.category)
                && Objects.equals(oldItem.headerImagePath, newItem.headerImagePath);
    }
}

