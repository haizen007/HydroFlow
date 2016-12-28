package online.hydroflow.app;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.Locale;

public class AppVendor {

    private static final String TAG = AppVendor.class.getSimpleName();

    // Check for a valid email
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    // Check for a password longer than 7
    public static boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

}
