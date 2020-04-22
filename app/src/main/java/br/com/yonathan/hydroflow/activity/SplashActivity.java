package br.com.yonathan.hydroflow.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import android.os.Handler;
import android.util.Log;

import br.com.yonathan.hydroflow.R;
import br.com.yonathan.hydroflow.utils.Vendor;
import br.com.yonathan.hydroflow.sql.SessionManager;

@RequiresApi(api = Build.VERSION_CODES.N)

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private final Vendor vendor = new Vendor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Session manager
        final SessionManager session = new SessionManager(getApplicationContext());

        // Delay in milliseconds
        final int delay = 1000;
        // Needs a Handler to handle the process
        final Handler h = new Handler();
        // It will be executed after the given delay
        final Runnable r = new Runnable() {
            public void run() {
                // Check if user is already logged-in
                if (session.isLoggedIn()) {
                    vendor.addIntent(SplashActivity.this, MainActivity.class);
                } else {
                    vendor.addIntent(SplashActivity.this, LoginActivity.class);
                }
            }
        };
        // Execute the Runnable after the given delay
        h.postDelayed(r, delay);
        Log.d(TAG, "##### SplashActivity - OK #####");
    }

}