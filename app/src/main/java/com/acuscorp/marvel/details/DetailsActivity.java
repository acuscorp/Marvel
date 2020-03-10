package com.acuscorp.marvel.details;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.acuscorp.marvel.Models.Item;
import com.acuscorp.marvel.Models.Item_;
import com.acuscorp.marvel.Models.Item__;
import com.acuscorp.marvel.SharedMarvelViewModel;
import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends FragmentActivity {

    private static final String TAG = "DetailsActivity";
    public static final String EXTRA_HERO_RESULT = "com.acuscorp.marvel.result";

    private int heroId=0;
    private String description="";
    private String heroUrl;
    private SharedMarvelViewModel sharedMarvelViewModel;
    private ImageView imageView;
    private TextView textView;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private Result result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imageView = findViewById(R.id.image_view_hero);

        textView = findViewById(R.id.text_view_description);
        Intent intent = getIntent();
        result = (Result) intent.getExtras().get(EXTRA_HERO_RESULT);
        List<String> urls = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Item> items =   result.getComics().getItems();
        List<Item_> items_ = result.getSeries().getItems();
        List<Item__> items__ = result.getStories().getItems();
        for(Item item:items){
            urls.add(item.getResourceURI());
            names.add(item.getName());

        }
        for(Item_ item:items_){
            urls.add(item.getResourceURI());
            names.add(item.getName());

        }
        for(Item__ item:items__){
            urls.add(item.getResourceURI());
            names.add(item.getName());

        }







        description = result.getDescription();
        initRecycler();

    }
    private void getMoreImages(String url){

    }


    private void initRecycler(){
        recyclerView =findViewById(R.id.recycle_view_comics);


        if (!description.trim().isEmpty()){
            textView.setText(description);
        }
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        List<Item> comics = result.getComics().getItems();
        detailsAdapter = new DetailsAdapter(this,comics);

        recyclerView.setAdapter(detailsAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
