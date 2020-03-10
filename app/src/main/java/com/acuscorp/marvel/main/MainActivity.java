package com.acuscorp.marvel.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acuscorp.marvel.Repository;
import com.acuscorp.marvel.SharedMarvelViewModel;
import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.R;
import com.acuscorp.marvel.details.DetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //region Variables
    private static final String TAG = "MainActivity";
    private static final int PAGE_SIZE_TABLET = 20;
    private static final int PAGE_SIZE_PHONE = 10;
    private SharedMarvelViewModel sharedMarvelViewModel;
    private RecyclerView recyclerView;
    private RecyclerAdapter heroAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isPhone = false;
    private int OFFSET = 10;
    private boolean isLoading;
    private boolean isLastPage;
    private int currentPage;
    private ProgressBar progressBar;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            Log.d(TAG, "Your current IP address : " + ip);
            Log.d(TAG,  "Your current Hostname : " + hostname);

        } catch (Exception e) {

            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recycle_view);
        progressBar = findViewById(R.id.fabProgress);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        setLayoutManaye();
        initViewModel();
        initRecyclerView();
    }

    //region initFunctions
    private void setLayoutManaye() {
        Display display = getWindowManager().getDefaultDisplay();
        int spanCount = 3;
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if (width > 1080) {
            Repository.setPageSize(PAGE_SIZE_TABLET);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = new GridLayoutManager(this, spanCount + 2);
            } else {
                layoutManager = new GridLayoutManager(this, spanCount);

            }

        } else {
            Repository.setPageSize(PAGE_SIZE_PHONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            layoutManager = new LinearLayoutManager(this);
            isPhone = true;

        }
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {

        sharedMarvelViewModel = new ViewModelProvider(this).get(SharedMarvelViewModel.class);
        sharedMarvelViewModel.getResults().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                heroAdapter.submitList(results);
                heroAdapter.notifyDataSetChanged();
                isLoading = false;
                isLastPage = false;

            }
        });
    }

    private void initRecyclerView() {
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
        recyclerView.addItemDecoration(itemDecorator);
        heroAdapter = new RecyclerAdapter(initGlide());
        recyclerView.setAdapter(heroAdapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        onItemClickListener();
        currentPage = 1;
        sharedMarvelViewModel.setResults(OFFSET * (currentPage - 1));
    }
    //endregion

    private void onItemClickListener() {
        heroAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result result) {

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_HERO_RESULT, result);
                startActivity(intent);
            }
        });
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int position = newState;
                Log.d(TAG, "onScrollStateChanged: position " + position);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);


            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (heroAdapter.getItemCount() < PAGE_SIZE_PHONE && isPhone == true ||
                    heroAdapter.getItemCount() < PAGE_SIZE_TABLET && isPhone == false) {
                isLastPage = true;

            }
            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && (totalItemCount >= PAGE_SIZE_PHONE && isPhone == true || totalItemCount >= PAGE_SIZE_TABLET && isPhone == false)) {
                    isLoading = true;
                    currentPage++;

                    sharedMarvelViewModel.setResults(OFFSET * (currentPage - 1));
                    progressBar.setVisibility(ProgressBar.VISIBLE);


                }
            }
        }
    };

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

}
