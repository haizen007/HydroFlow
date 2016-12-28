package online.hydroflow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import online.hydroflow.R;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;

public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        } else {

            Thread timerThread = new Thread() {
                public void run() {

                    try {
                        sleep(1250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            };
            timerThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}

//    // To check if is the first time the app is open, if not skip the Splash
//    SharedPreferences settings = this.getSharedPreferences("appInfo", 0);
//    boolean firstTime = settings.getBoolean("first_time", true);
//    if (firstTime) {
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("first_time", false);
//        editor.commit();
//
//        Thread timerThread = new Thread() {
//            public void run() {
//                try {
//                    sleep(1250);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    startMainClass();
//                }
//            }
//        };
//        timerThread.start();
//
//    } else {
//        startMainClass();
//    }

