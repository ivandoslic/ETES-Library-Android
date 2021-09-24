package com.example.eteslibauthproto.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eteslibauthproto.R;
import com.google.android.material.button.MaterialButton;

public class ETESLibButton extends MaterialButton {

    private TypeFactory mFontFactory;

    public ETESLibButton(@NonNull Context context) {
        super(context);
    }

    public ETESLibButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ETESLibButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.JosefinSansTextView, 0, 0);
        int typefaceType;
        try {
            typefaceType = array.getInteger(R.styleable.JosefinSansTextView_font_name, 0);
        } finally {
            array.recycle();
        }

        if(!isInEditMode()) {
            setTypeface(getTypeface(typefaceType));
        }
    }

    public Typeface getTypeface(int type) {
        if (mFontFactory == null)
            mFontFactory = new TypeFactory(getContext());

        switch (type) {
            case Constants.JS_BOLD:
                return mFontFactory.bold;
            case Constants.JS_BOLD_ITALIC:
                return mFontFactory.boldItalic;
            case Constants.JS_EXTRA_LIGHT:
                return mFontFactory.extraLight;
            case Constants.JS_EXTRA_LIGHT_ITALIC:
                return mFontFactory.extraLightItalic;
            case Constants.JS_ITALIC:
                return mFontFactory.italic;
            case Constants.JS_LIGHT:
                return mFontFactory.light;
            case Constants.JS_LIGHT_ITALIC:
                return mFontFactory.lightItalic;
            case Constants.JS_MEDIUM:
                return mFontFactory.medium;
            case Constants.JS_MEDIUM_ITALIC:
                return mFontFactory.mediumItalic;
            case Constants.JS_SEMI_BOLD:
                return mFontFactory.semiBold;
            case Constants.JS_SEMI_BOLD_ITALIC:
                return mFontFactory.semiBoldItalic;
            case Constants.JS_THIN:
                return mFontFactory.thin;
            case Constants.JS_THIN_ITALIC:
                return mFontFactory.thinItalic;

            default:
                return mFontFactory.regular;
        }
    }

    public interface Constants {
        int JS_BOLD = 1,
                JS_BOLD_ITALIC = 2,
                JS_EXTRA_LIGHT = 3,
                JS_EXTRA_LIGHT_ITALIC = 4,
                JS_ITALIC = 5,
                JS_LIGHT = 6,
                JS_LIGHT_ITALIC = 7,
                JS_MEDIUM = 8,
                JS_MEDIUM_ITALIC = 9,
                JS_SEMI_BOLD = 11,
                JS_SEMI_BOLD_ITALIC = 12,
                JS_THIN = 13,
                JS_THIN_ITALIC = 14;
    }
}
