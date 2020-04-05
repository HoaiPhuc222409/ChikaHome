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
        editor.putString("createAt", user.getCreateAt());
        editor.putString("avatar", user.getAvatar());
        editor.putString("name", user.getName());
        editor.putString("birthday", user.getBirthday());
        editor.putString("address", user.getAddress());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putString("role", user.getRole());
        editor.apply();
    }

    public static User loadSelf(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String createAt = preferences.getString("crateAt", null);
        String avatar = preferences.getString("avatar", null);
        String name = preferences.getString("name", null);
        String birthday = preferences.getString("birthday", null);
        String address = preferences.getString("address", null);
        String email = preferences.getString("email", null);
        String phone = preferences.getString("phone", null);
        String role = preferences.getString("role", null);
        return new User(createAt, avatar, name, birthday, address, email, phone, role);
    }

    public static void clearSelf(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
