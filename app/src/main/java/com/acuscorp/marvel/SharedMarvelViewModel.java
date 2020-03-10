package com.acuscorp.marvel;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListAdapter;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acuscorp.marvel.Models.Data;
import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.Repository;

import java.util.List;



public class SharedMarvelViewModel extends ViewModel {
    private static final String TAG = "SharedMarvelViewModel";
    MutableLiveData<List<Result>> resultados;
    int size = 0;



    public SharedMarvelViewModel() {

        resultados = new MutableLiveData<>();


    }

    public LiveData<List<Result>> getResults(){
        return resultados;
    }
    public void setResults(int offset){
        Repository.getResults(offset);
        final Handler hdlk = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                size = Repository.getResults().size();
                long counter=0;

                while (Repository.getResults().size()<=size && counter<5){
                    try {
                        Thread.sleep(1000);
                        counter++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }




                hdlk.post(new Runnable() {
                    @Override
                    public void run() {
                        resultados.setValue(Repository.getResults());
                        Log.d(TAG, "run: There has no beeing any result");

                    }
                });


            }
        }).start();



    }

}
