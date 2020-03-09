package com.acuscorp.marvel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.Models.Thumbnail;
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
    private LiveData<List<Result>> results;
    private int heroId ;
    private String heroUrl;


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

        onItemClickListener();
        currentPage=1;

        marvelViewModel.setResults(OFFSET*(currentPage-1));
    }

    private void onItemClickListener() {
        heroAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result result) {

                String heroUrl   =  result.getThumbnail().getPath()+"/portrait_xlarge."+result.getThumbnail().getExtension();

                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_HERO_ID, result.getId());
                intent.putExtra(DetailsActivity.EXTRA_HERO_DESCRIPTION,result.getDescription());
                intent.putExtra(DetailsActivity.EXTRA_HERO_URL,heroUrl);
                startActivity(intent);
            }
        });
    }

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
