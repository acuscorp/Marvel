package com.acuscorp.marvel;

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
import com.squareup.picasso.Picasso;

public class MainAdapter extends ListAdapter<Result, MainAdapter.MainHolder> {
    public MainAdapter() {
        super(DIFF_CALLBACK);

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
                    oldItem.getName() == newItem.getName();
        }
    };


    private  OnItemClickListener listener;
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item,parent,false);

        return new MainHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        Result currentHero = getItem(position);
        Thumbnail url = currentHero.getThumbnail();
//        String heroUrl = "http://x.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73/portrait_xlarge.jpg";
        String heroUrl = "https://firebasestorage.googleapis.com/v0/b/fir-ecb2b.appspot.com/o/uploads%2F1583631375309.jpg?alt=media&token=05680ca0-1447-4751-8c27-15d5e0a162ef";
//           String heroUrl   =  url.getPath()+"/portrait_xlarge."+url.getExtension();
        String heroName = currentHero.getName();
        holder.mHeroName.setText(heroName);

        Picasso.get().load(heroUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerInside()
                .into(holder.mHeroImage);
    }



    class MainHolder extends RecyclerView.ViewHolder{
        private ImageView mHeroImage;
        private TextView mHeroName;
        public MainHolder(@NonNull View itemView) {
            super(itemView);
            mHeroImage = itemView.findViewById(R.id.iv_hero_image);
            mHeroName = itemView.findViewById(R.id.tv_hero_name);

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
