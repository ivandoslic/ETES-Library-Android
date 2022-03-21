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
import com.example.eteslibauthproto.models.BookSearchItem;

import java.util.ArrayList;

public class BookSearchVerticalListAdapter extends RecyclerView.Adapter<BookSearchVerticalListAdapter.ViewHolder> {

    private ArrayList<BookSearchItem> bookSearchList;
    private Context context;
    private LayoutInflater layoutInflater;

    public BookSearchVerticalListAdapter(Context context, ArrayList<BookSearchItem> list) {
        this.context = context;
        this.bookSearchList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.book_search_item, parent, false);
        return new BookSearchVerticalListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = getBookTitle(bookSearchList.get(position).getTitle());
        String author = getBookAuthor(bookSearchList.get(position).getTitle());

        holder.bookTitleTextView.setText(title);
        holder.bookAuthorTextView.setText(author);

        Glide.with(context)
                .load(bookSearchList.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.bookCoverImageView);
    }

    private String getBookTitle(String title) {
        return title.split("\\|")[1];
    }

    private String getBookAuthor(String title) {
        return title.split("\\|")[0];
    }

    @Override
    public int getItemCount() {
        return bookSearchList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private BookSearchVerticalListAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(BookSearchVerticalListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView bookCoverImageView;
        TextView bookTitleTextView, bookAuthorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookAuthorTextView = itemView.findViewById(R.id.bookSearchItemAuthorTV);
            bookTitleTextView = itemView.findViewById(R.id.bookSearchItemTitleTV);
            bookCoverImageView = itemView.findViewById(R.id.bookSearchItemIV);

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
        public void onClick(View v) {

        }
    }

    public void filterList(ArrayList<BookSearchItem> filteredList) {
        bookSearchList = filteredList;
        notifyDataSetChanged();
    }
}
