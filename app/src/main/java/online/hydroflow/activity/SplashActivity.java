package online.hydroflow.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import online.hydroflow.R;
import online.hydroflow.helper.SessionManager;

public class SplashActivity extends Activity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            startMainClass();
        } else {

            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(1250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startMainClass();
                    }
                }
            };
            timerThread.start();
        }
    }

    protected void startMainClass() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

