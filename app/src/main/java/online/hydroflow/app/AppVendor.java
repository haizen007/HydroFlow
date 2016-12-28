package online.hydroflow.app;

import android.app.Activity;


public class AppVendor extends Activity {

    // Check for a valid email
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    // Check for a password longer than 7
    public static boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

}


