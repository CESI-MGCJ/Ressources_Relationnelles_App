package com.example.ressourcesrelationnelles.ui.ressources.View;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ressourcesrelationnelles.R;
import com.example.ressourcesrelationnelles.models.entities.Ressource;
import com.example.ressourcesrelationnelles.models.entities.User;
import com.example.ressourcesrelationnelles.utils.ImageUtils;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RessourceViewFragment extends Fragment {

    private Button btnReturn;
    private ImageView imgHeader;
    private TextView tvTitle;
    private TextView tvDescription;
    private LinearLayout layoutParagraphs;
    private ImageView imgContent;
    private TextView tvUser;
    private TextView tvDatePublished;
    private TextView tvDateUpdated;
    private Ressource ressource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ressource_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnReturn = view.findViewById(R.id.btnReturnToList);
        imgHeader = view.findViewById(R.id.imgHeader);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        layoutParagraphs = view.findViewById(R.id.layoutParagraphs);
        tvUser = view.findViewById(R.id.tvUser);
        tvDatePublished = view.findViewById(R.id.tvDatePublished);
        tvDateUpdated = view.findViewById(R.id.tvDateUpdated);

        btnReturn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            ressource = (Ressource) getArguments().getSerializable("ressource");
        }

        if (ressource != null) {
            bindData(ressource);
        }
    }

    private void bindData(Ressource res) {
        if (!TextUtils.isEmpty(res.headerImagePath)) {
            Glide.with(this).load(ImageUtils.getImageFullPath(res.headerImagePath)).into(imgHeader);
        }
        else{
            imgHeader.setBackgroundResource(R.drawable.no_img);
        }

        tvTitle.setText(!TextUtils.isEmpty(res.title) ? res.title : "Sans titre");
        tvDescription.setText(!TextUtils.isEmpty(res.description) ? res.description : "");

        layoutParagraphs.removeAllViews();
        if (!TextUtils.isEmpty(res.content)) {
            String[] sections = res.content.split("</section>");
            for (String sec : sections) {
                String paragraph = sec.replace("<section>", "").trim();
                if (!paragraph.isEmpty()) {
                    TextView tvPara = new TextView(requireContext());
                    tvPara.setText(paragraph);
                    tvPara.setTextSize(15f);
                    tvPara.setTextColor(Color.parseColor("#333333"));
                    tvPara.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    tvPara.setPadding(24, 16, 24, 16);
                    tvPara.setLineSpacing(6f, 1.0f);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 24);
                    tvPara.setLayoutParams(params);
                    layoutParagraphs.addView(tvPara);
                }
            }
        }

        if (!TextUtils.isEmpty(res.filePath)) {
            imgContent.setVisibility(View.VISIBLE);
            Glide.with(this).load(res.filePath).into(imgContent);
        }

        if(res.user != null)
            tvUser.setText("Rédigé par " + getUserName(res.user));

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        if (res.publicationDate != null) {
            tvDatePublished.setText("Publié le " + sdf.format(res.publicationDate));
        }

        if (res.updateDate != null) {
            tvDateUpdated.setText("Mis à jour le " + sdf.format(res.updateDate));
        }
    }

    private String getUserName(User u) {
        String first = (u.firstName != null) ? u.firstName : "";
        String last = (u.lastName != null) ? u.lastName : "";
        return (first + " " + last).trim();
    }
}