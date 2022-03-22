package com.example.eteslibauthproto.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.utils.ETESLibTextView;

import java.util.ArrayList;

public class SavedBooksGridListAdapter extends RecyclerView.Adapter<SavedBooksGridListAdapter.ViewHolder>{

    ArrayList<Book> savedBooks;
    Context context;
    LayoutInflater layoutInflater;

    public SavedBooksGridListAdapter(Context context, ArrayList<Book> books){
        this.context = context;
        this.savedBooks = books;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.saved_book_item, parent, false);
        return new SavedBooksGridListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.authorTv.setText(savedBooks.get(position).getAuthorName());
        holder.titleTv.setText(savedBooks.get(position).getTitle());

        Glide.with(context)
                .load(savedBooks.get(position).getCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.bookCoverIV);
    }

    @Override
    public int getItemCount() {
        return savedBooks.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private SavedBooksGridListAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(SavedBooksGridListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView bookCoverIV;
        ETESLibTextView titleTv, authorTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookCoverIV = itemView.findViewById(R.id.savedBookItemImageCoverTv);
            titleTv = itemView.findViewById(R.id.savedBookItemTitleTV);
            authorTv = itemView.findViewById(R.id.savedBookItemAuthorTV);

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
