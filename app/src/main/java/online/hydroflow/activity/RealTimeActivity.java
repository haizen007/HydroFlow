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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import online.hydroflow.R;
import online.hydroflow.chart.ValueFormatter;
import online.hydroflow.chart.Vendor;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;

public class RealTimeActivity extends Activity {

    private static final String TAG = ChartActivity.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;

    private final Vendor vendor = new Vendor();

    private static final Random r = new Random();

    private static final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);   // Locale to USA for "." Decimal (unique pattern accepted for "Float")
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#", symbols);              // Format to 1 Decimal using Locale USA -> Float Happy!

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


        final LineChart realTime = (LineChart) findViewById(R.id.RealTimeChart);

        // add Description
        Description d1 = new Description();              // Description Created
        d1.setText("Chart Real Time");                   // Description Text
        d1.setTextSize(14f);                             // Description Text Size
        d1.setEnabled(false);                            // Description Enabled?

        // add Legend
        Legend l = realTime.getLegend();                                   // Create Legend
        l.setForm(Legend.LegendForm.LINE);                                  // Legend Symbol Style
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);  // Legend Alignment
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);      // Legend Position
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);              // Legend Orientation
        l.setTextSize(14f);                                                 // Legend Text Size
        l.setDrawInside(false);                                             // Legend Inside?
        l.setEnabled(true);                                                 // Legend Enabled?

        // add Data
        List<Entry> dataA = new ArrayList<>();

        for (int i = 1; i < 31; i++) { // Day 1 to 30

            float n1 = r.nextFloat() + (r.nextInt(51) + 180); // Between 180.0 - 231.0

            float f1 = Float.valueOf(decimalFormat.format(n1));  // Format n to 1 Decimal

            Entry a = new Entry(i, f1);

            dataA.add(a);
        }

        // add LineDataSet 1
        LineDataSet set = new LineDataSet(dataA, getString(R.string.chart_05_real_time_leg));

        set.setFormSize(20f);                    // Line Size
        set.setColor(Color.GREEN);               // Line Color
        set.setCircleRadius(3f);                 // Circle Size
        set.setCircleColor(Color.GREEN);         // Circle Color
        set.setCircleHoleRadius(2f);             // Circle Hole Size
        set.setCircleColorHole(Color.GREEN);     // Circle Hole Color
        set.setDrawCircleHole(false);            // Circle Hole Draw?
        set.setHighlightLineWidth(1.2f);         // HighLight Size
        set.setHighLightColor(Color.GREEN);      // HighLight Color
        set.setFillAlpha(30);                    // Fill Alpha
        set.setFillColor(Color.GREEN);           // Fill Color
        set.setDrawFilled(true);                 // Fill Enabled?

        // add LineDataSet 1 and 2 to lineDataSets
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(set);

        // add Data
        LineData data1 = new LineData(lineDataSets);

        data1.setValueFormatter(new ValueFormatter()); // Format 1 Decimal
        data1.setValueTextSize(7);                     // Value Text Size
        data1.setValueTextColor(Color.BLACK);          // Value Text Color
        data1.setHighlightEnabled(true);               // HighLight Enabled?
        data1.setDrawValues(false);                    // Values Enable?

        XAxis x = realTime.getXAxis();
        x.setDrawAxisLine(false);            // X Axis Lines? (top and bottom)
        x.setDrawGridLines(false);           // X Axis Grid Lines?
        x.setTextColor(Color.BLACK);         // X Axis Value Text Color
        x.setGridColor(Color.LTGRAY);        // X Axis Grid Color
        x.setAxisLineColor(Color.LTGRAY);    // X Axis Line Color
        x.setAvoidFirstLastClipping(true);
        x.setEnabled(false);

        YAxis leftAxis = realTime.getAxisLeft();
        leftAxis.setGridColor(Color.TRANSPARENT);   // Y Axis Left - Grid Color (set just for one side and it will be apply for Left and Right)
        leftAxis.setAxisLineColor(Color.LTGRAY);    // Y Axis Left - Line Color
        leftAxis.setEnabled(true);

        YAxis rightAxis = realTime.getAxisRight();
        rightAxis.setAxisLineColor(Color.LTGRAY);   // Y Axis Right - Line Color
        leftAxis.setEnabled(true);

        realTime.setData(data1);                       // Add Data to Chart
        realTime.setDescription(d1);                   // Add Description
        realTime.setHighlightPerDragEnabled(false);    // HighLight to Drag Enabled?
        realTime.setPinchZoom(false);                  // if disabled, scaling can be done on x- and y-axis separately
        realTime.setDoubleTapToZoomEnabled(false);     // Double Tap > Zoom Enalbed?
        realTime.setTouchEnabled(true);                // Touch Enabled?
        realTime.setScaleEnabled(true);                // Zoom Enabled?
        realTime.setHardwareAccelerationEnabled(true); // Hardware Accelaration?
        realTime.animateX(2500);                       // Animation
        realTime.invalidate();                         // Refresh

        // On Clicked Listner
        realTime.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                // Center Chart View When On Clicked Value Value
                realTime.centerViewToAnimated(e.getX(), e.getY(), realTime.getData().getDataSetByIndex(h.getDataSetIndex())
                        .getAxisDependency(), 500);

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String yLITROS = String.valueOf(h.getY());

                // get X index -> String Day
                String xDIA = String.valueOf(pos);

                vendor.addToast(getString(R.string.day) + " " + xDIA + "\n" + yLITROS + " " + getString(R.string.liters), RealTimeActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });


        Log.d(TAG, "##### RealTimeActivity - OK #####");

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

}