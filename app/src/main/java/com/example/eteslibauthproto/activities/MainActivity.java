package com.example.eteslibauthproto.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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