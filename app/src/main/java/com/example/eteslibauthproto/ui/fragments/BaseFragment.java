package com.example.eteslibauthproto.ui.fragments;

import android.app.Dialog;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.eteslibauthproto.R;
import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment {

    void showSnackbarMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
