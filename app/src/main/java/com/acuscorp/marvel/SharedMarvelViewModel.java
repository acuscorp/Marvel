package com.acuscorp.marvel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.acuscorp.marvel.Models.Result;

import java.util.List;



public class SharedMarvelViewModel extends ViewModel {
    private static final String TAG = "SharedMarvelViewModel";
    LiveData<List<Result>> resultados;
    private Repository repository;
    int size = 0;



    public SharedMarvelViewModel(){
        repository = new Repository();
        resultados = repository.getAllResults();

    }

    public LiveData<List<Result>> getAllResults(){
        return resultados;
    }


    public void getMoreResults(int offset, int page_size) {
        repository.getMoreResults(offset, page_size);
    }
}
