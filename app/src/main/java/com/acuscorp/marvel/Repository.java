package com.acuscorp.marvel;

import androidx.lifecycle.LiveData;

import com.acuscorp.marvel.Models.Result;

import java.util.List;

public class Repository {

    private  ResultsDao resultsDao;
    private LiveData<List<Result>> allResult;
    private Server server;

    public Repository() {
        resultsDao = new ResultsDao() {
            @Override
            public void getAllResult(LiveData<List<Result>> results) {
                allResult = results;
            }
        };

        server = new Server(resultsDao);
        allResult = server.getResM();
    }

    public LiveData<List<Result>> getAllResults() {
        return allResult;
    }


    public void getMoreResults(int offset, int page_size) {
        server.getImage(offset,page_size);
    }
}
