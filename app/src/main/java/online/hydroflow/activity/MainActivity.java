package online.hydroflow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import online.hydroflow.BuildConfig;
import online.hydroflow.R;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;

public class MainActivity extends Activity {

    private TextView txtAppVersion;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtCPF;
    private Button btnLogout;
    private Boolean exit = false;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAppVersion = (TextView) findViewById(R.id.tv_app_version);
        txtName = (TextView) findViewById(R.id.tv_name);
        txtEmail = (TextView) findViewById(R.id.tv_email);
        txtCPF = (TextView) findViewById(R.id.tv_cpf);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();


        String name = getString(R.string.hint_name) + ": " + user.get("NOME");
        String email = getString(R.string.hint_email) + ": " + user.get("EMAIL");
        String cpf = getString(R.string.hiny_cpf) + ": " + user.get("CPF");

        // Displaying the user details on the screen
        txtAppVersion.setText(BuildConfig.VERSION_NAME);
        txtName.setText(name);
        txtEmail.setText(email);
        txtCPF.setText(cpf);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        // Delete the Table
        db.deleteUser();

        // Launching the login activity
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    // Back pressed twice to exit
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            System.exit(0); // force kill
            finish();
        } else {
            // get TXT for Toast
            String txt = getString(R.string.exit);
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
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
