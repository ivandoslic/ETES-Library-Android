package com.example.eteslibauthproto.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibTextView;

public class MainActivity extends AppCompatActivity {

    ETESLibTextView regularText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regularText = (ETESLibTextView) findViewById(R.id.regularFont);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.ETES_LIB_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "");

        regularText.setText("Hello " + username);
    }
}