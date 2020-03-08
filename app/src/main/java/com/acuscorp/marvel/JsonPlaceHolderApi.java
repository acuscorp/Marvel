package com.acuscorp.marvel;

import com.acuscorp.marvel.Models.Marvel;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


public interface JsonPlaceHolderApi {
    @GET("v1/public/characters")
    Call<Marvel> getHero(@QueryMap Map<String,String> parameters);
//    Call<List<JsonObject>> getHero(@QueryMap Map<String,String> parameters);
}
