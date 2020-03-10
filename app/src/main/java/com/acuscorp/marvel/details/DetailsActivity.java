package com.acuscorp.marvel.details;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acuscorp.marvel.Models.Data;
import com.acuscorp.marvel.Models.ImagesUrl;
import com.acuscorp.marvel.Models.Item;
import com.acuscorp.marvel.Models.Item_;
import com.acuscorp.marvel.Models.Item__;
import com.acuscorp.marvel.Models.Marvel;
import com.acuscorp.marvel.Models.Thumbnail;
import com.acuscorp.marvel.Repository;
import com.acuscorp.marvel.SharedMarvelViewModel;
import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.acuscorp.marvel.Repository.API_KEY;
import static com.acuscorp.marvel.Repository.HASH;
import static com.acuscorp.marvel.Repository.TIMESTAMP;
import static com.acuscorp.marvel.Repository.serviceGenerator;

public class DetailsActivity extends FragmentActivity {

    private static final String TAG = "DetailsActivity";
    public static final String EXTRA_HERO_RESULT = "com.acuscorp.marvel.result";
    //region Variables
    private String description = "";
    private SharedMarvelViewModel sharedMarvelViewModel;
    private ImageView imageView;
    private TextView textView;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private List<Result> results;
    private Result result;
    private List<Item> items;
    private List<Item_> items_;
    private List<Item__> items__;
    private List<String> urls;
    private List<String> names;
    private boolean isPhone;
    private int PAGE_SIZE_PHONE = 5;
    private int PAGE_SIZE_TABLET = 10;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int iterator = 0;
    private List<String> urlsImages = new ArrayList<>();

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setUI();
        Intent intent = getIntent();
        result = (Result) intent.getExtras().get(EXTRA_HERO_RESULT);
        getURLsFromResult();
        initRecycler();
    }

    private void setUI() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (width > 1080) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            layoutManager = new LinearLayoutManager(this);
            isPhone = true;
        }
        imageView = findViewById(R.id.image_view_hero);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height / 2);
        imageView.setLayoutParams(parms);
        textView = findViewById(R.id.text_view_description);

    }

    private void getURLsFromResult() {
        Thumbnail url = result.getThumbnail();
        String heroName = result.getName();
        String heroUrl = url.getPath() + "/standard_large." + url.getExtension();
        Picasso.with(imageView.getContext())
                .load(heroUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(imageView);
        description = result.getDescription();
        urls = new ArrayList<>();
        names = new ArrayList<>();
        items = result.getComics().getItems();
        items_ = result.getSeries().getItems();
        items__ = result.getStories().getItems();
        int counter = 0;
        for (Item item : items) {
            urls.add(item.getResourceURI());
            names.add(item.getName());
            getMoreUrlImages(item.getResourceURI());
        }
        for (Item_ item : items_) {
            urls.add(item.getResourceURI());
            names.add(item.getName());
            getMoreUrlImages(item.getResourceURI());
        }
//        for (Item__ item : items__) {
//            urls.add(item.getResourceURI());
//            names.add(item.getName());
//            getMoreUrlImages(item.getResourceURI());
//        }


    }

    List<ImagesUrl> urlImages = new ArrayList<>();

    private void getMoreUrlImages(final String url) {
        isLoading = true;
        Map<String, String> parameters = new HashMap<>();

        parameters.put("ts", TIMESTAMP);
        parameters.put("apikey", API_KEY);
        parameters.put("hash", HASH);

        serviceGenerator.getApi().getURLS(url, parameters).enqueue(new Callback<Marvel>() {
            @Override
            public void onResponse(Call<Marvel> call, Response<Marvel> response) {
                if (!response.isSuccessful()) {
                    int responseCode = response.code();
                    if (responseCode != 200) { // 504 Unsatisfiable Request (only-if-cached)

                        return;
                    }

                }
                Marvel getData = response.body();
                if (getData != null) {
                    Data data = getData.getData();

                    if (data.getResults() != null) {
                        isLoading = false;

//                        results.addAll(data.getResults());


                        {

                            List<Result> results2 = data.getResults();

                            for (Result result : results2) {
                                Thumbnail thumbnail = result.getThumbnail();
                                if (thumbnail != null) {
                                    String linkImage = result.getThumbnail().getPath() + "/portrait_xlarge." + result.getThumbnail().getExtension();

                                    urlsImages.add(linkImage);
                                    detailsAdapter.notifyDataSetChanged();


                                }


                            }


                        }
                    }}}

                    @Override
                    public void onFailure (Call < Marvel > call, Throwable t){
                        isLoading = false;
                    }
                });


            }


            private void initRecycler() {
                recyclerView = findViewById(R.id.recycle_view_comics);


                if (!description.trim().isEmpty()) {
                    textView.setText(description);
                }
                layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);

//                while (names.size()==urlsImages.size())

                detailsAdapter = new DetailsAdapter(this,initGlide(),urlsImages,names);
                recyclerView.setAdapter(detailsAdapter);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if (detailsAdapter.getItemCount() < PAGE_SIZE_PHONE && isPhone == true ||
                                detailsAdapter.getItemCount() < PAGE_SIZE_TABLET && isPhone == false) {
                            isLastPage = true;

                        }
                        if (!isLoading && !isLastPage) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                    && firstVisibleItemPosition >= 0
                                    && (totalItemCount >= PAGE_SIZE_PHONE && isPhone == true || totalItemCount >= PAGE_SIZE_TABLET && isPhone == false)) {


                            }
                        }
                    }
                });

            }

            private RequestManager initGlide() {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round);

                return Glide.with(this)
                        .setDefaultRequestOptions(options);
            }


        }
