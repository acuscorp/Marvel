package com.acuscorp.marvel;


import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.acuscorp.marvel.Models.Data;
import com.acuscorp.marvel.Models.Marvel;
import com.acuscorp.marvel.Models.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Repository {
    private static final String TAG = "Repository";
    private static List<Result> results = new ArrayList<>() ;
    private static MutableLiveData<List<Result>> resM ;
    public static String API_KEY = "d3b14f7f4734066b3d557fac768e496c";
    public static String HASH = "a64c0f47edffb941b376d27e6149d464";
    public static String TIMESTAMP = "9";
    private static  int PAGE_SIZE=10;
    private static int OFFSET;

    public static void setPageSize(int pageSize){
        PAGE_SIZE= pageSize;
    }


    public static ServiceGenerator serviceGenerator;

    public static void getResults(int offset ){
        OFFSET = offset;
        getImage();
        serviceGenerator.getInstance();
    }

    public static List<Result> getResults() {
        return results;
    }

    public static void getImage () {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("limit", "" + PAGE_SIZE);
        parameters.put("ts", TIMESTAMP);
        parameters.put("apikey", API_KEY);
        parameters.put("hash", HASH);
        parameters.put("offset","" +OFFSET);

        serviceGenerator.getApi().getHero(parameters).enqueue(new Callback<Marvel>() {
            @Override
            public void onResponse(Call<Marvel> call, retrofit2.Response<Marvel> response) {

                if (!response.isSuccessful()) {
                    int responseCode = response.code();
                    if(responseCode != 200) { // 504 Unsatisfiable Request (only-if-cached)


                    }
                    return;
                }
                Marvel getData = response.body();
                if(getData!=null){
                    Data data = getData.getData();


                    results.addAll(data.getResults());

                }
            }

            @Override
            public void onFailure(Call<Marvel> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });


    }





}
