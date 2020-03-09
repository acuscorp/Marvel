package com.acuscorp.marvel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.acuscorp.marvel.R;
import com.squareup.picasso.Picasso;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends FragmentActivity {

    private static final String TAG = "DetailsActivity";
    public static final String EXTRA_HERO_ID = "com.acuscorp.marvel.hero_id";
    public static final String EXTRA_HERO_URL = "com.acuscorp.marvel.hero_url";

    public static final String EXTRA_HERO_DESCRIPTION = "com.acuscorp.marvel.her_description";
    private int heroId=0;
    private String description="";
    private String heroUrl;
    private  List<String> imagesUrl;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        heroId = intent.getIntExtra(EXTRA_HERO_ID,-1);
        description =  intent.getStringExtra(EXTRA_HERO_DESCRIPTION);
        heroUrl = intent.getStringExtra(EXTRA_HERO_URL);
        Log.d(TAG, "onCreate: " +description);
        imageView = findViewById(R.id.image_view_hero);

        TextView textView = findViewById(R.id.text_view_description);
        if (!description.trim().isEmpty()){
            textView.setText(description);
        }
        imagesUrl =new ArrayList<>();





    }

    @Override
    protected void onStart() {
        super.onStart();
        Picasso.get()
                .load(heroUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit().centerCrop()
                .into(imageView);

    }
}
