package online.hydroflow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import online.hydroflow.R;
import online.hydroflow.app.AppVendor;
import online.hydroflow.helper.SQLiteHandler;
import online.hydroflow.helper.SessionManager;

public class ChartActivity extends FragmentActivity implements OnChartValueSelectedListener {

    // X and Y for PieChart
    private final float[] yData = {50.3f, 58.5f, 65.1f, 45.8f, 61.7f, 72.2f, 63.8f, 55.9f, 68.2f, 53.4f, 48.7f, 59.6f};

    //    private final String[] xData = vendor.addMonths();

    //    private final String[] xData = {txt};
    private final static String[] xData = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    private static final String TAG = ChartActivity.class.getSimpleName();
    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;
    private LineChart lineChart;
    private PieChart pieChart;
    private BarChart barChart;
    private TextView tv_LineChart;
    private TextView tv_BarChart;
    private TextView tv_PieChart;

    AppVendor vendor = new AppVendor();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        btnLogout = (Button) findViewById(R.id.btnLogout);

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

        tv_LineChart = (TextView) findViewById(R.id.tv_LineChart);
        tv_BarChart = (TextView) findViewById(R.id.tv_BarChart);
        tv_PieChart = (TextView) findViewById(R.id.tv_PieChart);

        tv_LineChart.setText("Comparação de Consumo em Litros");
        tv_BarChart.setText("Consumo por Mês em Litros");
        tv_PieChart.setText("Clique no Gráfico para exibir o Mês");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // LineChart - START

        lineChart = (LineChart) findViewById(R.id.lineChart);

        Description d1 = new Description();
        d1.setText("Comparação de Consumo entre Períodos");
        d1.setTextSize(14);
        d1.setEnabled(false);
        lineChart.setDescription(d1);

        // add legend to chart
        Legend l = lineChart.getLegend();
        l.setTextSize(14);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        // add data
        List<Entry> dataA = new ArrayList<>();
        List<Entry> dataB = new ArrayList<>();

        Random r = new Random();

        for (int i = 1; i < 31; i++) {

            float n1 = r.nextInt(33) + 38; // between 38-70
            float n2 = r.nextInt(33) + 38; // between 38-70

            Entry a = new Entry(i, n1);
            Entry b = new Entry(i, n2);
            dataA.add(a);
            dataB.add(b);

        }

        LineDataSet set1 = new LineDataSet(dataA, "De 0-30 dias");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.BLUE);

        LineDataSet set2 = new LineDataSet(dataB, "De 30-60 dias");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.RED);

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(set1);
        lineDataSets.add(set2);

        LineData data = new LineData(lineDataSets);
        lineChart.setData(data);
        lineChart.animateX(2500);
        lineChart.invalidate(); // refresh

        // when value is selected
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String yLITROS = String.valueOf(h.getY());

                // index (1-12) and xData Array (0-11)
                String xDIA = String.valueOf(pos);

                vendor.addToast("Dia " + xDIA + "\n" + yLITROS + " Litros", ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // LineChart - END

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // BarChart - START

        barChart = (BarChart) findViewById(R.id.barChart);

        Description d2 = new Description();
        d2.setText("Gráfico de Barras");
        d2.setTextSize(14);
        d2.setEnabled(false);
        barChart.setDescription(d2);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 38.5f));
        entries.add(new BarEntry(2f, 43.4f));
        entries.add(new BarEntry(3f, 59.2f));
        entries.add(new BarEntry(4f, 63.9f));
        entries.add(new BarEntry(5f, 58.4f));
        entries.add(new BarEntry(6f, 61.1f));
        entries.add(new BarEntry(7f, 57.7f));
        entries.add(new BarEntry(8f, 52.4f));
        entries.add(new BarEntry(9f, 57.5f));
        entries.add(new BarEntry(10f, 61.3f));
        entries.add(new BarEntry(11f, 53.8f));
        entries.add(new BarEntry(12f, 46.6f));

        barChart.setOnChartValueSelectedListener(this);
        barChart.setDrawValueAboveBar(true);

        // when value is selected
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String Y = String.valueOf(h.getY());

                // index (1-12) and xData Array (0-11)
                String X = xData[pos - 1];

                vendor.addToast(X + "\n" + Y + " Litros", ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        BarDataSet dataset = new BarDataSet(entries, "Meses: Jan > Dez");

//        ArrayList<String> labels = new ArrayList<>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");

        BarData d = new BarData(dataset);
//        BarData data = new BarData(labels, dataset);

        d.setBarWidth(0.9f); // set custom bar width
        dataset.setColors(ColorTemplate.MATERIAL_COLORS);
        barChart.setData(d);
        barChart.setFitBars(true); // make the x-axis fit exactly all
        barChart.animateY(3250);
        barChart.invalidate(); // refresh

        // BarChart - END

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // PieChart - START

        pieChart = (PieChart) findViewById(R.id.pieChart);

        Description d3 = new Description();
        d3.setText("Clique no Gráfico para exibir o Mês");
        d3.setTextSize(14);
        d3.setEnabled(false);
//        pieChart.getPaint(Chart.PAINT_CENTER_TEXT);
        pieChart.setDescription(d3);

        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
//        pieChart.setHoleColor(Color.WHITE);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(35);
        pieChart.setTransparentCircleAlpha(200);
        pieChart.setCenterText("TOTAL");
        pieChart.setCenterTextSize(14);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);

        addDataSet(pieChart);

        // when value is selected
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                Log.d(TAG, "##### " + e.toString() + " #####");

                // get X index
                int pos = (int) h.getX();

                // get Y index > value
                String Y = String.valueOf(h.getY());

                String X = xData[pos];
                vendor.addToast(X + "\n" + Y + " Litros", ChartActivity.this);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.animateY(4000);
        pieChart.invalidate(); // refresh

        // PieChart - END

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Log.d(TAG, "##### ChartActivity - OK #####");
    }

    // addData to PieChart
    private void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }

        for (int i = 1; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Consumo por Mês em Litros");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < xData.length; i++) {
            colors.add(Color.CYAN);
//            colors.add(ColorTemplate.JOYFUL_COLORS[i]);
        }
//        pieDataSet.setColors(colors);
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        //add legend to chart
        Legend l = pieChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(14);

//        int layout = getResources().getConfiguration().orientation;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            l.setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        } else {
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        }

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
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
        Intent i = new Intent(ChartActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onBackPressed() {
        vendor.addIntent(ChartActivity.this, MainActivity.class);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
