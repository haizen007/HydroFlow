package br.com.yonathan.hydroflow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import br.com.yonathan.hydroflow.BuildConfig;
import br.com.yonathan.hydroflow.R;
import br.com.yonathan.hydroflow.utils.Vendor;
import br.com.yonathan.hydroflow.sql.SQLiteHandler;
import br.com.yonathan.hydroflow.sql.SessionManager;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Boolean exit = false;

    private SQLiteHandler db;
    private SessionManager session;

    private final Vendor vendor = new Vendor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtAppVersion = (TextView) findViewById(R.id.tv_app_version);
        TextView txtName = (TextView) findViewById(R.id.tv_name);
        TextView txtEmail = (TextView) findViewById(R.id.tv_email);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = getString(R.string.hint_name) + ": " + user.get("NOME");
        String email = getString(R.string.hint_email) + ": " + user.get("EMAIL");

        // Displaying the user details on the screen
        txtAppVersion.setText(BuildConfig.VERSION_NAME);
        txtName.setText(name);
        txtEmail.setText(email);

        Log.d(TAG, "##### MainActivity - OK #####");
    }

    /*
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from SQLite users table
     */
    public void logoutUser(View v) {
        session.setLogin(false);
        // Delete the Table
        db.deleteUser();
        // Launching the login activity
        vendor.addIntent(MainActivity.this, LoginActivity.class);
    }

    // Go to RealTime Activity
    public void goRealTimeActivity(View v) {
        vendor.addIntent(MainActivity.this, RealTimeActivity.class);
    }

    // Go to Chart Activity
    public void goChartActivity(View v) {
        vendor.addIntent(MainActivity.this, ChartActivity.class);
    }

    // Back pressed twice to exit
    public void onBackPressed() {
        if (exit) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(i);
            // System.exit(0); // will force kill
            finish();
        } else {
            String txt = getString(R.string.exit);
            vendor.addToast(txt, MainActivity.this);
            exit = true;
            // Time to press it again is 3 seconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

}
