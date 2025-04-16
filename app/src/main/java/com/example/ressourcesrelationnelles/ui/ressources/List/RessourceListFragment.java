package com.example.ressourcesrelationnelles.ui.ressources.List;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ressourcesrelationnelles.App;
import com.example.ressourcesrelationnelles.R;
import com.example.ressourcesrelationnelles.models.PagedResponse;
import com.example.ressourcesrelationnelles.models.entities.Category;
import com.example.ressourcesrelationnelles.models.entities.Ressource;
import com.example.ressourcesrelationnelles.services.api.ApiServiceBuilder;
import com.example.ressourcesrelationnelles.services.api.IApiService;
import com.example.ressourcesrelationnelles.ui.ressources.View.RessourceViewFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RessourceListFragment extends Fragment {

    ChipGroup cHCategory;
    ImageButton btnFilter;
    EditText etSearch;
    RecyclerView rvRessource;
    TextView tvEmptyState;
    View popupView;


    private List<Category> cachedCategories;

    private List<Long> categoryIds = new ArrayList<>();
    private List<Long> ressourceTypeIds = new ArrayList<>();
    private List<Long> relationTypeIds = new ArrayList<>();
    private String searchValue = null;

    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private final int PAGE_SIZE = 10;
    private RessourceAdapter adapter;
    private List<Ressource> ressourceList = new ArrayList<>();

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY = 500; // en ms

    private Call<PagedResponse<Ressource>> currentCall = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ressource_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFilter = view.findViewById(R.id.btnFilter);
        etSearch = view.findViewById(R.id.etSearch);
        rvRessource = view.findViewById(R.id.recyclerViewRessources);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    searchValue = s.toString().trim();
                    resetAndReload();
                };

                searchHandler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
            }
        });

        // ✅ Déclenche la recherche si l’utilisateur appuie sur Entrée
        etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchValue = etSearch.getText().toString().trim();
                resetAndReload();

                return true;
            }
            return false;
        });

        rvRessource.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RessourceAdapter();
        adapter.setOnClickRessource(res -> {
                Bundle bundle = new Bundle();
        bundle.putSerializable("ressource", res);

        RessourceViewFragment fragment = new RessourceViewFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        });
        rvRessource.setAdapter(adapter);

        loadRessourcesPage();
        fetchCategoriesIfNeeded();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(cachedCategories != null)
            btnFilter.setVisibility(View.VISIBLE);
    }

    private void fetchCategoriesIfNeeded() {
        if (cachedCategories != null) return;

        IApiService api = ApiServiceBuilder.getApiService();

        api.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cachedCategories = response.body();
                    prepareFilterPopup();
                    btnFilter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(requireContext(), "Erreur chargement catégories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareFilterPopup(){
        popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_filter_ressources_list, null);

        ChipGroup chipGroupRelation = popupView.findViewById(R.id.chipGroupRelation);
        ChipGroup chipGroupCategorie = popupView.findViewById(R.id.chipGroupCategorie);
        ChipGroup chipGroupRessource = popupView.findViewById(R.id.chipGroupRessource);

        App.RELATION_TYPES.forEach((id, name) -> {
            Chip chip = new Chip(requireContext());
            chip.setText(name);
            chip.setCheckable(true);
            chip.setChecked(relationTypeIds.contains(id.intValue()));
            chip.setId(id.intValue());
            chipGroupRelation.addView(chip);
        });

        App.RESSOURCE_TYPES.forEach((id, name) -> {
            Chip chip = new Chip(requireContext());
            chip.setText(name);
            chip.setCheckable(true);
            chip.setChecked(ressourceTypeIds.contains(id.intValue()));
            chip.setId(id.intValue());
            chipGroupRessource.addView(chip);
        });

        cachedCategories.forEach((cat) -> {
            Chip chip = new Chip(requireContext());
            chip.setText(cat.name);
            chip.setCheckable(true);
            chip.setChecked(categoryIds.contains(cat.id.intValue()));
            chip.setId(cat.id.intValue());
            chipGroupCategorie.addView(chip);
        });
    }

    private void showFilterPopup() {
        if (popupView.getParent() != null) {
            ((ViewGroup) popupView.getParent()).removeView(popupView);
        }

        ChipGroup chipGroupRelation = popupView.findViewById(R.id.chipGroupRelation);
        ChipGroup chipGroupCategorie = popupView.findViewById(R.id.chipGroupCategorie);
        ChipGroup chipGroupRessource = popupView.findViewById(R.id.chipGroupRessource);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Filtres")
                .setView(popupView)
                .setPositiveButton("Appliquer", (dialog, which) -> {

                    relationTypeIds = chipGroupRelation.getCheckedChipIds().stream()
                            .map(Integer::longValue)
                            .collect(Collectors.toList());

                    categoryIds = chipGroupCategorie.getCheckedChipIds().stream()
                            .map(Integer::longValue)
                            .collect(Collectors.toList());

                    ressourceTypeIds = chipGroupRessource.getCheckedChipIds().stream()
                            .map(Integer::longValue)
                            .collect(Collectors.toList());

                    resetAndReload();

                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void loadRessourcesPage() {
        if(isLastPage) return;

        isLoading = true;
        adapter.showLoading();

        IApiService api = ApiServiceBuilder.getApiService();

        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }

        currentCall = api.searchRessources(
                searchValue,
                categoryIds.isEmpty() ? null : categoryIds,
                ressourceTypeIds.isEmpty() ? null : ressourceTypeIds,
                relationTypeIds.isEmpty() ? null : relationTypeIds,
                null, // status
                currentPage,
                PAGE_SIZE
        );

        currentCall.enqueue(new Callback<PagedResponse<Ressource>>() {
            @Override
            public void onResponse(Call<PagedResponse<Ressource>> call, Response<PagedResponse<Ressource>> response) {
                isLoading = false;
                adapter.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    PagedResponse<Ressource> result = response.body();
                    List<Ressource> newRessources = result.content;

                    ressourceList.addAll(newRessources);

                    adapter.submitList(new ArrayList<>(ressourceList));

                    isLastPage = (currentPage + 1) >= result.totalPages;
                    currentPage++;
                }
                else{
                    Toast.makeText(requireContext(), "Erreur " + response.code() + " : Impossible de récupérer les ressources", Toast.LENGTH_SHORT).show();
                }

                if (ressourceList.isEmpty() && !isLoading) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<Ressource>> call, Throwable t) {
                isLoading = false;
                adapter.hideLoading();
                if (ressourceList.isEmpty() && !isLoading) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                }

                Toast.makeText(requireContext(), "Impossible de se connecter", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetAndReload() {
        currentPage = 0;
        isLastPage = false;
        ressourceList.clear();
        adapter.submitList(new ArrayList<>());
        loadRessourcesPage(); // 🔄 restart
    }




}