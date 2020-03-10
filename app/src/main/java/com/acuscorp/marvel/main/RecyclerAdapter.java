package com.acuscorp.marvel.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.acuscorp.marvel.Models.Result;
import com.acuscorp.marvel.Models.Thumbnail;
import com.acuscorp.marvel.R;
import com.bumptech.glide.RequestManager;

public class RecyclerAdapter extends ListAdapter<Result, com.acuscorp.marvel.main.RecyclerAdapter.MainHolder> {
    private RequestManager requestManager;
    private  OnItemClickListener listener;
    public RecyclerAdapter(RequestManager requestManager) {
        super(DIFF_CALLBACK);
        this.requestManager = requestManager;

    }
    private static final DiffUtil.ItemCallback<Result> DIFF_CALLBACK = new DiffUtil.ItemCallback<Result>() {
        @Override
        public boolean areItemsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem.getId().equals(newItem.getId()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getName().contains(newItem.getName()) ;
        }
    };



    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_hero_item,parent,false);

        return new MainHolder(itemView,requestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {

        holder.bind(getItem(position));


    }




    class MainHolder extends RecyclerView.ViewHolder{
        private RequestManager requestManager;
        private ImageView mHeroImage;
        private TextView mHeroName;
        public MainHolder(@NonNull View itemView,RequestManager requestManager) {
            super(itemView);
            mHeroImage = itemView.findViewById(R.id.iv_hero_image);
            mHeroName = itemView.findViewById(R.id.tv_hero_name);

            this.requestManager = requestManager;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position  = getAdapterPosition();
                    if(listener!=null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(getItem(position));
                    }
                }
            });


        }
        public void bind(Result result){

            Thumbnail url = result.getThumbnail();
            String heroName = result.getName();
            String heroUrl2   =  url.getPath()+"/portrait_xlarge."+url.getExtension();
            String heroUrl = "https://firebasestorage.googleapis.com/v0/b/fir-ecb2b.appspot.com/o/uploads%2F1583631375309.jpg?alt=media&token=05680ca0-1447-4751-8c27-15d5e0a162ef";
            requestManager
                    .load(heroUrl)
                    .fitCenter()
                    .into(mHeroImage);
            mHeroName.setText(heroName);
        }
    }

    public Result getResultAt(int position){
        return getItem(position);
    }
    public interface OnItemClickListener{
        void onItemClick(Result result);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}
