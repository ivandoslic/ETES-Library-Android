package com.example.eteslibauthproto.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity {

    private Dialog mProgressDialog;

    void showErrorSnackBar(String message, boolean isError) {
        Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackBar.getView();

        if(isError) {
            //snackBarView.setBackgroundColor(ContextCompat.getColor(BaseActivity.this, R.color.color_snack_bar_error));
            snackBarView.setBackground(ContextCompat.getDrawable(BaseActivity.this, R.drawable.snackbar_error_round_corner));
        } else {
            //snackBarView.setBackgroundColor(ContextCompat.getColor(BaseActivity.this, R.color.color_snack_bar_success));
            snackBarView.setBackground(ContextCompat.getDrawable(BaseActivity.this, R.drawable.snackbar_success_round_corner));
        }

        snackBar.setTextColor(getColor(R.color.white));

        snackBar.show();
    }

    void showProgressDialog(String text) {
        mProgressDialog = new Dialog(this);

        mProgressDialog.setContentView(R.layout.dialog_progress);

        ETESLibTextView loadingText = (ETESLibTextView) mProgressDialog.findViewById(R.id.progressText);
        loadingText.setText(text);

        ImageView logo =(ImageView) mProgressDialog.findViewById(R.id.progressImageView);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        logo.startAnimation(rotation);

        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.dismiss();
    }

}