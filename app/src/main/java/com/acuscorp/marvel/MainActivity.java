package com.acuscorp.marvel;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acuscorp.marvel.Models.Result;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MarvelViewModel marvelViewModel;
    private RecyclerView recyclerView;
    private RecyclerAdapter heroAdapter;
    private TextView textViewResult;
    private Api api;
    private ServiceGenerator serviceGenerator = ServiceGenerator.getInstance();
    private LinearLayoutManager layoutManager;
    LiveData<List<Result>> results;


    //---------------------
//    private String API_KEY = "d3b14f7f4734066b3d557fac768e496c";
//    private String HASH = "a64c0f47edffb941b376d27e6149d464";
//    private String TIMESTAMP = "9";
    private int PAGE_SIZE = 10;
    private int OFFSET =10;
    private boolean isLoading;
    private boolean isLastPage;
    private int currentPage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycle_view);


        marvelViewModel = new  ViewModelProvider(this).get(MarvelViewModel.class);
        marvelViewModel.getResults().observe(this, new Observer<List<Result>>() {
                    @Override
                    public void onChanged(List<Result> results) {
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        heroAdapter.submitList(results);
                        heroAdapter.notifyDataSetChanged();
                        isLoading=false;
                        isLastPage=false;

                    }
                });
        initRecyclerView();
//        getImage();


    }

    private void initRecyclerView() {



        progressBar = findViewById(R.id.fabProgress);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
        recyclerView.addItemDecoration(itemDecorator);
        heroAdapter = new RecyclerAdapter(initGlide());
        recyclerView.setAdapter(heroAdapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        heroAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result result) {
                Toast.makeText(MainActivity.this, "id: "+result.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        currentPage=1;

        marvelViewModel.setResults(OFFSET*(currentPage-1));
    }
//
//    public void getImage () {
//        isLoading = true;
//
//        currentPage += 1;
//
//
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("limit", "" + PAGE_SIZE*currentPage);
//        parameters.put("ts", TIMESTAMP);
//        parameters.put("apikey", API_KEY);
//        parameters.put("hash", HASH);
//        parameters.put("offset",OFFSET*(currentPage-1)+"");
//
//        serviceGenerator.getApi().getHero(parameters).enqueue(new Callback<Marvel>() {
//            @Override
//            public void onResponse(Call<Marvel> call, retrofit2.Response<Marvel> response) {
//                isLoading = false;
//                progressBar.setVisibility(ProgressBar.GONE);
//                if (!response.isSuccessful()) {
//                    int responseCode = response.code();
//                    if(responseCode != 200) { // 504 Unsatisfiable Request (only-if-cached)
//
//                        Toast.makeText(MainActivity.this, "There is no connection", Toast.LENGTH_SHORT).show();
//                    }
//                    return;
//                }
//                Marvel getData = response.body();
//                if(getData!=null){
//                    Data data = getData.getData();
//
//
//                    results.addAll(data.getResults().subList(PAGE_SIZE*(currentPage-1),data.getResults().size()));
//                    heroAdapter.submitList(results);
//                    heroAdapter.notifyDataSetChanged();
//                    layoutManager.scrollToPositionWithOffset(PAGE_SIZE*(currentPage-1), 20);
//                    if (results.size() < PAGE_SIZE) {
//                        isLastPage = true;
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Marvel> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });
//
//
//    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE){
                int position = newState;
                Log.d(TAG, "onScrollStateChanged: position "+ position);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);


            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (heroAdapter.getItemCount() < PAGE_SIZE) {
                isLastPage = true;

            }
            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE)
                {
                    isLoading=true;
                    currentPage++;

                    marvelViewModel.setResults(OFFSET*(currentPage-1));
                    progressBar.setVisibility(ProgressBar.VISIBLE);



                }
            }
        }
    };




    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

}
