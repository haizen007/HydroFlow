package online.hydroflow.utils;

import android.app.Activity;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class ValueFormatter extends Activity implements IValueFormatter {

    private final DecimalFormat mFormat;

    public ValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//        return mFormat.format(value) + " $"; // e.g. append a dollar-sign
        return mFormat.format(value);
    }

//    public float ToOneDecimal(float value) {
//        float f = Float.valueOf(mFormat.format(value)); // Convert to one decimal
//        return f;
//    }

}