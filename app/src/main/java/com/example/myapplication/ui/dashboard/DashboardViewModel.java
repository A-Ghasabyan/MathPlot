package com.example.myapplication.ui.dashboard;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    public LineData generateLineData(String function, int color) {
        List<Entry> entries = new ArrayList<>();
        for (float x = -10; x <= 10; x += 0.1f) {
            float y = evaluateFunction(function, x);
            entries.add(new Entry(x, y));
        }

        LineDataSet dataSet = new LineDataSet(entries, "y = " + function);
        dataSet.setColor(color);
        dataSet.setLineWidth(2f);

        return new LineData(dataSet);
    }

    private float evaluateFunction(String function, float x) {
        if (function.equals("x^3")) {
            return x * x * x;
        } else if (function.equals("sin(x)")) {
            return (float) Math.sin(x);
        } else {
            return 0;
        }
    }
}
