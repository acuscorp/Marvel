

        package com.acuscorp.marvel.details;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.acuscorp.marvel.R;
        import com.bumptech.glide.RequestManager;
        import com.squareup.picasso.Picasso;

        import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsHolder> {

    private Context context;
    private List<String> urls;
    private List<String> names;
    private RequestManager requestManager;

    public DetailsAdapter(Context context, RequestManager requestManager,List<String> urls, List<String> names) {
        this.context = context;
        this.urls = urls;
        this.names = names;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_comic_item,parent,false);
        return new DetailsHolder(view);
    }

    @Override
    public void onBindViewHolder( DetailsHolder holder, int position) {
        String heroUrl = "https://firebasestorage.googleapis.com/v0/b/fir-ecb2b.appspot.com/o/uploads%2F1583631375309.jpg?alt=media&token=05680ca0-1447-4751-8c27-15d5e0a162ef";
//        Picasso.get()
//                .load(heroUrl)
//                .placeholder(R.mipmap.ic_launcher_round)
//                .fit()
//                .centerCrop()
//                .into(holder.mHeroComicImage);
        holder.mHeroName.setText(names.get(position));
        requestManager
                .load(urls.get(position))
                .fitCenter()
                .into(holder.mHeroComicImage);

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class DetailsHolder extends RecyclerView.ViewHolder{
        private RequestManager requestManager;
        private ImageView mHeroComicImage;
        private TextView mHeroName;
        public DetailsHolder(View itemView) {
            super(itemView);
            mHeroComicImage = itemView.findViewById(R.id.iv_comic_image);
            mHeroName = itemView.findViewById(R.id.tv_comic_name);

        }

    }




}