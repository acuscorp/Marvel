package com.acuscorp.marvel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acuscorp.marvel.Models.Result;

public class DetailsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<Result> result = new MutableLiveData<>();

    public void setResult(Result result){
        this.result.setValue(result);
    }

    public LiveData<Result> getResult
}
