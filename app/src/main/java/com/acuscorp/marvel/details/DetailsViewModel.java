package com.acuscorp.marvel.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.acuscorp.marvel.Repository;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";
    private LiveData<List<String>> urlList;
    private Repository repository;
    public DetailsViewModel(){
        repository = new Repository();
        urlList = repository.getAllUrl();
    }

    public LiveData<List<String>> getAllUrl(){
        return urlList;
    }

    public void getMoreUrlImages(final String url) {
        repository.getMoreUrl(url);

    }



}
