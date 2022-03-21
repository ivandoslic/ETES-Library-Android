package com.example.eteslibauthproto.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.BookSearchItem;
import com.example.eteslibauthproto.models.Genre;
import com.example.eteslibauthproto.utils.ETESLibEditText;
import com.example.eteslibauthproto.utils.adapters.GenresGridListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchRecyclerView;
    private ArrayList<Genre> currentGenresList;
    private ArrayList<BookSearchItem> currentSearchList;
    private ETESLibEditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        initGenresRecyclerView();

        searchInput = findViewById(R.id.searchTextInputEditText);
        searchInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startFullTextSearch();
                }
                return false;
            }
        });
    }

    private void initGenresRecyclerView() {
        FirestoreClass.getBooksSearchListActivitySearch(this);
    }

    public void gotSearchListSuccessfully(ArrayList<BookSearchItem> tempList) {
        currentSearchList = tempList;
        currentGenresList = new ArrayList<>();

        for (BookSearchItem item : tempList) {
            if(!isInTheGenresList(item.getGenre())) {
                Genre tempGenre = new Genre(item.getGenre(), item.getTitle(), item.getImage());
                currentGenresList.add(tempGenre);
            } else {
                addBookToGenre(item);
            }
        }

        setupGenresRecyclerView();
    }

    private void setupGenresRecyclerView() {
        GenresGridListAdapter genresGridListAdapter = new GenresGridListAdapter(this, currentGenresList);
        GridLayoutManager genresLayoutManager = new GridLayoutManager(this, 2);

        genresGridListAdapter.setOnItemClickListener(new GenresGridListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(SearchActivity.this, FullTextSearchActivity.class);
                i.putExtra("genre_picked", currentGenresList.get(position).getName());
                startActivity(i);
            }
        });

        searchRecyclerView.setLayoutManager(genresLayoutManager);
        searchRecyclerView.setAdapter(genresGridListAdapter);
    }

    private void addBookToGenre(BookSearchItem item) {
        for(Genre genreInList : currentGenresList) {
            if(genreInList.getName().compareTo(item.getGenre()) == 0) {
                genreInList.addBookAndCover(item.getTitle(), item.getImage());
            }
        }
    }

    private boolean isInTheGenresList(String genre) {
        if(currentGenresList.isEmpty())
            return false;

        for(Genre genreInList : currentGenresList) {
            if(genreInList.getName().compareTo(genre) == 0) {
                return true;
            }
        }
        return false;
    }

    private void startFullTextSearch() {
        Intent i = new Intent(this, FullTextSearchActivity.class);
        startActivity(i);
        finish();
    }
}