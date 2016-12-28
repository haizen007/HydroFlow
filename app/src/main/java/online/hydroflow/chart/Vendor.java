package online.hydroflow.chart;

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
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// needed for getString()
//import online.hydroflow.R;

public class Vendor extends FragmentActivity {

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

        // defult 0 to grant permission when it's already granted, needed to -> addFolder
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

    // Create Folder
    private final File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/HydroFlow");
    private boolean success = true;

    public final boolean addFolder() {

        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return success;
    }

    // get Current Date
    public final String addDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("[dd-MM-yyyy]", Locale.US);

        return df.format(c.getTime());
    }

}