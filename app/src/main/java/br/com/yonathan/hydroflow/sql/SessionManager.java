package br.com.yonathan.hydroflow.sql;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {

    private static final String TAG = SessionManager.class.getSimpleName();
    private final SharedPreferences pref;
    private final Editor editor;
    private static final String PREF_NAME = "HydroFlow";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public SessionManager(Context c) {
        int PRIVATE_MODE = 0;
        pref = c.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();

        Log.d(TAG, "##### User Login Session Modified to: " + isLoggedIn + " #####");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

}
