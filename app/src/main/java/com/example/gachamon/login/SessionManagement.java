package com.example.gachamon.login;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String unlogged = "unlogged";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user) {
        //save session of user whenever user is logged in
        //  int id = user.getId();
        String email = user.getEmail();

        //editor.putInt(SESSION_KEY,id).commit();
        editor.putString(SESSION_KEY, email).commit();
    }

    // public int getSession(){
    public String getSession() {
        //return user id whose session is saved

        //       return sharedPreferences.getInt(SESSION_KEY,-1);
        return sharedPreferences.getString(SESSION_KEY, unlogged);
    }


    public void removeSession() {
        //   editor.putInt(SESSION_KEY,-1).commit();
        editor.putString(SESSION_KEY, unlogged).commit();

    }
}