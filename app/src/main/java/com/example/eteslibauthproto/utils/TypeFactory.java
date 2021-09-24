package com.example.eteslibauthproto.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFactory {

    private String JS_BOLD = "JosefinSans-Bold.ttf";
    private String JS_BOLD_ITALIC = "JosefinSans-BoldItalic.ttf";
    private String JS_EXTRA_LIGHT = "JosefinSans-ExtraLight.ttf";
    private String JS_EXTRA_LIGHT_ITALIC = "JosefinSans-ExtraLightItalic.ttf";
    private String JS_ITALIC = "JosefinSans-Italic.ttf";
    private String JS_LIGHT = "JosefinSans-Light.ttf";
    private String JS_LIGHT_ITALIC = "JosefinSans-LightItalic.ttf";
    private String JS_MEDIUM = "JosefinSans-Medium.ttf";
    private String JS_MEDIUM_ITALIC = "JosefinSans-MediumItalic.ttf";
    private String JS_REGULAR = "JosefinSans-Regular.ttf";
    private String JS_SEMI_BOLD = "JosefinSans-SemiBold.ttf";
    private String JS_SEMI_BOLD_ITALIC = "JosefinSans-SemiBoldItalic.ttf";
    private String JS_THIN = "JosefinSans-Thin.ttf";
    private String JS_THIN_ITALIC = "JosefinSans-ThinItalic.ttf";

    Typeface bold, boldItalic, extraLight, extraLightItalic, italic, light, lightItalic, medium, mediumItalic,
            regular, semiBold, semiBoldItalic, thin, thinItalic;

    public TypeFactory(Context context) {
        bold = Typeface.createFromAsset(context.getAssets(), JS_BOLD);
        boldItalic = Typeface.createFromAsset(context.getAssets(), JS_BOLD_ITALIC);
        extraLight = Typeface.createFromAsset(context.getAssets(), JS_EXTRA_LIGHT);
        extraLightItalic = Typeface.createFromAsset(context.getAssets(), JS_EXTRA_LIGHT_ITALIC);
        italic = Typeface.createFromAsset(context.getAssets(), JS_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(), JS_LIGHT);
        lightItalic = Typeface.createFromAsset(context.getAssets(), JS_LIGHT_ITALIC);
        medium = Typeface.createFromAsset(context.getAssets(), JS_MEDIUM);
        mediumItalic = Typeface.createFromAsset(context.getAssets(), JS_MEDIUM_ITALIC);
        regular = Typeface.createFromAsset(context.getAssets(), JS_REGULAR);
        semiBold = Typeface.createFromAsset(context.getAssets(), JS_SEMI_BOLD);
        semiBoldItalic = Typeface.createFromAsset(context.getAssets(), JS_SEMI_BOLD_ITALIC);
        thin = Typeface.createFromAsset(context.getAssets(), JS_THIN);
        thinItalic = Typeface.createFromAsset(context.getAssets(), JS_THIN_ITALIC);
    }

}
