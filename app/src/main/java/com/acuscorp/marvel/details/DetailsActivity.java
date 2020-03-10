package com.acuscorp.marvel.details;

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
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acuscorp.marvel.Models.Data;
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
        String heroUrl = url.getPath() + "/portrait_xlarge." + url.getExtension();
        Picasso.get()
                .load(heroUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(imageView);

        urls = new ArrayList<>();

        names = new ArrayList<>();

        items = result.getComics().getItems();

        items_ = result.getSeries().getItems();

        items__ = result.getStories().getItems();
        int counter = 0;
        for (Item item : items) {
            urls.add(item.getResourceURI());
            names.add(item.getName());
            counter++;


        }
        for (Item_ item : items_) {
            urls.add(item.getResourceURI());
            names.add(item.getName());
            counter++;

        }
        for (Item__ item : items__) {
            urls.add(item.getResourceURI());
            names.add(item.getName());
            counter++;

        }
        try {
        if (counter >= 15) {
            for (int i = 0; i < 15; i++) {
                getMoreUrlImages(urls.get(i));

                    Thread.sleep(100);

            }
        } else {
            for (String urlcopy : urls) {
                getMoreUrlImages(urlcopy);
                Thread.sleep(100);
            }

        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        description = result.getDescription();
    }


    private void getMoreUrlImages(String url) {
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


                    results.addAll(data.getResults());

                }
            }

            @Override
            public void onFailure(Call<Marvel> call, Throwable t) {

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
        List<Item> comics = result.getComics().getItems();
        detailsAdapter = new DetailsAdapter(this, initGlide(), urls, names);

        recyclerView.setAdapter(detailsAdapter);

    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }


}
