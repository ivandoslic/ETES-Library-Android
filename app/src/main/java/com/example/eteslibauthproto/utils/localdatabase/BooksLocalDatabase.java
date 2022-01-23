package com.example.eteslibauthproto.utils.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.utils.Constants;

import java.util.HashMap;

public class BooksLocalDatabase extends LocalDatabaseManager {
    public BooksLocalDatabase(@Nullable Context context) {
        super(context, Constants.BOOKS_LOCAL_DATABASE_TABLE, getBooksDatabaseColumnsStructure());
    }

    public void addBookToDatabase(Book book) {
        ContentValues bookEntry = convertBookToContentValues(book);
        if(!this.addData(bookEntry)) {
            // TODO : Handle addingDataError
        }
    }

    public void addBooksToDatabase(Book... books) {
        for (Book book : books) {
            addBookToDatabase(book);
        }
    }

    private ContentValues convertBookToContentValues(Book book) {
        ContentValues bookContentValues = new ContentValues();

        // bookContentValues.put("id", book.getId());
        bookContentValues.put("amount", book.getAmount());
        bookContentValues.put("authorName", book.getAuthorName());
        bookContentValues.put("cover", book.getCover());
        bookContentValues.put("description", book.getDescription());
        bookContentValues.put("genre", book.getGenre());
        bookContentValues.put("language", book.getLanguage());
        bookContentValues.put("title", book.getTitle());
        bookContentValues.put("yearWritten", book.getYearWritten());

        return bookContentValues;
    }

    private static HashMap<String, ColumnType> getBooksDatabaseColumnsStructure() {
        HashMap<String, ColumnType> tempHashMap = new HashMap<>();

        // tempHashMap.put("id", ColumnType.TEXT);
        tempHashMap.put("amount", ColumnType.INTEGER);
        // tempHashMap.put("authorID", ColumnType.TEXT);
        tempHashMap.put("authorName", ColumnType.TEXT);
        tempHashMap.put("cover", ColumnType.TEXT);
        tempHashMap.put("description", ColumnType.TEXT);
        tempHashMap.put("genre", ColumnType.TEXT);
        tempHashMap.put("language", ColumnType.TEXT);
        tempHashMap.put("title", ColumnType.TEXT);
        tempHashMap.put("yearWritten", ColumnType.TEXT);

        return tempHashMap;
    }


}
