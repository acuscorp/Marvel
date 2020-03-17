package com.acuscorp.marvel.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.Repository;

import java.util.List;



public class SharedMarvelViewModel extends ViewModel {
    private static final String TAG = "SharedMarvelViewModel";
    LiveData<List<Result>> results;
    private Repository repository;

    public SharedMarvelViewModel(){
        repository = new Repository();
        results = repository.getAllResults();
    }
    public LiveData<List<Result>> getAllResults(){
        return results;
    }
    public void getMoreResults(int offset, int page_size) {
        repository.getMoreResults(offset, page_size);
    }
}
