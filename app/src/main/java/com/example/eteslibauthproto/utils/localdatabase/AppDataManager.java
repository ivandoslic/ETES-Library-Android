package com.example.eteslibauthproto.utils.localdatabase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.ui.fragments.HomeFragment;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.misc.DatesInStringComparator;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AppDataManager {
    private BooksLocalDatabase booksLocalDatabase;
    private Context currentContext;
    private Fragment currentFragment;
    private Activity currentActivity;

    public AppDataManager(Context context, Fragment fragment) {
        // booksLocalDatabase = new BooksLocalDatabase(context);
        currentContext = context;
        currentFragment = fragment;
        checkForUpdates();
    }

    private void checkForUpdates() {
        SharedPreferences sharedPreferences = currentContext.getSharedPreferences(Constants.ETES_LIB_PREFERENCES, Context.MODE_PRIVATE);
        String lastFetched = sharedPreferences.getString(Constants.USER_LAST_LOGGED_TIME, "There is a problem...");
        String currentTime = Calendar.getInstance().getTime().toString();

        FirestoreClass.getTenBooks(this); // Don't always do this, first check if you actually need to make a request
    }

    public void setLoadedBooks(ArrayList<DocumentSnapshot> bookDocs) {
        ArrayList<Book> tempBookList = new ArrayList<>();
        bookDocs.forEach((bookDoc) -> {
            Book tempBook = new Book(bookDoc);
            tempBookList.add(tempBook);
        });

        if(currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).setCurrentlyLoadedBookList(tempBookList);
        }
    }

    public Cursor getBooksCursor() {
        return booksLocalDatabase.getAllData();
    }
}
