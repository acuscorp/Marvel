package com.acuscorp.marvel;

import android.net.Uri;

import com.acuscorp.marvel.Models.Marvel;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface Api {
    @GET("v1/public/characters")
    Call<Marvel> getHero(@QueryMap Map<String,String> parameters);
    @GET
    Call<Marvel> getURLS(@Url String url, @QueryMap Map<String,String> parameters);
}
