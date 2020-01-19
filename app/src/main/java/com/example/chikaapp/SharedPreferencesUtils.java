package com.example.chikaapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chikaapp.model.Token;
import com.example.chikaapp.model.User;

public class SharedPreferencesUtils {

    public static void saveToken(Context context, Token token) {
        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token.getTokenType() + " " + token.getAccessToken());
        editor.apply();
    }

    public static String loadToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        return token;
    }

    public static void clearToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveStatusLogin(Context context, boolean status) {
        SharedPreferences preferences = context.getSharedPreferences("status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("status", status);
        editor.apply();
    }

    public static boolean loadStatusLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("status", Context.MODE_PRIVATE);
        return preferences.getBoolean("status", false);
    }

    public static void saveSelf(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.apply();
    }

    public static User loadSelf(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = preferences.getString("name", null);
        String username = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        return new User(name, username, email);
    }

    public static void clearSelf(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
