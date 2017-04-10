package online.hydroflow.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import online.hydroflow.R;
import online.hydroflow.chart.ValueFormatter;
import online.hydroflow.chart.Vendor;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;

public class ChartActivity extends Activity {

    private static final String TAG = ChartActivity.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;

    private final Vendor vendor = new Vendor();
    private final String date = vendor.addDate();
    private boolean success;
    private int permission;

    private LineChart lineChart;
    private BarChart barChart;
    private PieChart pieChart;
    private BarChart NegativePositive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // xData for Bar and Pie
        final String[] xData = {
                getString(R.string.month_01), getString(R.string.month_02), getString(R.string.month_03), getString(R.string.month_04),
                getString(R.string.month_05), getString(R.string.month_06), getString(R.string.month_07), getString(R.string.month_08),
                getString(R.string.month_09), getString(R.string.month_10), getString(R.string.month_11), getString(R.string.month_12)};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // LineChart - START

        lineChart = (LineChart) findViewById(R.id.LineChart);

        // add Description
        Description d1 = new Description();              // Description Created
        d1.setText("Chart 01");                          // Description Text
        d1.setTextSize(14f);                             // Description Text Size
        d1.setEnabled(false);                            // Description Enabled?

        // add Legend
        Legend l1 = lineChart.getLegend();                                   // Create Legend
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

            Entry a = new Entry(i, vendor.addRandom(50, 180)); // Between 180.0 - 230.0
            Entry b = new Entry(i, vendor.addRandom(50, 180)); // Between 180.0 - 230.0

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
        set1.setCircleColorHole(Color.BLUE);   // Circle Hole Color
        set1.setDrawCircleHole(false);          // Circle Hole Draw?
        set1.setHighlightLineWidth(1.2f);       // HighLight Size
        set1.setHighLightColor(Color.BLUE);     // HighLight Color
        set1.setFillAlpha(30);                  // Fill Alpha
        set1.setFillColor(Color.CYAN);          // Fill Color
        set1.setDrawFilled(true);               // Fill Enabled?

        // add LineDataSet 2
        LineDataSet set2 = new LineDataSet(dataB, getString(R.string.chart_01_line_leg02));

        set2.setFormSize(20f);                  // Line Size
        set2.setColor(Color.RED);               // Line Color
        set2.setCircleRadius(3f);               // Circle Size
        set2.setCircleColor(Color.RED);         // Circle Color
        set2.setCircleHoleRadius(2f);           // Circle Hole Size
        set2.setCircleColorHole(Color.MAGENTA); // Circle Hole Color
        set2.setDrawCircleHole(false);          // Circle Hole Draw?
        set2.setHighlightLineWidth(1.2f);       // HighLight Size
        set2.setHighLightColor(Color.RED);      // HighLight Color
        set2.setFillAlpha(15);                  // Fill Alpha
        set2.setFillColor(Color.MAGENTA);       // Fill Color
        set2.setDrawFilled(true);               // Fill Enabled?

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

        XAxis x1 = lineChart.getXAxis();
        x1.setDrawAxisLine(false);            // X Axis Lines? (top and bottom)
        x1.setDrawGridLines(true);            // X Axis Grid Lines?
        x1.setTextColor(Color.BLACK);         // X Axis Value Text Color
        x1.setGridColor(Color.LTGRAY);        // X Axis Grid Color
        x1.setAxisLineColor(Color.LTGRAY);    // X Axis Line Color

        YAxis y1 = lineChart.getAxisLeft();
        y1.setGridColor(Color.TRANSPARENT);   // Y Axis Left - Grid Color (set just for one side and it will be apply for Left and Right)
        y1.setAxisLineColor(Color.LTGRAY);    // Y Axis Left - Line Color

        YAxis y11 = lineChart.getAxisRight();
        y11.setAxisLineColor(Color.LTGRAY);   // Y Axis Right - Line Color

        lineChart.setData(data1);                       // Add Data to Chart
        lineChart.setDescription(d1);                   // Add Description
        lineChart.setHighlightPerDragEnabled(false);    // HighLight to Drag Enabled?
        lineChart.setPinchZoom(false);                  // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setDoubleTapToZoomEnabled(false);     // Double Tap > Zoom Enalbed?
        lineChart.setTouchEnabled(true);                // Touch Enabled?
        lineChart.setScaleEnabled(true);                // Zoom Enabled?
        lineChart.setScaleYEnabled(false);              // Zoom Y Enable?
        lineChart.setHardwareAccelerationEnabled(true); // Hardware Accelaration?
        lineChart.animateX(2500);                       // Animation
        lineChart.invalidate();                         // Refresh

        // On Clicked Listner
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                // Center Chart View When On Clicked Value Value
                lineChart.centerViewToAnimated(e.getX(), e.getY(), lineChart.getData().getDataSetByIndex(h.getDataSetIndex())
                        .getAxisDependency(), 500);

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String yLITROS = String.valueOf(h.getY());

                // get X index -> String Day
                String xDIA = String.valueOf(pos);

                vendor.addToast(getString(R.string.day) + " " + xDIA + "\n" + yLITROS + " " + getString(R.string.liters), ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listner
        lineChart.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onLongClick(View view) {

                permission = vendor.addPermissions(ChartActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = lineChart.saveToPath(getString(R.string.chart) + " " + getString(R.string.chart_01_line_desc) + " " + date, "/DCIM/HydroFlow");
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

        barChart = (BarChart) findViewById(R.id.BarChart);

        // add Description
        Description d2 = new Description();     // Description Created
        d2.setText("Chart 02");                 // Description Text
        d2.setTextSize(14);                     // Description Text Size
        d2.setEnabled(false);                   // Description Enabled?

        // add Legend
        Legend l2 = barChart.getLegend();
        l2.setForm(Legend.LegendForm.SQUARE);                                // Legend Symbol Style
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);    // Legend Alignment
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);      // Legend Position
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);              // Legend Orientation
        l2.setTextSize(11f);                                                 // Legend Text Size
        l2.setDrawInside(false);                                             // Legend Inside?
        l2.setEnabled(true);                                                 // Legend Enabled?

        // Entries for the DataSet
        List<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(1f, 38.5f)); // 0 skipped to 1 be Jan and 12 Dec
//        entries.add(new BarEntry(2f, 43.4f));
//        entries.add(new BarEntry(3f, 59.2f));
//        entries.add(new BarEntry(4f, 63.9f));
//        entries.add(new BarEntry(5f, 58.4f));
//        entries.add(new BarEntry(6f, 61.1f));
//        entries.add(new BarEntry(7f, 57.7f));
//        entries.add(new BarEntry(8f, 52.4f));
//        entries.add(new BarEntry(9f, 57.5f));
//        entries.add(new BarEntry(10f, 61.3f));
//        entries.add(new BarEntry(11f, 53.8f));
//        entries.add(new BarEntry(12f, 46.6f));

        for (int i = 1; i < 13; i++) { // Months 1 to 12

            entries.add(new BarEntry(i, vendor.addRandom(3, 4)));  // Between 4.0 - 7.0

        }

        BarDataSet dataset = new BarDataSet(entries, getString(R.string.months));

//        dataset.setColors(ColorTemplate.MATERIAL_COLORS);   // Color of Data Set (months)

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

        // auto add Colors based on Color Template
//        for (int i = 0; i < 5; i++) {
//            int[] color = ColorTemplate.PASTEL_COLORS;
//            colors.add(color[i]);
//        }

        // add Colors to DataSet
        dataset.setColors(colors);

        BarData data2 = new BarData(dataset);

        data2.setValueFormatter(new ValueFormatter());   // Format 1 Decimal
        data2.setBarWidth(0.9f);                         // Bar Width (y)
        data2.setValueTextSize(10f);                     // Value Text Size (entries)
        data2.setValueTextColor(Color.BLACK);            // Value Text Color (entries)

        XAxis x2 = barChart.getXAxis();
        x2.setDrawAxisLine(false);            // X Axis Lines? (top and bottom)
        x2.setDrawGridLines(true);            // X Axis Grid Lines?
        x2.setTextColor(Color.BLACK);         // X Axis Value Text Color
        x2.setGridColor(Color.LTGRAY);        // X Axis Grid Color
        x2.setAxisLineColor(Color.LTGRAY);    // X Axis Line Color

        YAxis y2 = barChart.getAxisLeft();
        y2.setGridColor(Color.TRANSPARENT);   // Y Axis Left - Grid Color (set just for one side and it will be apply for Left and Right)
        y2.setAxisLineColor(Color.LTGRAY);    // Y Axis Left - Line Color

        YAxis y22 = barChart.getAxisRight();
        y22.setAxisLineColor(Color.LTGRAY);   // Y Axis Right - Line Color

        barChart.setData(data2);                        // Add Data to Chart
        barChart.setDescription(d2);                    // Add Description
        barChart.setDrawValueAboveBar(true);            // Values Above Bars?
        barChart.setFitBars(true);                      // Create Space Between Bars to Edge?
        barChart.setTouchEnabled(true);                 // Touch Enabled?
        barChart.setScaleEnabled(false);                // Zoom Enabled?
        barChart.setHardwareAccelerationEnabled(true);  // Hardware Accelaration?
        barChart.animateY(3250);                        // Animation
        barChart.invalidate();                          // Refresh

        // On Clicked Listner
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String Y = String.valueOf(h.getY());

                // index (1-12) and xData Array (0-11)
                String X = xData[pos - 1];

                vendor.addToast(X + "\n" + Y + " " + getString(R.string.cubic), ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listner
        barChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                permission = vendor.addPermissions(ChartActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = barChart.saveToPath(getString(R.string.chart) + " " + getString(R.string.chart_02_bar_desc) + " " + date, "/DCIM/HydroFlow");
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

        pieChart = (PieChart) findViewById(R.id.PieChart);

        // add Description
        Description d3 = new Description();             // Description Created
        d3.setText("Chart 03");                         // Description Text
        d3.setTextSize(14f);                            // Description Text Size
        d3.setEnabled(false);                           // Description Enabled?

        // add Legend
        Legend l3 = pieChart.getLegend();                                   // Legend Created
        l3.setForm(Legend.LegendForm.CIRCLE);                               // Legend Symbol Style
        l3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);  // Legend Alignment
        l3.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);        // Legend Position
        l3.setOrientation(Legend.LegendOrientation.VERTICAL);               // Legend Orientation
        l3.setTextSize(10f);                                                // Legend Text Size
        l3.setDrawInside(true);                                             // Legend Inside?
        l3.setEnabled(false);                                               // Legend Enabled?

        addDataSet(pieChart);   // Add Data and Dataset

        pieChart.setDescription(d3);                   // Add Description
        pieChart.setRotationEnabled(true);             // Chart Rotation Enabled?
        pieChart.setUsePercentValues(true);            // Data Calculeted in %?
        pieChart.setHoleColor(Color.TRANSPARENT);      // Hole Color
        pieChart.setHoleRadius(30f);                   // Hole Size
        pieChart.setCenterText(getString(R.string.chart_03_pie_leg));          // Center Text
        pieChart.setCenterTextSize(14f);               // Center Text Size
        pieChart.setCenterTextColor(Color.BLACK);      // Center Text Color
        pieChart.setDrawCenterText(true);              // Center Text Enable?
        pieChart.setTransparentCircleRadius(50f);      // Trans. Circle Size
        pieChart.setTransparentCircleAlpha(175);       // Trans. Circle Alpha
        pieChart.setEntryLabelTextSize(11f);           // Entries Text Size
        pieChart.setEntryLabelColor(Color.BLACK);      // Entries Text Color (x)
        pieChart.setDrawEntryLabels(true);             // Entries Enabled?
        pieChart.setTouchEnabled(true);                // Touch Enabled?
        pieChart.setHardwareAccelerationEnabled(true); // Hardware Accelaration?
        pieChart.animateY(4000);                       // Animation
        pieChart.invalidate();                         // Refresh

        // On Clicked Listner
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String Y = String.valueOf(h.getY());

                // index (0-11) and xData Array (0-11)
                String X = xData[pos];
                vendor.addToast(X + "\n" + Y + " " + getString(R.string.cubic), ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listner
        pieChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                permission = vendor.addPermissions(ChartActivity.this);

                if (permission == 0) {
                    success = vendor.addFolder();
                    if (success) {
                        boolean save = pieChart.saveToPath(getString(R.string.chart) + " " + getString(R.string.chart_03_pie_desc) + " " + date, "/DCIM/HydroFlow");
                        if (save) {
                            vendor.addToast(getString(R.string.chart) + " 3\n" + getString(R.string.img_saved), ChartActivity.this);
                        } else {
                            vendor.addToast(getString(R.string.img_error_to_save), ChartActivity.this);
                        }
                    }
                }
                return success;
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
        NegativePositive.setExtraTopOffset(-30f);              // Ajusts
        NegativePositive.setExtraBottomOffset(10f);            // Ajusts
        NegativePositive.setExtraLeftOffset(70f);              // Ajusts
        NegativePositive.setExtraRightOffset(70f);             // Ajusts
        NegativePositive.setFitBars(true);                     // Create Space Between Bars to Edge?
        NegativePositive.setDrawBarShadow(false);              // Shadow Inside Chart?
        NegativePositive.setDrawValueAboveBar(true);           // Y Values Above Bars?
        NegativePositive.getAxisRight().setEnabled(true);      // Y Axis on Right Border?
        NegativePositive.setDrawGridBackground(false);         // BG Only Inside Chart??
        NegativePositive.setTouchEnabled(true);                // Touch Enabled?
        NegativePositive.setScaleEnabled(false);               // Zoom Enabled?
        NegativePositive.setHardwareAccelerationEnabled(true); // Hardware Accelaration?
        NegativePositive.animateY(5000);                       // Animation
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

            float n = vendor.addRandom(2, 0) + vendor.addRandom(2, -2) + 0.3f;
            float f = vendor.addFormatDecimal(n);

            data4.add(new Data(i, f));
        }

        setData(data4, NegativePositive);

        // On Clicked Listner
        NegativePositive.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

//                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String Y = String.valueOf(h.getY());

                // index (1-12) and xData Array (0-11)
                String X = xData[pos - 1];
                vendor.addToast(X + "\n" + Y + " " + getString(R.string.cubic), ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // On LongPress Listner
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

//        int green = Color.rgb(110, 190, 102);
//        int red = Color.rgb(211, 74, 88);

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

//        final float[] yData = {38.5f, 43.4f, 59.2f, 63.9f, 58.4f, 61.1f, 57.7f, 52.4f, 57.5f, 61.3f, 53.8f, 46.6f};

        final String[] xData = {
                getString(R.string.month_01), getString(R.string.month_02), getString(R.string.month_03), getString(R.string.month_04),
                getString(R.string.month_05), getString(R.string.month_06), getString(R.string.month_07), getString(R.string.month_08),
                getString(R.string.month_09), getString(R.string.month_10), getString(R.string.month_11), getString(R.string.month_12)};

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            entries.add(new PieEntry(vendor.addRandom(3, 4), xData[i]));  // Between 4.0 - 7.0
//            entries.add(new PieEntry(yData[i], xData[i]));
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
            colors.add(color[i]); // "i" has to match with max index of color template choosed

            // Using Random
//            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));    // Random RGB Create
//            colors.add(color);                                                                    // Random RGB Add
        }

//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);  // Array of Colors to auto fullfill

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

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
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

}
