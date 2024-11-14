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

        float startX = -11;
        float endX = 11;

        for (int x = (int) startX; x <= endX; x++) {
            float y = evaluateFunction(formula, x);
            entries.add(new Entry(x, y));
        }

        LineDataSet dataSet = new LineDataSet(entries, "y = " + formula);
        dataSet.setColor(color);
        dataSet.setLineWidth(2f);

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);

        return new LineData(dataSet);
    }

    private float evaluateFunction(String formula, float x) {
        switch (formula) {
            case "f(x)=a*b^x":
                float aExp = 1.0f;
                float bExp = 2.0f;
                return aExp * (float) Math.pow(bExp, x);

            case "f(x)=a*e^-0.5(x-b/c)^2":
                float aGauss = 1.0f;
                float bGauss = 0.0f;
                float cGauss = 1.0f;
                return aGauss * (float) Math.exp(-0.5 * Math.pow((x - bGauss) / cGauss, 2));

            case "f(n)=(1/√5)(((1+√5)/2)^n-((1-√5)/2)^n)":
                return (float) ((1 / Math.sqrt(5)) *
                        (Math.pow((1 + Math.sqrt(5)) / 2, x) - Math.pow((1 - Math.sqrt(5)) / 2, x)));

            default:
                return 0;
        }
    }
}
