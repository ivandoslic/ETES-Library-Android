package com.example.eteslibauthproto.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eteslibauthproto.R;

public class GlideLoader {
    Context context;

    public GlideLoader(Context c) {
        this.context = c;
    }

    public void loadUserPicture(Uri imageUri, ImageView imageView) {
        try {

            Glide.with(context)
                    .load(imageUri)
                    .centerCrop()
                    .placeholder(R.drawable.profile_placeholder_image)
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
