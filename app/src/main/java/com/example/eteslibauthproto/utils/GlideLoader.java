package com.example.eteslibauthproto.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eteslibauthproto.R;

public class GlideLoader {
    Context context;

    public GlideLoader(Context c) {
        this.context = c;
    }

    public void loadUserPicture(Object image, ImageView imageView) {
        try {

            Glide.with(context)
                    .load(image)
                    .centerCrop()
                    .placeholder(R.drawable.profile_placeholder_image)
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageFromURLToFragment(Fragment fragment, Object image, ImageView imageView) {
        try {
            Glide.with(fragment)
                    .load(image)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
