package com.acuscorp.marvel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.acuscorp.marvel.Models.Data;
import com.acuscorp.marvel.Models.Marvel;
import com.acuscorp.marvel.Models.Result;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private MainAdapter heroAdapter;
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    List<Result> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        heroAdapter = new MainAdapter();


        recyclerView.setAdapter(heroAdapter);

        heroAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result result) {
                Toast.makeText(MainActivity.this, "id: "+result.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newReques = originalRequest.newBuilder()
                                .header("Interceptor-Header","xyz")
                                .build();
                        return chain.proceed(newReques);
                    }
                })
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com/")       //! Do not forget to end with "/"
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getImage();






    }
    public void getImage() {

        String API_KEY = "d3b14f7f4734066b3d557fac768e496c";
        String HASH = "a64c0f47edffb941b376d27e6149d464";
        String TIMESTAMP = "9";
        int LIMIT = 10;
//        http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784/portrait_xlarge.jpg
        // http://gateway.marvel.com/v1/public/characters?limit=20&ts=1&apikey=caf5cfb7bb53560565010926559e2ce5&hash=cf169362def5c1474de3888976be220b

        Map<String,String> parameters =  new HashMap<>();
        parameters.put("limit",""+LIMIT);
        parameters.put("ts",TIMESTAMP);
        parameters.put("apikey",API_KEY);
        parameters.put("hash",HASH);

        Call<Marvel> data = jsonPlaceHolderApi.getHero(parameters);

        data.enqueue(new Callback<Marvel>() {
            @Override
            public void onResponse(Call<Marvel> call, retrofit2.Response<Marvel> response) {
                Log.d(TAG, "onResponse: " +response.code() +
                        "\t" + response.body());


                Marvel getData= response.body();
                Log.d(TAG, "onResponse: message" +response.message());
                Data data = getData.getData();
                results=null;
                results = new ArrayList<>();
                results.addAll(data.getResults());
                heroAdapter.submitList(results);
                heroAdapter.notifyDataSetChanged();

                Log.d(TAG, "onResponse: "+ response.message());


            }

            @Override
            public void onFailure(Call<Marvel> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }



}
