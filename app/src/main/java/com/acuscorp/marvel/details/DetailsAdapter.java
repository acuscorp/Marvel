package com.acuscorp.marvel.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.acuscorp.marvel.Models.NameUrls;
import com.acuscorp.marvel.R;
import com.bumptech.glide.RequestManager;

import java.util.List;

public class DetailsAdapter extends ListAdapter<String,DetailsAdapter.DetailsHolder> {

    private Context context;
    private List<String> urls;
    private List<String> names;
    private RequestManager requestManager;

    public DetailsAdapter(RequestManager requestManager) {
        super( DIFF_CALLBACK);
        this.requestManager = requestManager;
    }

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.matches(newItem)&&
                    oldItem.matches(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.matches(newItem)&&
                    oldItem.matches(newItem);
        }
    };

    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comic_item,parent,false);
        return new DetailsHolder(view,requestManager); 
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class DetailsHolder extends RecyclerView.ViewHolder {
        private final RequestManager requestManager;
        private ImageView mHeroComicImage;
        private TextView mHeroName;

        public DetailsHolder(View itemView,RequestManager requestManager) {
            super(itemView);
            mHeroComicImage = itemView.findViewById(R.id.iv_comic_image);
            mHeroName = itemView.findViewById(R.id.tv_comic_name);
            this.requestManager =requestManager;

        }


        public void bind(String item) {

            requestManager
                    .load(item)
                    .fitCenter()
                    .into(mHeroComicImage);
        }
    }


}