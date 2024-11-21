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

            case "f(x)=cosh(x)": // Hyperbolic Cosine
                return (float) Math.cosh(x);

            case "f(x)=a*e^-0.5(x-b/c)^2":
                float aGauss = 1.0f;
                float bGauss = 0.0f;
                float cGauss = 1.0f;
                return aGauss * (float) Math.exp(-0.5 * Math.pow((x - bGauss) / cGauss, 2));

            case "f(n)=(1/√5)(((1+√5)/2)^n-((1-√5)/2)^n)":
                return (float) ((1 / Math.sqrt(5)) *
                        (Math.pow((1 + Math.sqrt(5)) / 2, x) - Math.pow((1 - Math.sqrt(5)) / 2, x)));

            case "f(x)=(Vmax*x)/(Km + x)": // Michaelis-Menten function
                if (x < 0) x = 0; // Ensure x is positive, or use a positive range for x
                float Vmax = 10.0f; // Maximum reaction velocity
                float Km = 5.0f;    // Michaelis constant
                return (Vmax * x) / (Km + x);

            case "f(x)=a*x^2 + b*x + c": // Parabola
                float aParabola = 1.0f;
                float bParabola = 0.0f;
                float cParabola = 0.0f;
                return aParabola * (x * x) + bParabola * x + cParabola;

            case "f(x)=a*(x^2)/(x^2 + b^2)": // Bridge Load (simplified)
                float aBridge = 1.0f;
                float bBridge = 2.0f;
                return aBridge * (x * x) / (x * x + bBridge * bBridge);

            case "f(x)=A*sin(ωx + φ)": // Sine wave
                float A = 1.0f; // Amplitude
                float omega = 1.0f; // Angular frequency
                float phi = 0.0f; // Phase shift
                return A * (float) Math.sin(omega * x + phi);

            case "f(x)=F/A": // Material Stress (σ = F / A) with varying Force
                float k = 10.0f; // Constant factor for varying force
                float force = k * x; // Force increases linearly with x
                float area = 5.0f; // Cross-sectional area remains constant
                return force / area;

            case "f(x)=e^x": // Euler's Number (e^x)
                return (float) Math.exp(x);

            case "f(x)=a*x^3 + b*x^2 + c*x + d": // Cubic function
                float aCubic = 1.0f;
                float bCubic = 0.0f;
                float cCubic = 0.0f;
                float dCubic = 0.0f;
                return aCubic * (x * x * x) + bCubic * (x * x) + cCubic * x + dCubic;

            default:
                return 0;
        }
    }
}