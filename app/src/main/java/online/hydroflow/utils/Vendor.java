package online.hydroflow.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

// needed for getString()
//import online.hydroflow.R;

public class Vendor {

    private Calendar c = Calendar.getInstance();

    private final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);   // Locale to USA for "." Decimal (unique pattern accepted for "Float")
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#", symbols);              // Format to 1 Decimal using Locale USA -> Float Happy!

    // Check for a valid email
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    // Check for a password longer than 7
    public static boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    // Create Toast
    public final void addToast(String txt, Activity a) {

        Toast t = Toast.makeText(a, txt, Toast.LENGTH_SHORT);
        TextView v = (TextView) t.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        t.show();
    }

    // Create Intent
    public final void addIntent(Activity a, Class c) {

        Intent i = new Intent(a, c);
        a.startActivity(i);
        a.finish();
    }

    // Create Months
//    public String[] addMonths() {
//        return new String[]{
//                getString(R.string.month_01), getString(R.string.month_02), getString(R.string.month_03), getString(R.string.month_04),
//                getString(R.string.month_05), getString(R.string.month_06), getString(R.string.month_07), getString(R.string.month_08),
//                getString(R.string.month_09), getString(R.string.month_10), getString(R.string.month_11), getString(R.string.month_12)};
//    }

    /**
     * Check Permission (API >= 23)
     * PERMISSION_DENIED -1
     * PERMISSION_GRANTED 0
     */
    public final int addPermissions(Activity a) {

        // defult 0 to grant permission when it's already granted then -> addFolder
        final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

        if (ContextCompat.checkSelfPermission(a,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }

        return MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
    }

    // Check if the folder exists, if not create it
    public final boolean addFolder() {

        final File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/HydroFlow");
        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return success;
    }

    // Get the current date
    public final String addDate() {

//        int day = c.get(Calendar.DAY_OF_MONTH);
//        int month = c.get(Calendar.MONTH);
//        int year = c.get(Calendar.YEAR);
//        String date = day + "-" + month + "-" + year;
//        return date;

        SimpleDateFormat df = new SimpleDateFormat("[yyyy-MM-dd]", Locale.GERMANY);
        return df.format(c.getTime());
    }

    // Get the current time
    public final String addTime() {

        String h, m, s;

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);

        if (hour < 10) {
            h = "0" + String.valueOf(hour);
        } else {
            h = String.valueOf(hour);
        }

        if (minutes < 10) {
            m = "0" + String.valueOf(minutes);
        } else {
            m = String.valueOf(minutes);
        }

        if (seconds < 10) {
            s = "0" + String.valueOf(seconds);
        } else {
            s = String.valueOf(seconds);
        }

        return "[" + h + "h" + m + "m" + s + "s]";
    }

    public float addRandom(int n1, int n2) {

        Random r = new Random();

        float f = r.nextFloat() + r.nextInt(n1) + n2;   // Random between [0.0,1.0] + random(n1) + n2
        return Float.valueOf(decimalFormat.format(f));  // Format n to 1 Decimal

    }

    public float addFormatDecimal(float f) {

        return Float.valueOf(decimalFormat.format(f));  // Format n to 1 Decimal

    }


}