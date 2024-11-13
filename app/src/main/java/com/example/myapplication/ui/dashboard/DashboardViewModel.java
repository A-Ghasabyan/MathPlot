package com.example.myapplication.ui.dashboard;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    public LineData generateLineData(String formula, int color) {
        List<Entry> entries = new ArrayList<>();
        for (float x = -10; x <= 10; x += 0.1f) {
            float y = evaluateFunction(formula, x);
            entries.add(new Entry(x, y));
        }

        LineDataSet dataSet = new LineDataSet(entries, "y = " + formula);
        dataSet.setColor(color);
        dataSet.setLineWidth(2f);

        return new LineData(dataSet);
    }

    private float evaluateFunction(String formula, float x) {
        switch (formula) {
            case "x^2":
                return x * x;
            case "sin(x)":
                return (float) Math.sin(x);
            case "x^3":
                return x * x * x;
            default:
                return 0;
        }
    }
}
