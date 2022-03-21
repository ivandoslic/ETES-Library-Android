package com.example.eteslibauthproto.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.BookSearchItem;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibEditText;
import com.example.eteslibauthproto.utils.adapters.BookSearchVerticalListAdapter;
import com.example.eteslibauthproto.utils.adapters.UserSearchVerticalListAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Locale;

public class FullTextSearchActivity extends AppCompatActivity {

    ETESLibEditText searchInput;
    ChipGroup filterChips;
    RecyclerView searchRecyclerView;
    ArrayList<BookSearchItem> bookSearchItemArrayList;
    ArrayList<User> userSearchArrayList;
    BookSearchVerticalListAdapter bookSearchListAdapter;
    UserSearchVerticalListAdapter userSearchListAdapter;
    LinearLayoutManager bookSearchLayoutManager, userSearchLayoutManager;

    String genreFilter;
    boolean hasFilter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_text_search);

        Intent startingIntent = getIntent();
        if(startingIntent.hasExtra("genre_picked")) {
            genreFilter = startingIntent.getStringExtra("genre_picked");
            hasFilter = true;
        }

        bookSearchItemArrayList = FirestoreClass.getBooksSearchListInstance();

        searchInput = findViewById(R.id.fullTextSearchTextInputEditText);
        searchInput.requestFocus();

        searchRecyclerView = findViewById(R.id.fullTextSearchRecyclerView);
        setupBooksSearch();

        filterChips = findViewById(R.id.filterChipGroup);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        filterChips.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if(checkedId == R.id.booksChip) {
                    setBookSearchActive();
                } else if (checkedId == R.id.usersChip) {
                    setUsersSearchActive();
                }
            }
        });
    }

    private void setBookSearchActive() {
        searchRecyclerView.setLayoutManager(bookSearchLayoutManager);
        searchRecyclerView.setAdapter(bookSearchListAdapter);
    }

    private void setUsersSearchActive() {
        searchRecyclerView.setLayoutManager(userSearchLayoutManager);
        searchRecyclerView.setAdapter(userSearchListAdapter);
    }

    private void setupBooksSearch() {
        if(hasFilter) {
            ArrayList<BookSearchItem> allBooks = FirestoreClass.getBooksSearchListInstance();
            ArrayList<BookSearchItem> onlyBooksInGenre = new ArrayList<>();
            for(BookSearchItem bookSearchItem : allBooks) {
                if(bookSearchItem.getGenre().compareTo(genreFilter) == 0) {
                    onlyBooksInGenre.add(bookSearchItem);
                }
            }
            bookSearchListAdapter = new BookSearchVerticalListAdapter(this, onlyBooksInGenre);
            bookSearchItemArrayList = onlyBooksInGenre;
            Chip booksChip = findViewById(R.id.booksChip);
            booksChip.setText(genreFilter);
        } else {
            bookSearchListAdapter = new BookSearchVerticalListAdapter(this, FirestoreClass.getBooksSearchListInstance());
        }
        bookSearchLayoutManager = new LinearLayoutManager(this);

        bookSearchListAdapter.setOnItemClickListener(new BookSearchVerticalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                FirestoreClass.getSearchBookSelected(getThis(), bookSearchItemArrayList.get(position).getUid());
            }
        });

        FirestoreClass.getUserSearchList(this);
        userSearchLayoutManager = new LinearLayoutManager(this);

        searchRecyclerView.setLayoutManager(bookSearchLayoutManager);
        searchRecyclerView.setAdapter(bookSearchListAdapter);
    }

    private FullTextSearchActivity getThis() {
        return this;
    }

    private void filter(String input) {
        ArrayList<BookSearchItem> filteredBookList = new ArrayList<>();
        ArrayList<User> filteredUserList = new ArrayList<>();

        for(BookSearchItem searchItem : bookSearchItemArrayList) {
            if(searchItem.getTitle().toLowerCase().contains(input.toLowerCase())) {
                filteredBookList.add(searchItem);
            }
        }

        for(User userItem : userSearchArrayList) {
            if(userItem.getName().toLowerCase().contains(input.toLowerCase())) {
                filteredUserList.add(userItem);
            }
        }

        bookSearchListAdapter.filterList(filteredBookList);
        userSearchListAdapter.filterList(filteredUserList);

        bookSearchListAdapter.setOnItemClickListener(new BookSearchVerticalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                FirestoreClass.getSearchBookSelected(getThis(), filteredBookList.get(position).getUid());
            }
        });
    }

    public void gotUserListSuccessfully(ArrayList<User> usersList) {
        userSearchArrayList = usersList;
        userSearchListAdapter = new UserSearchVerticalListAdapter(this, usersList);

        userSearchListAdapter.setOnItemClickListener(new UserSearchVerticalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(getThis(), UserProfileActivity.class);
                i.putExtra(Constants.EXTRA_USER_DETAILS, userSearchArrayList.get(position));
                startActivity(i);
            }
        });
    }

    public void gotSelectedBookSuccessfully(Book searchedBook) {
        Intent i = new Intent(this, BookPreviewActivity.class);
        i.putExtra(Constants.BOOK_PREVIEW_INTENT_NAME, searchedBook);
        i.putExtra(Constants.EXTRA_USER_DETAILS, FirestoreClass.getCurrentUserInstance());
        startActivity(i);
    }
}