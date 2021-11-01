package com.example.eteslibauthproto.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.databinding.FragmentProfileBinding;
import com.example.eteslibauthproto.ui.activities.SettingsActivity;

public class ProfileFragment extends Fragment {

    // private NotificationsViewModel notificationsViewModel;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /* TODO: Get user object to be able to load the public profile, if possible get user object
         *       in DashboardActivity and then pass it to all fragments for easier implementation
         *       and making less requests to server
         */

        ImageButton settings = binding.profileSettingsButton;

        /* TODO: Check if user is looking at his profile or other profile and allow or disallow him
         *       to access settings accordingly
         */

        settings.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}