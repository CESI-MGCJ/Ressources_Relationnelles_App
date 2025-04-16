package com.example.ressourcesrelationnelles.services.api;

import com.example.ressourcesrelationnelles.models.ApiAuthResponse;
import com.example.ressourcesrelationnelles.models.PagedResponse;
import com.example.ressourcesrelationnelles.models.entities.Category;
import com.example.ressourcesrelationnelles.models.entities.Ressource;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IApiService {
    @GET
    Call<Object> get(@Url String url);

    @POST
    Call<Object> post(@Url String url, @Body Object body);

    @PUT
    Call<Object> put(@Url String url, @Body Object body);

    @DELETE
    Call<Object> delete(@Url String url);

    // AUTH ENDPOINTS
    @POST("auth/signup")
    Call<ApiAuthResponse> signup(@Body Map<String, String> body);

    @POST("auth/login")
    Call<ApiAuthResponse> login(@Body Map<String, String> body);

    @GET("category")
    Call<List<Category>> getCategories();

    @GET("ressource/search")
    Call<PagedResponse<Ressource>> searchRessources(
            @Query("search") String search,
            @Query("categoryIds") List<Long> categoryIds,
            @Query("ressourceTypeIds") List<Long> ressourceTypeIds,
            @Query("relationTypeIds") List<Long> relationTypeIds,
            @Query("status") String status,
            @Query("page") int page,
            @Query("size") int size
    );

}

