package online.hydroflow.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import online.hydroflow.R;
import online.hydroflow.chart.Vendor;
import online.hydroflow.helper.SessionManager;
@RequiresApi(api = Build.VERSION_CODES.N)

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private final Vendor vendor = new Vendor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Session manager
        SessionManager session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {

            vendor.addIntent(SplashActivity.this, MainActivity.class);

        } else {

            Thread timerThread = new Thread() {
                public void run() {

                    try {
                        sleep(1250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        vendor.addIntent(SplashActivity.this, LoginActivity.class);
                    }
                }
            };
            timerThread.start();
        }

        Log.d(TAG, "##### SplashActivity - OK #####");
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