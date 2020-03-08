package com.acuscorp.marvel;

import androidx.lifecycle.LiveData;

import com.acuscorp.marvel.Models.Result;

import java.util.List;

public interface IRemoteDataAccess {
    LiveData<List<Result>> getAllHeroes();
}
