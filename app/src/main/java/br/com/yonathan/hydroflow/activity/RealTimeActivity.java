package br.com.yonathan.hydroflow.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.yonathan.hydroflow.R;
import br.com.yonathan.hydroflow.utils.Vendor;
import br.com.yonathan.hydroflow.sql.SQLiteHandler;
import br.com.yonathan.hydroflow.sql.SessionManager;
import br.com.yonathan.hydroflow.utils.Constants;

public class RealTimeActivity extends Activity {

    private static final String TAG = ChartActivity.class.getSimpleName();

    private SQLiteHandler db;
    private SessionManager session;

    private final Vendor vendor = new Vendor();
    private final String date = vendor.addDate();
    private final String time = vendor.addTime();
    private boolean success;
    private int permission;

    private LineChart RealTIme;
    private float consumo, aux;
    private ValueEventListener listener;

    // Firebase connection
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebase1 = database.getReference(Constants.FIREBASE_VALUE_USUARIO);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        RealTIme = findViewById(R.id.RealTimeChart);

        // add Data
        LineData data = new LineData();

        // add empty data
        RealTIme.setData(data);

        // add Description
        Description d = new Description();              // Description Created
        d.setText("Real Time");                         // Description Text
        d.setTextSize(14f);                             // Description Text Size
        d.setEnabled(false);                            // Description Enabled?

        // add Legend
        Legend l = RealTIme.getLegend();                                    // Create Legend
        l.setForm(Legend.LegendForm.LINE);                                  // Legend Symbol Style
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);  // Legend Alignment
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);      // Legend Position
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);              // Legend Orientation
        l.setTextSize(14f);                                                 // Legend Text Size
        l.setDrawInside(false);                                             // Legend Inside?
        l.setEnabled(true);                                                 // Legend Enabled?

        XAxis x = RealTIme.getXAxis();
        x.setDrawAxisLine(false);            // X Axis Lines? (top and bottom)
        x.setDrawGridLines(false);           // X Axis Grid Lines?
        x.setTextColor(Color.BLACK);         // X Axis Value Text Color
        x.setGridColor(Color.LTGRAY);        // X Axis Grid Color
        x.setAxisLineColor(Color.LTGRAY);    // X Axis Line Color
        x.setAvoidFirstLastClipping(true);   // X Axis Avoid Clip
        x.setEnabled(false);                 // X Axis Enable?

        YAxis leftAxis = RealTIme.getAxisLeft();
        leftAxis.setGridColor(Color.TRANSPARENT);      // Y Axis Left - Grid Color (set just for one side and it will be apply for Left and Right)
        leftAxis.setAxisLineColor(Color.LTGRAY);       // Y Axis Left - Line Color
        leftAxis.setTextColor(Color.GRAY);             // Y Axis Left - Value Text Color
        leftAxis.setEnabled(true);                     // Y Axis Left Enable?

        YAxis rightAxis = RealTIme.getAxisRight();
        rightAxis.setAxisLineColor(Color.LTGRAY);      // Y Axis Right - Line Color
        rightAxis.setTextColor(Color.GRAY);            // Y Axis Right - Value Text Color
        leftAxis.setEnabled(true);                     // Y Axis Right Enable?

        RealTIme.setData(data);                        // Add Data to Chart
        RealTIme.setDescription(d);                    // Add Description
        RealTIme.setHighlightPerDragEnabled(false);    // HighLight to Drag Enabled?
        RealTIme.setPinchZoom(false);                  // if disabled, scaling can be done on x- and y-axis separately
        RealTIme.setDoubleTapToZoomEnabled(false);     // Double Tap > Zoom Enabled?
        RealTIme.setScaleEnabled(true);                // Zoom Enabled?
        RealTIme.setScaleYEnabled(false);              // Zoom Y Enable?
        RealTIme.setDragDecelerationEnabled(true);     // Continue to scroll
        RealTIme.setTouchEnabled(true);                // Touch Enabled?
        RealTIme.setDragEnabled(true);                 // Move chart with the finger
        RealTIme.setHardwareAccelerationEnabled(true); // Hardware Acceleration?
        RealTIme.animateX(2500);           // Animation

        // On Clicked Listener
        RealTIme.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                // Center Chart View When On Clicked Value Value
                RealTIme.centerViewToAnimated(e.getX(), e.getY(), RealTIme.getData().getDataSetByIndex(h.getDataSetIndex())
                        .getAxisDependency(), 500);

                vendor.addToast(h.getY() + " " + getString(R.string.milliliters), RealTimeActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listener
        RealTIme.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                permission = vendor.addPermissions(RealTimeActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = RealTIme.saveToPath(getString(R.string.chart) + " 5 - " + getString(R.string.chart_05_real_time_desc) + " " + date + " " + time, "/DCIM/HydroFlow");
                        if (save) {
                            vendor.addToast(getString(R.string.chart) + " 5\n" + getString(R.string.img_saved), RealTimeActivity.this);
                        } else {
                            vendor.addToast(getString(R.string.img_error_to_save), RealTimeActivity.this);
                        }
                    }
                }
                return success;
            }
        });

        // Just to fill with something on the screen before the Firebase data
        addEntry(0);

        // For every "delay milliseconds" it will add new Demo Data if Switch is TRUE
        final int delay = 1000;
        // Needs a Handler to handle the process
        final Handler handler = new Handler();
        // It will be executed after the given delay
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Creates random data between 5 and 95 with one decimal
                float demo = vendor.addRandom(90, 5);
                // Add random data to the Real Time Chart
                addEntry(demo);
                // After the delay it will repeat making the Switch be checked again creating a loop
                handler.postDelayed(this, delay);
            }
        };

        // Switch button to add Demo Data
        Switch sw = findViewById(R.id.switch_demo);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    vendor.addToast(getString(R.string.toast_demo_data_on), RealTimeActivity.this);
                    // After the delay it will repeat because there is another process like this inside the run()
                    handler.postDelayed(runnable, delay);
                } else {
                    vendor.addToast(getString(R.string.toast_demo_data_off), RealTimeActivity.this);
                    // Removes the runnable
                    handler.removeCallbacks(runnable);
                    addEntry(0);
                }
            }
        });

        Log.d(TAG, "##### RealTImeActivity - OK #####");

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkFirebase() {

        listener = firebase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consumo = dataSnapshot.child(Constants.FIREBASE_VALUE_CONSUMO).child(Constants.FIREBASE_VALUE_TEMPO_REAL).getValue(Float.class);
                Log.d(TAG, "##### Firebase: " + consumo + " mL #####");
                /*
                  Not necessary anymore, but in case of no data change on Firebase the final value will be 0.
                  That means no updates are coming from NodeMCU or the values from WaterRead are 0.
                  Otherwise, just "addEntry(consumo);" and delete the "aux" from scope
                 */
                if (aux == consumo) {
                    addEntry(0);
                } else {
                    addEntry(consumo);
                }
                aux = consumo;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read values
                Log.d(TAG, "#####  Failed to read values from Firebase #####", databaseError.toException());
            }
        });
    }

    private void addEntry(float consumo) {

        LineData dataSet = RealTIme.getData();

        if (dataSet != null) {

            ILineDataSet set = dataSet.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                dataSet.addDataSet(set);
            }

            dataSet.addEntry(new Entry(set.getEntryCount(), consumo), 0);

            dataSet.notifyDataChanged();

            // let the chart know it's data has changed
            RealTIme.notifyDataSetChanged();

            // limit the number of visible entries
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                RealTIme.setVisibleXRangeMaximum(5);
            } else {
                RealTIme.setVisibleXRangeMaximum(10);
            }

            // move to the latest entry
            RealTIme.moveViewToX(dataSet.getEntryCount());

        }
    }

    private LineDataSet createSet() {

        LineDataSet set1 = new LineDataSet(null, getString(R.string.chart_05_real_time_leg));
        set1.setFormSize(20f);                    // Line Size
        set1.setColor(Color.GREEN);               // Line Color
        set1.setCircleRadius(4f);                 // Circle Size
        set1.setCircleColor(Color.GREEN);         // Circle Color
        set1.setCircleHoleRadius(3f);             // Circle Hole Size
        set1.setCircleColorHole(Color.GREEN);     // Circle Hole Color
        set1.setDrawCircleHole(false);            // Circle Hole Draw?
        set1.setHighlightLineWidth(1.2f);         // HighLight Size
        set1.setHighLightColor(Color.GREEN);      // HighLight Color
        set1.setFillAlpha(50);                    // Fill Alpha
        set1.setFillColor(Color.GREEN);           // Fill Color
        set1.setDrawFilled(true);                 // Fill Enabled?
        set1.setValueTextSize(11f);               // Text Size
        set1.setValueTextColor(Color.BLACK);      // Text Color
        set1.setDrawValues(true);                 // Text Enable?
        return set1;
    }

    public void logoutUser(View v) {
        session.setLogin(false);
        // Delete the Table
        db.deleteUser();
        // Launching the login activity
        vendor.addIntent(RealTimeActivity.this, LoginActivity.class);
    }

    @Override
    public void onBackPressed() {
        vendor.addIntent(RealTimeActivity.this, MainActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "##### onStart ##### ");
        // call the Firebase Listeners
        checkFirebase();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "##### onResume ##### ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebase1.removeEventListener(listener);
        Log.d(TAG, "##### onPause ##### ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "##### onStop ##### ");
    }

}
