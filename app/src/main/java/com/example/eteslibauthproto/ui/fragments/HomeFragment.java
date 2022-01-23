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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.example.eteslibauthproto.utils.adapters.BooksHorizontalListAdapter;
import com.example.eteslibauthproto.utils.localdatabase.AppDataManager;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    // private HomeViewModel homeViewModel;
    private User mUser;
    private ArrayList<Book> currentlyLoadedBookList;
    private FragmentHomeBinding binding;
    private AppDataManager dataManager;
    User currentUser;
    TextView usernameText;
    ImageView profileImageView;
    RecyclerView booksRecyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataManager = new AppDataManager(getActivity(), this);
        FirestoreClass.getUserDetailsFragment(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        profileImageView = binding.profileImageViewHome;
        usernameText = binding.usernameLabelHome;
        booksRecyclerView = binding.booksRecyclerView;

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

    public void setCurrentlyLoadedBookList(ArrayList<Book> bookArrayList) {
        if (currentlyLoadedBookList == null)
            currentlyLoadedBookList = new ArrayList<>();

        currentlyLoadedBookList.addAll(bookArrayList);

        updateUI();
    }

    private void updateUI() {
        BooksHorizontalListAdapter booksHorizontalListAdapter = new BooksHorizontalListAdapter(getContext(), currentlyLoadedBookList);
        booksRecyclerView.setAdapter(booksHorizontalListAdapter);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void storeUser(User u) {
        mUser = u;
        initialLoad();
    }
}