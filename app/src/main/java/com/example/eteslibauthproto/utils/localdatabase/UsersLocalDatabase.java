package com.example.eteslibauthproto.utils.localdatabase;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.Nullable;

import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;

import java.util.HashMap;

public class UsersLocalDatabase extends LocalDatabaseManager {

    public UsersLocalDatabase(@Nullable Context context) {
        super(context, Constants.USERS_LOCAL_DATABASE_TABLE, getUserDatabaseColumnsStructure());
    }

    // Main UserLocalDatabase methods :

    public void addUserToDatabase(User user) {
        ContentValues userEntry = convertUserToContentValues(user);
        if(!this.addData(userEntry)) {
            // TODO : Handle addingDataError...
        }
    }

    // TODO: CREATE public void updateUserInDatabase(User user) { ... }

    // TODO: CREATE public User getUserWithID(String usersUID) { ... }

    // Utility methods :

    private ContentValues convertUserToContentValues(User user) {
        ContentValues userContentValues = new ContentValues();

        userContentValues.put("email", user.getEmail());
        userContentValues.put("gender", user.getGender());
        userContentValues.put("id", user.getId());
        userContentValues.put("image", user.getImage());
        userContentValues.put("name", user.getName());
        userContentValues.put("schoolingYear", user.getSchoolingYear());

        return userContentValues;
    }

    // Method for getting HashMap (Structure) of users database column :

    public static HashMap<String, ColumnType> getUserDatabaseColumnsStructure() {
        HashMap<String, ColumnType> tempHashMap = new HashMap<>();

        tempHashMap.put("email", ColumnType.TEXT);
        tempHashMap.put("gender", ColumnType.TEXT);
        tempHashMap.put("id", ColumnType.TEXT);
        tempHashMap.put("image", ColumnType.TEXT);
        tempHashMap.put("name", ColumnType.TEXT);
        tempHashMap.put("schoolingYear", ColumnType.INTEGER);

        return tempHashMap;
    }
}
