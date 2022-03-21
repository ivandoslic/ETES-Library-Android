package com.example.eteslibauthproto.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.databinding.FragmentHomeBinding;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.ui.activities.BookPreviewActivity;
import com.example.eteslibauthproto.ui.activities.SearchActivity;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.example.eteslibauthproto.utils.adapters.BooksHorizontalListAdapter;
import com.example.eteslibauthproto.utils.localdatabase.AppDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends BaseFragment {

    // private HomeViewModel homeViewModel;
    private User mUser;
    private ArrayList<Book> currentlyLoadedBookList;
    private ArrayList<Book> randomBooks;
    private ArrayList<Book> specificAuthorBooks;
    private FragmentHomeBinding binding;
    private AppDataManager dataManager;
    TextView usernameText;
    ImageView profileImageView;
    RecyclerView booksRecyclerView, specificAuthorBooksRecyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataManager = new AppDataManager(getActivity(), this);
        FirestoreClass.getUserDetailsFragment(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        profileImageView = binding.profileImageViewHome;
        usernameText = binding.usernameLabelHome;
        booksRecyclerView = binding.booksRecyclerView;
        specificAuthorBooksRecyclerView = binding.specificAuthorBooksRecyclerView;
        CardView search = binding.homeFragmentSearchCard;

        search.setOnClickListener(v -> {
                Intent i = new Intent(getContext(), SearchActivity.class);
                startActivity(i);
        });

        return root;
    }

    private void initialLoad() {
        if(mUser != null) {
            GlideLoader.loadImageFromURLToFragment(this, mUser.getImage(), profileImageView);
            usernameText.setText(mUser.getName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentlyLoadedBookList != null) {
            updateUI();
            initialLoad();
        }
    }

    public void setCurrentlyLoadedBookList(ArrayList<Book> bookArrayList) {
        if (currentlyLoadedBookList == null)
            currentlyLoadedBookList = new ArrayList<>();

        currentlyLoadedBookList.addAll(bookArrayList);

        sortCollectedData();
    }

    private void sortCollectedData() {
        if(specificAuthorBooks == null)
            specificAuthorBooks = new ArrayList<>();

        for(Book book : currentlyLoadedBookList) {
            if(book.getAuthorName().compareTo("Miroslav Krleza") == 0) {
                specificAuthorBooks.add(book);
            }
        }

        if(randomBooks == null)
            randomBooks = new ArrayList<>();

        if(currentlyLoadedBookList.size() > 5) {
            int maxCount = 5;
            for (int i = 0; i < maxCount; i++) {
                if(currentlyLoadedBookList.get(i).getAuthorName().compareTo("Miroslav Krleza") != 0) {
                    randomBooks.add(currentlyLoadedBookList.get(i));
                } else {
                    maxCount++;
                }
            }
        } else {
            randomBooks.addAll(currentlyLoadedBookList);
        }

        updateUI();
    }

    private void updateUI() {
        BooksHorizontalListAdapter booksHorizontalListAdapter = new BooksHorizontalListAdapter(getContext(), randomBooks);

        booksHorizontalListAdapter.setOnItemClickListener(new BooksHorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(getActivity(), BookPreviewActivity.class);
                i.putExtra(Constants.BOOK_PREVIEW_INTENT_NAME, randomBooks.get(position));
                i.putExtra(Constants.EXTRA_USER_DETAILS, mUser);
                startActivity(i);
            }
        });

        booksRecyclerView.setAdapter(booksHorizontalListAdapter);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        BooksHorizontalListAdapter booksHorizontalListAdapterSpecificAuthor = new BooksHorizontalListAdapter(getContext(), specificAuthorBooks);

        booksHorizontalListAdapterSpecificAuthor.setOnItemClickListener(new BooksHorizontalListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(getActivity(), BookPreviewActivity.class);
                i.putExtra(Constants.BOOK_PREVIEW_INTENT_NAME, specificAuthorBooks.get(position));
                startActivity(i);
            }
        });

        specificAuthorBooksRecyclerView.setAdapter(booksHorizontalListAdapterSpecificAuthor);
        specificAuthorBooksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void storeUser(User u) {
        mUser = u;
        initialLoad();
    }
}