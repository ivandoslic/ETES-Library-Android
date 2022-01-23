package com.example.eteslibauthproto.utils.localdatabase;

import static com.example.eteslibauthproto.utils.localdatabase.ColumnType.INTEGER;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class LocalDatabaseManager extends SQLiteOpenHelper {

    private String TABLE_NAME;
    private HashMap<String, ColumnType> COLUMNS;

    // Simple constructor (Custom made) :

    public LocalDatabaseManager(@Nullable Context context, @Nullable String name, HashMap<String, ColumnType> columns) {
        super(context, name, null, 1);
        setCOLUMNS(columns);
        setTABLE_NAME(name);
    }

    // Factory constructors:

    public LocalDatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public LocalDatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public LocalDatabaseManager(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    // Indispensable methods:

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createTableCommand = getCreateTableCommand();
        database.execSQL(createTableCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE if EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    // Main database functions/methods :

    public boolean addData(ContentValues databaseEntry) {
        SQLiteDatabase database = this.getWritableDatabase();
        long result = database.insert(TABLE_NAME, null, databaseEntry);

        /* insert() function returns -1 on failure and some other number if it has succeeded so according to that we return
         * false if it failed and true if it succeeded, so we can make some error handling in activities
         */

        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        return database.rawQuery(query, null);
    }

    // TODO: MAKE public boolean updateData(HashMap<K, V> updatedEntries) { ... }

    public void clearDatabase() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;

        database.execSQL(query);
    }

    // Getters and setters :

    private void setTABLE_NAME(String name) {
        this.TABLE_NAME = name;
    }

    private void setCOLUMNS(HashMap<String, ColumnType> columns) {
        this.COLUMNS = columns;
    }

    // Utility methods :

    private String getCreateTableCommand() {
        StringBuilder result = new StringBuilder("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        for(Map.Entry<String, ColumnType> entry : COLUMNS.entrySet()) {
            String columnName = entry.getKey();
            ColumnType columnType = entry.getValue();

            String columnTypeString = columnType.name();

            result.append(columnName).append(" ").append(columnTypeString).append(", ");
        }

        result.deleteCharAt(result.length() - 1);

        result.append(")");

        return result.toString();
    }
}
