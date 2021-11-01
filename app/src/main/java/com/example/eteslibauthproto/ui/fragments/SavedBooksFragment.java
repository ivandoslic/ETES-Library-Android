package com.example.eteslibauthproto.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.databinding.FragmentSavedBooksBinding;

public class SavedBooksFragment extends Fragment {

    // private DashboardViewModel dashboardViewModel;
    private FragmentSavedBooksBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentSavedBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;

        textView.setText("Dash Frag!");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}