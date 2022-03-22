package com.example.eteslibauthproto.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.databinding.FragmentSavedBooksBinding;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.ui.activities.BookPreviewActivity;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.adapters.BooksHorizontalListAdapter;
import com.example.eteslibauthproto.utils.adapters.SavedBooksGridListAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SavedBooksFragment extends Fragment {

    private FragmentSavedBooksBinding binding;
    private ConstraintLayout noSavedBooksMessage;
    private RecyclerView savedBooksRecyclerView;
    private GridLayoutManager layoutManager;
    private SavedBooksGridListAdapter listAdapter;
    private ArrayList<Book> savedBooks;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSavedBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        noSavedBooksMessage = binding.noSavedBooksInSavedBooks;
        savedBooksRecyclerView = binding.savedBooksRecyclerView;

        initSavedBooks();

        return root;
    }

    private void initSavedBooks() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SAVED_BOOKS, Context.MODE_PRIVATE);
        if(sharedPreferences.getString("saved", null) != null && sharedPreferences.getString("saved", null).compareTo("") != 0) {
            noSavedBooksMessage.setVisibility(View.GONE);
            savedBooksRecyclerView.setVisibility(View.VISIBLE);
            String[] saveIds = sharedPreferences.getString("saved", null).split("\\|");
            for (int i = 1; i < saveIds.length; i++) {
                FirestoreClass.getSavedBook(this, saveIds[i]);
            }
        } else {
            noSavedBooksMessage.setVisibility(View.VISIBLE);
            savedBooksRecyclerView.setVisibility(View.GONE);
        }
    }

    private void handleRecycler() {
        if(layoutManager == null || listAdapter == null) {
            layoutManager = new GridLayoutManager(getContext(), 2);
            listAdapter = new SavedBooksGridListAdapter(getContext(), savedBooks);
        } else {
            listAdapter.notifyDataSetChanged();
        }

        listAdapter.setOnItemClickListener(new SavedBooksGridListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(getActivity(), BookPreviewActivity.class);
                i.putExtra(Constants.BOOK_PREVIEW_INTENT_NAME, savedBooks.get(position));
                i.putExtra(Constants.EXTRA_USER_DETAILS, FirestoreClass.getCurrentUserInstance());
                startActivity(i);
            }
        });

        savedBooksRecyclerView.setLayoutManager(layoutManager);
        savedBooksRecyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addToSavedBooksList(Book savedBook) {
        if(savedBooks == null)
            savedBooks = new ArrayList<>();

        savedBooks.add(savedBook);
        handleRecycler();
    }
}