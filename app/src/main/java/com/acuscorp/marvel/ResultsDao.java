package com.acuscorp.marvel;

import androidx.lifecycle.LiveData;

import com.acuscorp.marvel.Models.Result;

import java.util.List;

public interface ResultsDao {

    void getAllResult(LiveData<List<Result>>  results);
    void getAllUrl(LiveData<List<String>> urlList);
}
