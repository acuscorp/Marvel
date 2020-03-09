package com.acuscorp.marvel;

import android.os.Handler;
import android.os.Looper;
import android.widget.ListAdapter;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acuscorp.marvel.Models.Data;
import com.acuscorp.marvel.Models.Result;

import java.util.List;

public class MarvelViewModel extends ViewModel {

    MutableLiveData<List<Result>> resultados;
    int size = 0;



    public MarvelViewModel() {

        resultados = new MutableLiveData<>();


    }

    public LiveData<List<Result>> getResults(){
        return resultados;
    }
    public void setResultados(int offset, int limit, int currentPage){
        Repository.getResults(offset,limit, currentPage);
        final Handler hdlk = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                size = Repository.results.size();
                long counter=0;

                while (Repository.results.size()<=size && counter<30){
                    try {
                        Thread.sleep(100);
                        counter++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



                hdlk.post(new Runnable() {
                    @Override
                    public void run() {
                        resultados.setValue(Repository.results);

                    }
                });


            }
        }).start();



    }

}
