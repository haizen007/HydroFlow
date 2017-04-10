package online.hydroflow.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import online.hydroflow.R;
import online.hydroflow.chart.Vendor;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;

public class RealTimeActivity extends Activity {

    private static final String TAG = ChartActivity.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;

    private LineChart realTime;

    private final Vendor vendor = new Vendor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        Button btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        realTime = (LineChart) findViewById(R.id.RealTimeChart);

        // add Data
        LineData data = new LineData();

        // add empty data
        realTime.setData(data);

        // add Description
        Description d = new Description();              // Description Created
        d.setText("Real Time");                         // Description Text
        d.setTextSize(14f);                             // Description Text Size
        d.setEnabled(false);                            // Description Enabled?

        // add Legend
        Legend l = realTime.getLegend();                                    // Create Legend
        l.setForm(Legend.LegendForm.LINE);                                  // Legend Symbol Style
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);  // Legend Alignment
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);      // Legend Position
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);              // Legend Orientation
        l.setTextSize(14f);                                                 // Legend Text Size
        l.setDrawInside(false);                                             // Legend Inside?
        l.setEnabled(true);                                                 // Legend Enabled?

        XAxis x = realTime.getXAxis();
        x.setDrawAxisLine(false);            // X Axis Lines? (top and bottom)
        x.setDrawGridLines(false);           // X Axis Grid Lines?
        x.setTextColor(Color.BLACK);         // X Axis Value Text Color
        x.setGridColor(Color.LTGRAY);        // X Axis Grid Color
        x.setAxisLineColor(Color.LTGRAY);    // X Axis Line Color
        x.setAvoidFirstLastClipping(true);   // X Axis Avoid Clip
        x.setEnabled(false);                 // X Axis Enable?

        YAxis leftAxis = realTime.getAxisLeft();
        leftAxis.setGridColor(Color.TRANSPARENT);   // Y Axis Left - Grid Color (set just for one side and it will be apply for Left and Right)
        leftAxis.setAxisLineColor(Color.LTGRAY);    // Y Axis Left - Line Color
        leftAxis.setTextColor(Color.GRAY);          // Y Axis Left - Value Text Color
        leftAxis.setEnabled(true);                  // Y Axis Left Enable?

        YAxis rightAxis = realTime.getAxisRight();
        rightAxis.setAxisLineColor(Color.LTGRAY);   // Y Axis Right - Line Color
        rightAxis.setTextColor(Color.GRAY);         // Y Axis Right - Value Text Color
        leftAxis.setEnabled(true);                  // Y Axis Right Enable?

        realTime.setData(data);                        // Add Data to Chart
        realTime.setDescription(d);                    // Add Description
        realTime.setHighlightPerDragEnabled(false);    // HighLight to Drag Enabled?
        realTime.setPinchZoom(false);                  // if disabled, scaling can be done on x- and y-axis separately
        realTime.setDoubleTapToZoomEnabled(false);     // Double Tap > Zoom Enalbed?
        realTime.setScaleEnabled(true);                // Zoom Enabled?
        realTime.setScaleYEnabled(false);              // Zoom Y Enable?
        realTime.setDragDecelerationEnabled(true);     // Continue to scroll
        realTime.setTouchEnabled(true);                // Touch Enabled?
        realTime.setDragEnabled(true);                 // Move chart with the finger
        realTime.setHardwareAccelerationEnabled(true); // Hardware Accelaration?
        realTime.animateX(2500);                       // Animation

        // On Clicked Listner
        realTime.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                // Center Chart View When On Clicked Value Value
                realTime.centerViewToAnimated(e.getX(), e.getY(), realTime.getData().getDataSetByIndex(h.getDataSetIndex())
                        .getAxisDependency(), 500);

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String yML = String.valueOf(h.getY());

                // get X index -> String Time
                String xSEG = String.valueOf(pos + 1);

                vendor.addToast(xSEG + "s" + "\n" + yML + " " + getString(R.string.milliliters), RealTimeActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        feedMultiple();

        Log.d(TAG, "##### RealTimeActivity - OK #####");

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void addEntry() {

        LineData data = realTime.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            float n = (float) ((Math.random() * 235f) + 15f);    // Between 15 - 250
            float f = vendor.addFormatDecimal(n);                // Format n to 1 Decimal

//            float n = vendor.addRandom(1, 0) + 0.2f * 0.3f;
//            float f = vendor.addFormatDecimal(n);

            data.addEntry(new Entry(set.getEntryCount(), f), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            realTime.notifyDataSetChanged();

            // limit the number of visible entries
            realTime.setVisibleXRangeMaximum(15);
            // realTime.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            realTime.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // realTime.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private Thread thread;

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i < 99999; i++) {

                    // Don't generate garbage runnables inside the loop.
                    runOnUiThread(runnable);

                    try {
                        Thread.sleep(1000); // refresh time
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, getString(R.string.chart_05_real_time_leg));
        set.setFormSize(20f);                    // Line Size
        set.setColor(Color.GREEN);               // Line Color
        set.setCircleRadius(4f);                 // Circle Size
        set.setCircleColor(Color.GREEN);         // Circle Color
        set.setCircleHoleRadius(3f);             // Circle Hole Size
        set.setCircleColorHole(Color.GREEN);     // Circle Hole Color
        set.setDrawCircleHole(false);            // Circle Hole Draw?
        set.setHighlightLineWidth(1.2f);         // HighLight Size
        set.setHighLightColor(Color.GREEN);      // HighLight Color
        set.setFillAlpha(50);                    // Fill Alpha
        set.setFillColor(Color.GREEN);           // Fill Color
        set.setDrawFilled(true);                 // Fill Enabled?
        set.setValueTextSize(11f);               // Text Size
        set.setValueTextColor(Color.BLACK);      // Text Color
        set.setDrawValues(true);                 // Text Enable?
        return set;
    }

    private void logoutUser() {
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
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
    }

}