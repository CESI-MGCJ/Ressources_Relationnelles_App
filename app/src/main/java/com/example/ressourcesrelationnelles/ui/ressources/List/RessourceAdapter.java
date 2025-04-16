package com.example.ressourcesrelationnelles.ui.ressources.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ressourcesrelationnelles.R;
import com.example.ressourcesrelationnelles.models.entities.Ressource;
import com.example.ressourcesrelationnelles.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RessourceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    private final List<Ressource> ressources = new ArrayList<>();
    private boolean showLoader = false;

    private Consumer<Ressource> onClickRessource = null;

    public void setOnClickRessource(Consumer<Ressource> event){
        onClickRessource = event;
    }

    public void submitList(List<Ressource> newList) {
        ressources.clear();
        ressources.addAll(newList);
        notifyDataSetChanged();
    }

    public void showLoading() {
        if (!showLoader) {
            showLoader = true;
            notifyItemInserted(ressources.size());
        }
    }

    public void hideLoading() {
        if (showLoader) {
            showLoader = false;
            notifyItemRemoved(ressources.size());
        }
    }

    @Override
    public int getItemCount() {
        return ressources.size() + (showLoader ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return (position < ressources.size()) ? TYPE_ITEM : TYPE_LOADING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loader, parent, false);
            return new LoaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ressource, parent, false);
            return new RessourceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RessourceViewHolder) {
            Ressource res = ressources.get(position);
            RessourceViewHolder resHolder = (RessourceViewHolder) holder;

            resHolder.tvTitle.setText(res.title);
            resHolder.tvCategory.setText(res.category != null ? res.category.name : "Catégorie inconnue");

            if (res.headerImagePath != null && !res.headerImagePath.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(ImageUtils.getImageFullPath(res.headerImagePath))
                        .into(resHolder.imgHeader);
            } else {
                resHolder.imgHeader.setImageResource(R.drawable.no_img);
            }

            holder.itemView.setOnClickListener(v -> onClickRessource.accept(res));
        }
    }

    static class RessourceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory;
        ImageView imgHeader;

        public RessourceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            imgHeader = itemView.findViewById(R.id.imgHeader);
        }
    }

    static class LoaderViewHolder extends RecyclerView.ViewHolder {
        public LoaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}


