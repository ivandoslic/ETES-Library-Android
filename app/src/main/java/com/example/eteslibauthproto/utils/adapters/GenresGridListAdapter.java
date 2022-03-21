package com.example.eteslibauthproto.utils.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.models.Genre;
import com.example.eteslibauthproto.utils.misc.RandomColors;

import java.util.List;
import java.util.Random;

public class GenresGridListAdapter extends RecyclerView.Adapter<GenresGridListAdapter.ViewHolder> {

    private List<Genre> genres;
    private Context context;
    private LayoutInflater layoutInflater;
    private RandomColors colorGenerator;

    public GenresGridListAdapter(Context context, List<Genre> genres) {
        this.genres = genres;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.colorGenerator = new RandomColors();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.search_genre_recycler_view_item, parent, false);
        return new GenresGridListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.genreTitleTv.setText(genres.get(position).getName());

        Glide.with(context)
                .load(genres.get(position).getBookSearchCovers().get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.sampleBookIv);

        holder.genreCardRootLayout.setBackgroundColor(colorGenerator.getColor());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private GenresGridListAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(GenresGridListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView genreTitleTv;
        ImageView sampleBookIv;
        ConstraintLayout genreCardRootLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genreTitleTv = itemView.findViewById(R.id.genreItemGenreTV);
            sampleBookIv = itemView.findViewById(R.id.genreItemBookCoverIV);
            genreCardRootLayout = itemView.findViewById(R.id.genreItemRootLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemClick(itemView, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
