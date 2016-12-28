package online.hydroflow.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import online.hydroflow.R;

public class AppVendor extends FragmentActivity {

    // Check for a valid email
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    // Check for a password longer than 7
    public static boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    public void addToast(String txt, Activity a) {
        Toast t = Toast.makeText(a, txt, Toast.LENGTH_SHORT);
        TextView v = (TextView) t.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        t.show();
    }

    public void addIntent(Activity a, Class c) {
        Intent i = new Intent(a, c);
        a.startActivity(i);
        a.finish();
    }

    public String[] addMonths() {
        String[] meses = {
                getString(R.string.month_01), getString(R.string.month_02), getString(R.string.month_03), getString(R.string.month_04),
                getString(R.string.month_05), getString(R.string.month_06), getString(R.string.month_07), getString(R.string.month_08),
                getString(R.string.month_09), getString(R.string.month_10), getString(R.string.month_11), getString(R.string.month_12)};
        return meses;
    }

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
//    }

//    protected Typeface mTfRegular;
//    protected Typeface mTfLight;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
//    }
}
