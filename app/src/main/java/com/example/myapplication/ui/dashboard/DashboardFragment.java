package com.example.myapplication.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(view -> showFunctionDialog());

        return root;
    }

    private void showFunctionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Function");

        String[] displayNames = getResources().getStringArray(R.array.functions_display_names);
        String[] formulas = getResources().getStringArray(R.array.functions_formulas);

        Map<String, String> functionMap = new HashMap<>();
        for (int i = 0; i < displayNames.length; i++) {
            functionMap.put(displayNames[i], formulas[i]);
        }

        List<Map.Entry<String, String>> functionList = new ArrayList<>(functionMap.entrySet());

        FunctionAdapter adapter = new FunctionAdapter(getContext(), functionList);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_function_search, null);
        EditText searchEditText = dialogView.findViewById(R.id.search_edit_text);
        ListView listView = dialogView.findViewById(R.id.function_list_view);
        listView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        builder.setView(dialogView);
        builder.setNegativeButton("Close", null);
        builder.show();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Map.Entry<String, String> selectedFunction = adapter.getItem(position);
            if (selectedFunction != null) {
                String displayName = selectedFunction.getKey();
                String formula = selectedFunction.getValue();
                plotFunctionInDialog(formula, displayName);
            }
        });
    }

    private void plotFunctionInDialog(String formula, String displayName) {
        LineData lineData = dashboardViewModel.generateLineData(
                formula,
                getResources().getColor(R.color.purple_500, null)
        );
        showChartDialog(lineData, displayName);
    }

    private void showChartDialog(LineData lineData, String displayName) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_chart, null);

        LineChart chart = dialogView.findViewById(R.id.chart);
        chart.setData(lineData);
        chart.invalidate();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setTitle("Graph: " + displayName)
                .setNegativeButton("Close", null)
                .create();

        dialog.show();
        dialog.setOnDismissListener(dialogInterface -> createWindowOnDashboard(lineData, displayName));
    }

    private int offsetX = 0;
    private int offsetY = 0;

    private void createWindowOnDashboard(LineData lineData, String function) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        LineChart chart = new LineChart(getContext());
        chart.setData(lineData);
        chart.invalidate();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 400);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
