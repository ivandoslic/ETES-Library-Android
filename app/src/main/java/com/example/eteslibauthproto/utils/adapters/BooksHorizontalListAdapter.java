package com.example.eteslibauthproto.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.utils.GlideLoader;

import java.util.ArrayList;

public class BooksHorizontalListAdapter extends RecyclerView.Adapter<BooksHorizontalListAdapter.ViewHolder>{

    private ArrayList<Book> books;
    private LayoutInflater layoutInflater;
    private Context context;

    public BooksHorizontalListAdapter(Context context, ArrayList<Book> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.books = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.book_recycler_item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book currentBook = books.get(position);

        Glide.with(context)
                .load(currentBook.getCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.coverImageView);

        holder.titleTextView.setText(currentBook.getTitle());
        holder.authorTextView.setText(currentBook.getAuthorName());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImageView;
        TextView titleTextView, authorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.booksRecyclerViewImage);
            titleTextView = itemView.findViewById(R.id.booksRecyclerViewTitleTV);
            authorTextView = itemView.findViewById(R.id.booksRecyclerViewAuthorTV);
        }
    }
}
