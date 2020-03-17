package com.acuscorp.marvel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acuscorp.marvel.Models.Data;
import com.acuscorp.marvel.Models.ImagesUrl;
import com.acuscorp.marvel.Models.Marvel;
import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.Models.Thumbnail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Server {
    private static final String TAG = "Server";
    private ResultsDao resultsDao;
    private List<Result> results = new ArrayList<>();
    private List<String> urlsImages = new ArrayList<>();
    private MutableLiveData<List<Result>> resM = new MutableLiveData<>();
    private MutableLiveData<List<String>> UrlM = new MutableLiveData<>();
    public String API_KEY = "d3b14f7f4734066b3d557fac768e496c";
    public String HASH = "a64c0f47edffb941b376d27e6149d464";
    public String TIMESTAMP = "9";

    public ServiceGenerator serviceGenerator;


    public Server(ResultsDao resultsDao) {
        this.resultsDao = resultsDao;
        serviceGenerator.getInstance();
    }

    public MutableLiveData<List<Result>> getResM() {
        return resM;
    }

    public void getImage(int OFFSET, int PAGE_SIZE) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("limit", "" + PAGE_SIZE);
        parameters.put("ts", TIMESTAMP);
        parameters.put("apikey", API_KEY);
        parameters.put("hash", HASH);
        parameters.put("offset", "" + OFFSET);

        serviceGenerator.getApi().getHero(parameters).enqueue(new Callback<Marvel>() {
            @Override
            public void onResponse(Call<Marvel> call, retrofit2.Response<Marvel> response) {

                if (!response.isSuccessful()) {
                    int responseCode = response.code();
                    if (responseCode != 200) { // 504 Unsatisfiable Request (only-if-cached)


                    }
                    return;
                }
                Marvel getData = response.body();
                if (getData != null) {
                    Data data = getData.getData();
                    results.addAll(data.getResults());
                    resM.setValue(results);
                    resultsDao.getAllResult(resM);
                }
            }

            @Override
            public void onFailure(Call<Marvel> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });


    }


    List<ImagesUrl> urlImages = new ArrayList<>();

    public void getMoreUrl(final String url) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("ts", TIMESTAMP);
        parameters.put("apikey", API_KEY);
        parameters.put("hash", HASH);

        serviceGenerator.getApi().getURLS(url, parameters).enqueue(new Callback<Marvel>() {
            @Override
            public void onResponse(Call<Marvel> call, Response<Marvel> response) {
                if (!response.isSuccessful()) {
                    int responseCode = response.code();
                    if (responseCode != 200) { // 504 Unsatisfiable Request (only-if-cached)
                        return;
                    }
                }
                Marvel getData = response.body();
                if (getData != null) {
                    Data data = getData.getData();
                    if (data.getResults() != null) {


                        List<Result> results2 = data.getResults();

                        for (Result result : results2) {
                            Thumbnail thumbnail = result.getThumbnail();
                            if (thumbnail != null) {
                                String linkImage = result.getThumbnail().getPath() + "/portrait_xlarge." + result.getThumbnail().getExtension();
                                urlsImages.add(linkImage);
                                UrlM.setValue(urlsImages);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Marvel> call, Throwable t) {
            }
        });
    }
    public LiveData<List<String>> getUrlM() {
        return UrlM;
    }
}
