package com.example.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Timestamp;

public class AppData {

    SharedPreferences prefs;
    Context context;

    public AppData(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("MiaBoo", 0);
    }

    public void SaveUser(String userID, String userName, String email, String userType) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userid", userID);
        editor.putString("username", userName);
        editor.putString("email", email);
        editor.putString("user_type", userType);
        editor.apply();
    }

    public String getUserId() {
        return prefs.getString("userid", "");
    }

    public String getUsername() {
        return prefs.getString("username", "");
    }

    public String getEmail() {
        return prefs.getString("email", "");
    }

    public Boolean isAdmin() {
        String userType =  prefs.getString("user_type", "");
        return userType == "admin";
    }
}
