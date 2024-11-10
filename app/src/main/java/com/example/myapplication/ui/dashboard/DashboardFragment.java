package com.example.myapplication.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFunctionDialog();
            }
        });

        return root;
    }

    private void showFunctionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Function");

        // Create and set up the AutoCompleteTextView for search functionality
        AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getContext());
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.functions_array));
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);  // Start filtering after the first character

        builder.setView(autoCompleteTextView);

        builder.setPositiveButton("Plot", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedFunction = autoCompleteTextView.getText().toString();
                if (!selectedFunction.isEmpty()) {
                    plotFunctionInDialog(selectedFunction);
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void plotFunctionInDialog(String function) {
        List<Entry> entries = new ArrayList<>();
        for (float x = -10; x <= 10; x += 0.1f) {
            float y = evaluateFunction(function, x);
            entries.add(new Entry(x, y));
        }

        LineDataSet dataSet = new LineDataSet(entries, "y = " + function);
        dataSet.setColor(R.color.purple_500);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);

        Dialog(lineData, function);
    }

    private void Dialog(LineData lineData, String function) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_chart, null);

        LineChart chart = dialogView.findViewById(R.id.chart);
        chart.setData(lineData);
        chart.invalidate();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setTitle("Graph: y = " + function)
                .setNegativeButton("Close", null)
                .create();

        dialog.show();

        dialog.setOnDismissListener(dialogInterface -> createWindowOnDashboard(lineData, function));
    }

    private int offsetX = 0;
    private int offsetY = 0;

    private void createWindowOnDashboard(LineData lineData, String function) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        LineChart chart = new LineChart(getContext());
        chart.setData(lineData);
        chart.invalidate();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                500, 400);

        params.leftMargin = offsetX;
        params.topMargin = offsetY;
        frameLayout.setLayoutParams(params);

        frameLayout.addView(chart);
        binding.dashboardLayout.addView(frameLayout);

        offsetX += 520;
        if (offsetX + 500 > binding.dashboardLayout.getWidth()) {
            offsetX = 0;
            offsetY += 420;
        }

        makeDraggable(frameLayout);
    }

    private void makeDraggable(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            float xDelta, yDelta;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getContext() == null) return false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDelta = view.getX() - event.getRawX();
                        yDelta = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + xDelta)
                                .y(event.getRawY() + yDelta)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
