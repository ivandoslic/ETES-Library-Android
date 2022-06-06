package com.example.eteslibauthproto.ui.fragments;

import android.app.Dialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment {

    void showSnackbarMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private Dialog mProgressDialog;

    void showProgressDialog(String text) {
        mProgressDialog = new Dialog(getContext());

        mProgressDialog.setContentView(R.layout.dialog_progress);

        ETESLibTextView loadingText = (ETESLibTextView) mProgressDialog.findViewById(R.id.progressText);
        loadingText.setText(text);

        ImageView logo =(ImageView) mProgressDialog.findViewById(R.id.progressImageView);
        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
        logo.startAnimation(rotation);

        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.dismiss();
    }
}
