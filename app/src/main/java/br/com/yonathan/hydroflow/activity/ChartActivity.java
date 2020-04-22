package br.com.yonathan.hydroflow.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import br.com.yonathan.hydroflow.R;
import br.com.yonathan.hydroflow.utils.ValueFormatter;
import br.com.yonathan.hydroflow.utils.Vendor;
import br.com.yonathan.hydroflow.sql.SQLiteHandler;
import br.com.yonathan.hydroflow.sql.SessionManager;

public class ChartActivity extends Activity {

    private static final String TAG = ChartActivity.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;

    private final Vendor vendor = new Vendor();
    private final String date = vendor.addDate();
    private boolean success;
    private int permission;

    private LineChart LineChart;
    private BarChart BarChart;
    private PieChart PieChart;
    private BarChart NegativePositive;

    private final float months[] = new float[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fill the Months Array with random data for BarChart and PieChart
        fillMonths();

        // xData for Bar and Pie
        final String[] xData = {
                getString(R.string.month_01), getString(R.string.month_02), getString(R.string.month_03), getString(R.string.month_04),
                getString(R.string.month_05), getString(R.string.month_06), getString(R.string.month_07), getString(R.string.month_08),
                getString(R.string.month_09), getString(R.string.month_10), getString(R.string.month_11), getString(R.string.month_12)};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // LineChart - START

        LineChart = (LineChart) findViewById(R.id.LineChart);

        // add Description
        Description d1 = new Description();              // Description Created
        d1.setText("Chart 01");                          // Description Text
        d1.setTextSize(14f);                             // Description Text Size
        d1.setEnabled(false);                            // Description Enabled?

        // add Legend
        Legend l1 = LineChart.getLegend();                                   // Create Legend
        l1.setForm(Legend.LegendForm.LINE);                                  // Legend Symbol Style
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);  // Legend Alignment
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);      // Legend Position
        l1.setOrientation(Legend.LegendOrientation.HORIZONTAL);              // Legend Orientation
        l1.setTextSize(14f);                                                 // Legend Text Size
        l1.setDrawInside(false);                                             // Legend Inside?
        l1.setEnabled(true);                                                 // Legend Enabled?

        // add Data X and Y
        List<Entry> dataA = new ArrayList<>();
        List<Entry> dataB = new ArrayList<>();

        for (int i = 1; i < 31; i++) { // Day 1 to 30

            Entry a = new Entry(i, vendor.addRandom(50, 180)); // Between 180.0 to 230.0
            Entry b = new Entry(i, vendor.addRandom(50, 180)); // Between 180.0 to 230.0

            dataA.add(a);
            dataB.add(b);
        }

        // add LineDataSet 1
        LineDataSet set1 = new LineDataSet(dataA, getString(R.string.chart_01_line_leg01));

        set1.setFormSize(20f);                  // Line Size
        set1.setColor(Color.BLUE);              // Line Color
        set1.setCircleRadius(3f);               // Circle Size
        set1.setCircleColor(Color.BLUE);        // Circle Color
        set1.setCircleHoleRadius(2f);           // Circle Hole Size
        set1.setCircleColorHole(Color.BLUE);    // Circle Hole Color
        set1.setDrawCircleHole(false);          // Circle Hole Draw?
        set1.setHighlightLineWidth(1.2f);       // HighLight Size
        set1.setHighLightColor(Color.BLUE);     // HighLight Color
        set1.setFillAlpha(40);                  // Fill Alpha
        set1.setFillColor(Color.CYAN);          // Fill Color
        set1.setDrawFilled(true);               // Fill Enabled?

        // add LineDataSet 2
        LineDataSet set2 = new LineDataSet(dataB, getString(R.string.chart_01_line_leg02));

        set2.setFormSize(20f);                                          // Line Size
        set2.setColor(Color.parseColor("#AA00ff"));           // Line Color
        set2.setCircleRadius(3f);                                       // Circle Size
        set2.setCircleColor(Color.parseColor("#AA00ff"));     // Circle Color
        set2.setCircleHoleRadius(2f);                                   // Circle Hole Size
        set2.setCircleColorHole(Color.parseColor("#AA00ff")); // Circle Hole Color
        set2.setDrawCircleHole(false);                                  // Circle Hole Draw?
        set2.setHighlightLineWidth(1.2f);                               // HighLight Size
        set2.setHighLightColor(Color.parseColor("#AA00ff"));  // HighLight Color
        set2.setFillAlpha(20);                                          // Fill Alpha
        set2.setFillColor(Color.parseColor("#AA00ff"));       // Fill Color
        set2.setDrawFilled(true);                                       // Fill Enabled?

        // add LineDataSet 1 and 2 to lineDataSets
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(set1);
        lineDataSets.add(set2);

        // add Data
        LineData data1 = new LineData(lineDataSets);

        data1.setValueFormatter(new ValueFormatter()); // Format 1 Decimal
        data1.setValueTextSize(7);                     // Value Text Size
        data1.setValueTextColor(Color.BLACK);          // Value Text Color
        data1.setHighlightEnabled(true);               // HighLight Enabled?
        data1.setDrawValues(false);                    // Values Enable?

        XAxis x1 = LineChart.getXAxis();
        x1.setDrawAxisLine(false);            // X Axis Lines? (top and bottom)
        x1.setDrawGridLines(true);            // X Axis Grid Lines?
        x1.setTextColor(Color.BLACK);         // X Axis Value Text Color
        x1.setGridColor(Color.LTGRAY);        // X Axis Grid Color
        x1.setAxisLineColor(Color.LTGRAY);    // X Axis Line Color

        YAxis y1 = LineChart.getAxisLeft();
        y1.setGridColor(Color.TRANSPARENT);   // Y Axis Left - Grid Color (set just for one side and it will be apply for Left and Right)
        y1.setAxisLineColor(Color.LTGRAY);    // Y Axis Left - Line Color

        YAxis y11 = LineChart.getAxisRight();
        y11.setAxisLineColor(Color.LTGRAY);   // Y Axis Right - Line Color

        LineChart.setData(data1);                       // Add Data to Chart
        LineChart.setDescription(d1);                   // Add Description
        LineChart.setHighlightPerDragEnabled(false);    // HighLight to Drag Enabled?
        LineChart.setPinchZoom(false);                  // if disabled, scaling can be done on x- and y-axis separately
        LineChart.setDoubleTapToZoomEnabled(false);     // Double Tap > Zoom Enabled?
        LineChart.setTouchEnabled(true);                // Touch Enabled?
        LineChart.setScaleEnabled(true);                // Zoom Enabled?
        LineChart.setScaleYEnabled(false);              // Zoom Y Enable?
        LineChart.setHardwareAccelerationEnabled(true); // Hardware Acceleration?
        LineChart.animateX(2500);           // Animation
        LineChart.invalidate();                         // Refresh

        // On Clicked Listener
        LineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                // Center Chart View When On Clicked Value Value
                LineChart.centerViewToAnimated(e.getX(), e.getY(), LineChart.getData().getDataSetByIndex(h.getDataSetIndex())
                        .getAxisDependency(), 500);

//                Log.d(TAG, "##### " + e.toString() + " #####");
//                Log.d(TAG, "\n##### " + h.toString() + " #####");

                // get the dataSet 0 or 1
                int xDataSet = h.getDataSetIndex();

                if (xDataSet == 0) { // 1 to 30 days
                    vendor.addToast(getString(R.string.day) + " " + (int) h.getX() + "\n" + h.getY() + " " + getString(R.string.liters), ChartActivity.this);
                } else { //  31 to 60 days
                    vendor.addToast(getString(R.string.day) + " " + ((int) h.getX() + 30) + "\n" + h.getY() + " " + getString(R.string.liters), ChartActivity.this);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listener
        LineChart.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onLongClick(View view) {

                permission = vendor.addPermissions(ChartActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = LineChart.saveToPath(getString(R.string.chart) + " " + getString(R.string.chart_01_line_desc) + " " + date, "/DCIM/HydroFlow");
                        if (save) {
                            vendor.addToast(getString(R.string.chart) + " 1\n" + getString(R.string.img_saved), ChartActivity.this);
                        } else {
                            vendor.addToast(getString(R.string.img_error_to_save), ChartActivity.this);
                        }
                    }
                }
                return success;
            }
        });

        // LineChart - END

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // BarChart - START

        BarChart = (BarChart) findViewById(R.id.BarChart);

        // add Description
        Description d2 = new Description();     // Description Created
        d2.setText("Chart 02");                 // Description Text
        d2.setTextSize(14);                     // Description Text Size
        d2.setEnabled(false);                   // Description Enabled?

        // add Legend
        Legend l2 = BarChart.getLegend();
        l2.setForm(Legend.LegendForm.SQUARE);                                // Legend Symbol Style
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);    // Legend Alignment
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);      // Legend Position
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);              // Legend Orientation
        l2.setTextSize(11f);                                                 // Legend Text Size
        l2.setDrawInside(false);                                             // Legend Inside?
        l2.setEnabled(true);                                                 // Legend Enabled?

        // Entries for the DataSet
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++) { // Months 1 to 12
            entries.add(new BarEntry(i + 1, months[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.months));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#F44336"));
        colors.add(Color.parseColor("#E91E63"));
        colors.add(Color.parseColor("#9C27B0"));
        colors.add(Color.parseColor("#673AB7"));
        colors.add(Color.parseColor("#3F51B5"));
        colors.add(Color.parseColor("#2196F3"));
        colors.add(Color.parseColor("#00BCD4"));
        colors.add(Color.parseColor("#009688"));
        colors.add(Color.parseColor("#4CAF50"));
        colors.add(Color.parseColor("#8BC34A"));
        colors.add(Color.parseColor("#FFC107"));
        colors.add(Color.parseColor("#FF9800"));

        // add Colors to DataSet
        dataSet.setColors(colors);

        BarData data2 = new BarData(dataSet);

        data2.setValueFormatter(new ValueFormatter());   // Format 1 Decimal
        data2.setBarWidth(0.9f);                         // Bar Width (y)
        data2.setValueTextSize(10f);                     // Value Text Size (entries)
        data2.setValueTextColor(Color.BLACK);            // Value Text Color (entries)

        XAxis x2 = BarChart.getXAxis();
        x2.setDrawAxisLine(false);            // X Axis Lines? (top and bottom)
        x2.setDrawGridLines(true);            // X Axis Grid Lines?
        x2.setTextColor(Color.BLACK);         // X Axis Value Text Color
        x2.setGridColor(Color.LTGRAY);        // X Axis Grid Color
        x2.setAxisLineColor(Color.LTGRAY);    // X Axis Line Color

        YAxis y2 = BarChart.getAxisLeft();
        y2.setGridColor(Color.TRANSPARENT);   // Y Axis Left - Grid Color (set just for one side and it will be apply for Left and Right)
        y2.setAxisLineColor(Color.LTGRAY);    // Y Axis Left - Line Color

        YAxis y22 = BarChart.getAxisRight();
        y22.setAxisLineColor(Color.LTGRAY);   // Y Axis Right - Line Color

        BarChart.setData(data2);                        // Add Data to Chart
        BarChart.setDescription(d2);                    // Add Description
        BarChart.setDrawValueAboveBar(true);            // Values Above Bars?
        BarChart.setFitBars(true);                      // Create Space Between Bars to Edge?
        BarChart.setTouchEnabled(true);                 // Touch Enabled?
        BarChart.setScaleEnabled(false);                // Zoom Enabled?
        BarChart.setHardwareAccelerationEnabled(true);  // Hardware Acceleration?
        BarChart.animateY(3250);            // Animation
        BarChart.invalidate();                          // Refresh

        // On Clicked Listener
        BarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // xData [months] pos (1 to 12) - 1, to be (0 to 11)
                vendor.addToast(xData[((int) h.getX() - 1)] + "\n" + h.getY() + " " + getString(R.string.cubic), ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listener
        BarChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                permission = vendor.addPermissions(ChartActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = BarChart.saveToPath(getString(R.string.chart) + " " + getString(R.string.chart_02_bar_desc) + " " + date, "/DCIM/HydroFlow");
                        if (save) {
                            vendor.addToast(getString(R.string.chart) + " 2\n" + getString(R.string.img_saved), ChartActivity.this);
                        } else {
                            vendor.addToast(getString(R.string.img_error_to_save), ChartActivity.this);
                        }
                    }
                }
                return success;
            }
        });

        // BarChart - END

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // PieChart - START

        PieChart = (PieChart) findViewById(R.id.PieChart);

        // add Description
        Description d3 = new Description();             // Description Created
        d3.setText("Chart 03");                         // Description Text
        d3.setTextSize(14f);                            // Description Text Size
        d3.setEnabled(false);                           // Description Enabled?

        // add Legend
        Legend l3 = PieChart.getLegend();                                   // Legend Created
        l3.setForm(Legend.LegendForm.CIRCLE);                               // Legend Symbol Style
        l3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);  // Legend Alignment
        l3.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);        // Legend Position
        l3.setOrientation(Legend.LegendOrientation.VERTICAL);               // Legend Orientation
        l3.setTextSize(10f);                                                // Legend Text Size
        l3.setDrawInside(true);                                             // Legend Inside?
        l3.setEnabled(false);                                               // Legend Enabled?

        addDataSet(PieChart);   // Add Data and DataSet

        PieChart.setDescription(d3);                   // Add Description
        PieChart.setRotationEnabled(true);             // Chart Rotation Enabled?
        PieChart.setUsePercentValues(true);            // Data Calculated in %?
        PieChart.setHoleColor(Color.TRANSPARENT);      // Hole Color
        PieChart.setHoleRadius(30f);                   // Hole Size
        PieChart.setCenterText(getString(R.string.chart_03_pie_leg));         // Center Text
        PieChart.setCenterTextSize(14f);               // Center Text Size
        PieChart.setCenterTextColor(Color.BLACK);      // Center Text Color
        PieChart.setDrawCenterText(true);              // Center Text Enable?
        PieChart.setTransparentCircleRadius(50f);      // Trans. Circle Size
        PieChart.setTransparentCircleAlpha(175);       // Trans. Circle Alpha
        PieChart.setEntryLabelTextSize(11f);           // Entries Text Size
        PieChart.setEntryLabelColor(Color.BLACK);      // Entries Text Color (x)
        PieChart.setDrawEntryLabels(true);             // Entries Enabled?
        PieChart.setTouchEnabled(true);                // Touch Enabled?
        PieChart.setHardwareAccelerationEnabled(true); // Hardware Acceleration?
        PieChart.animateY(4000);           // Animation
        PieChart.invalidate();                         // Refresh

        // On Clicked Listener
        PieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // xData [months]
                vendor.addToast(xData[(int) h.getX()] + "\n" + h.getY() + " " + getString(R.string.cubic), ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // PieChart Listener for LongPress
        PieChart.setOnChartGestureListener(new OnChartGestureListener() {

            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                permission = vendor.addPermissions(ChartActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = PieChart.saveToPath(getString(R.string.chart) + " " + getString(R.string.chart_03_pie_desc) + " " + date, "/DCIM/HydroFlow");
                        if (save) {
                            vendor.addToast(getString(R.string.chart) + " 3\n" + getString(R.string.img_saved), ChartActivity.this);
                        } else {
                            vendor.addToast(getString(R.string.img_error_to_save), ChartActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

        // PieChart - END

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // BarChartPositiveNegative - START

        NegativePositive = (BarChart) findViewById(R.id.NegativePositive);

        // add Description
        Description d4 = new Description();      // Description Created
        d4.setText("Chart 04");                  // Description Text
        d4.setTextSize(12f);                     // Description Text Size
        d4.setEnabled(false);                    // Description Enabled?

        // add Legend
        Legend l4 = NegativePositive.getLegend();                           // Legend Created
        l4.setForm(Legend.LegendForm.DEFAULT);                              // Legend Symbol Style
        l4.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Legend Alignment
        l4.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);     // Legend Position
        l4.setOrientation(Legend.LegendOrientation.HORIZONTAL);             // Legend Orientation
        l4.setTextSize(10f);                                                // Legend Text Size
        l4.setDrawInside(false);                                            // Legend Inside?
        l4.setEnabled(true);                                                // Legend Enabled?

        NegativePositive.setDescription(d4);                   // Add Description
        NegativePositive.setExtraTopOffset(-30f);              // Adjusts
        NegativePositive.setExtraBottomOffset(10f);            // Adjusts
        NegativePositive.setExtraLeftOffset(70f);              // Adjusts
        NegativePositive.setExtraRightOffset(70f);             // Adjusts
        NegativePositive.setFitBars(true);                     // Create Space Between Bars to Edge?
        NegativePositive.setDrawBarShadow(false);              // Shadow Inside Chart?
        NegativePositive.setDrawValueAboveBar(true);           // Y Values Above Bars?
        NegativePositive.getAxisRight().setEnabled(true);      // Y Axis on Right Border?
        NegativePositive.setDrawGridBackground(false);         // BG Only Inside Chart??
        NegativePositive.setTouchEnabled(true);                // Touch Enabled?
        NegativePositive.setScaleEnabled(false);               // Zoom Enabled?
        NegativePositive.setHardwareAccelerationEnabled(true); // Hardware Acceleration?
        NegativePositive.animateY(5000);           // Animation
        NegativePositive.invalidate();                         // Refresh

        XAxis x4 = NegativePositive.getXAxis();
        x4.setPosition(XAxis.XAxisPosition.BOTH_SIDED); // X Axis Position (both sides)
        x4.setTextSize(10f);                            // X Axis Text Size
        x4.setTextColor(Color.BLACK);                   // X Axis Text Color
        x4.setDrawAxisLine(false);                      // X Axis Lines? (top and bottom)
        x4.setDrawGridLines(true);                      // X Axis Grid Lines?
        x4.setGridColor(Color.LTGRAY);                  // X Axis Left - Grid Color
        x4.setAxisLineColor(Color.LTGRAY);              // X Axis Left - Line Color
        x4.setLabelCount(6);                            // X Axis Qtd to Show
        x4.setCenterAxisLabels(false);                  // X Axis Center?
        x4.setGranularity(1f);                          // X Axis Granularity
        x4.setEnabled(true);                            // X Axis Enabled?

        YAxis y4 = NegativePositive.getAxisLeft();
        y4.setDrawLabels(true);                  // Y Axis Left - Values Enabled? (left side)
        y4.setSpaceTop(11f);                     // Y Axis Left - Distance from Top
        y4.setSpaceBottom(11f);                  // Y Axis Left - Distance from Bottom
        y4.setGridColor(Color.LTGRAY);           // Y Axis Left - Grid Color
        y4.setAxisLineColor(Color.LTGRAY);       // Y Axis Left - Line Color
        y4.setDrawAxisLine(true);                // Y Axis Left - Line? (left side)
        y4.setDrawGridLines(false);              // Y Axis Left - Grid Lines? (it makes stronger)
        y4.setZeroLineWidth(0.7f);               // Y Axis Left - Zero Line Size
        y4.setZeroLineColor(Color.BLACK);        // Y Axis Left - Zero Line Color
        y4.setDrawZeroLine(true);                // Y Axis Left - Zero Line Enabled?

        YAxis y44 = NegativePositive.getAxisRight();
        y44.setAxisLineColor(Color.LTGRAY);      // Y Axis Right - Line Color

        final List<Data> data4 = new ArrayList<>();

        for (int i = 1; i < 13; i++) {

            float n = vendor.addRandom(2, 0) - vendor.addRandom(2, 0) + 0.3f;
            float f = vendor.addFormatDecimal(n);

            data4.add(new Data(i, f));
        }

        setData(data4, NegativePositive);

        // On Clicked Listener
        NegativePositive.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // xData [months] pos (1 to 12) - 1, to be (0 to 11)
                vendor.addToast(xData[((int) h.getX() - 1)] + "\n" + h.getY() + " " + getString(R.string.cubic), ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listener
        NegativePositive.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                permission = vendor.addPermissions(ChartActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = NegativePositive.saveToPath(getString(R.string.chart) + " " + getString(R.string.chart_04_neg_pos_desc) + " " + date, "/DCIM/HydroFlow");
                        if (save) {
                            vendor.addToast(getString(R.string.chart) + " 4\n" + getString(R.string.img_saved), ChartActivity.this);
                        } else {
                            vendor.addToast(getString(R.string.img_error_to_save), ChartActivity.this);
                        }
                    }
                }
                return success;
            }
        });

        // BarChartPositiveNegative - END

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Log.d(TAG, "##### ChartActivity - OK #####");

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setData(List<Data> dataList, BarChart NegPos) {

        ArrayList<BarEntry> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        int red = Color.parseColor("#EF5350");
        int green = Color.parseColor("#66BB6A");

        for (int i = 0; i < dataList.size(); i++) {

            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0)
                colors.add(red);
            else
                colors.add(green);
        }

        BarDataSet set;

        if (NegPos.getData() != null &&
                NegPos.getData().getDataSetCount() > 0) {
            set = (BarDataSet) NegPos.getData().getDataSetByIndex(0);
            set.setValues(values);
            NegPos.getData().notifyDataChanged();
            NegPos.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, getString(R.string.months));
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData data = new BarData(set);
            data.setValueFormatter(new ValueFormatter()); // Format to 1 Decimal
            data.setValueTextSize(10f);                   // Value Text Size
            data.setBarWidth(0.9f);                       // Bar Width (y)

            NegPos.setData(data);    // Add Data to Chart
            NegPos.invalidate();     // Refresh
        }
    }

    private class Data {

        private final float yValue;
        private final float xValue;

        private Data(float xValue, float yValue) {
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void addDataSet(PieChart pieChart) {

        final String[] xData = {
                getString(R.string.month_01), getString(R.string.month_02), getString(R.string.month_03), getString(R.string.month_04),
                getString(R.string.month_05), getString(R.string.month_06), getString(R.string.month_07), getString(R.string.month_08),
                getString(R.string.month_09), getString(R.string.month_10), getString(R.string.month_11), getString(R.string.month_12)};

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            entries.add(new PieEntry(months[i], xData[i]));  // Between 4.0 to 7.0
        }

        // create DataSet
        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.months));
        dataSet.setValueFormatter(new ValueFormatter());                    // Format 1 Decimal
        dataSet.setSelectionShift(12f);                                     // Boundary Distance
        dataSet.setSliceSpace(3f);                                          // Slice Space
        dataSet.setValueTextSize(12f);                                      // Value Text Size
        dataSet.setValueTextColor(Color.BLACK);                             // Value Text Color
        dataSet.setValueFormatter(new PercentFormatter());                  // Value Text Add %
        dataSet.setValueLinePart1OffsetPercentage(80f);                     // Value Text Line Distance from Center
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);  // Value Text Position

//        Random rnd = new Random();  // Random Created
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            // Using Index of Color Template
            int[] color = ColorTemplate.VORDIPLOM_COLORS;
            colors.add(color[i]); // "i" has to match with max index of chosen template color
        }

        // add Colors to DataSet
        dataSet.setColors(colors);

        // create Data
        PieData data3 = new PieData(dataSet);    // Add DataSet to Data

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            data3.setValueTextSize(12f);         // Value Text Size  (y)
        } else {
            data3.setValueTextSize(16f);         // Value Text Size  (y)
        }
        pieChart.setData(data3);                 // Add Data to Chart
    }

    private void fillMonths() {
        for (int i = 0; i < 12; i++) {
            float x = vendor.addRandom(3, 4); // Between 4.0 to 7.0
            months[i] = x;
        }
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
        vendor.addIntent(ChartActivity.this, LoginActivity.class);
    }

    @Override
    public void onBackPressed() {
        vendor.addIntent(ChartActivity.this, MainActivity.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
