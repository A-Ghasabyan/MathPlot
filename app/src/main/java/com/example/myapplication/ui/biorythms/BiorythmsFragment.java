package com.example.myapplication.ui.biorythms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentBiorythmsBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;

public class BiorythmsFragment extends Fragment {

    private FragmentBiorythmsBinding binding;
    private EditText birthYearEditText, birthMonthEditText, birthDayEditText;
    private EditText currentYearEditText, currentMonthEditText, currentDayEditText;
    private LineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBiorythmsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        birthYearEditText = binding.birthYearEditText;
        birthMonthEditText = binding.birthMonthEditText;
        birthDayEditText = binding.birthDayEditText;

        currentYearEditText = binding.currentYearEditText;
        currentMonthEditText = binding.currentMonthEditText;
        currentDayEditText = binding.currentDayEditText;

        addRealTimeValidation();

        chart = binding.lineChart;

        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        adjustChartLayout();

        binding.plotButton.setOnClickListener(v -> {
            try {
                // Parse inputs
                int birthYear = Integer.parseInt(birthYearEditText.getText().toString());
                int birthMonth = Integer.parseInt(birthMonthEditText.getText().toString());
                int birthDay = Integer.parseInt(birthDayEditText.getText().toString());

                int currentYear = Integer.parseInt(currentYearEditText.getText().toString());
                int currentMonth = Integer.parseInt(currentMonthEditText.getText().toString());
                int currentDay = Integer.parseInt(currentDayEditText.getText().toString());

                // Validate dates
                if (!isValidDate(birthYear, birthMonth, birthDay) ||
                        !isValidDate(currentYear, currentMonth, currentDay) ||
                        !isCurrentDateAfterBirthDate(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay)) {
                    Toast.makeText(getContext(), "Invalid date inputs. Please check again.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Plot biorythms
                plotBiorythms(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay);

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter valid numbers.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void addRealTimeValidation() {
        // Add TextWatcher for birth year
        birthYearEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateBirthYear();
            }
        });

        // Add TextWatcher for birth month
        birthMonthEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateBirthMonth();
            }
        });

        // Add TextWatcher for birth day
        birthDayEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateBirthDay();
            }
        });

        // Add TextWatcher for current year
        currentYearEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCurrentYear();
            }
        });

        // Add TextWatcher for current month
        currentMonthEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCurrentMonth();
            }
        });

        // Add TextWatcher for current day
        currentDayEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCurrentDay();
            }
        });
    }

    // Validation methods
    private void validateBirthYear() {
        String text = birthYearEditText.getText().toString().trim();
        if (text.isEmpty()) {
            birthYearEditText.setError("Birth year is required");
        } else {
            int year = Integer.parseInt(text);
            if (year > Calendar.getInstance().get(Calendar.YEAR)) {
                birthYearEditText.setError("Enter a valid year");
            } else {
                birthYearEditText.setError(null); // Clear error
            }
        }
    }

    private void validateBirthMonth() {
        String text = birthMonthEditText.getText().toString().trim();
        if (text.isEmpty()) {
            birthMonthEditText.setError("Birth month is required");
        } else {
            int month = Integer.parseInt(text);
            if (month < 1 || month > 12) {
                birthMonthEditText.setError("Enter a valid month");
            } else {
                birthMonthEditText.setError(null); // Clear error
            }
        }
    }

    private void validateBirthDay() {
        String text = birthDayEditText.getText().toString().trim();
        if (text.isEmpty()) {
            birthDayEditText.setError("Birth day is required");
        } else {
            int day = Integer.parseInt(text);
            if (day < 1 || day > 31) {
                birthDayEditText.setError("Enter a valid day");
            } else {
                birthDayEditText.setError(null); // Clear error
            }
        }
    }

    private void validateCurrentYear() {
        String birthYearText = birthYearEditText.getText().toString().trim();
        String currentYearText = currentYearEditText.getText().toString().trim();

        if (currentYearText.isEmpty()) {
            currentYearEditText.setError("Current year is required");
        } else if (!birthYearText.isEmpty()) {
            int birthYear = Integer.parseInt(birthYearText);
            int currentYear = Integer.parseInt(currentYearText);

            if (currentYear < birthYear) {
                currentYearEditText.setError("Current year must be greater than or equal to the birth year");
            } else {
                currentYearEditText.setError(null);
            }
        }
    }

    private void validateCurrentMonth() {
        String text = currentMonthEditText.getText().toString().trim();
        if (text.isEmpty()) {
            currentMonthEditText.setError("Current month is required");
        } else {
            int month = Integer.parseInt(text);
            if (month < 1 || month > 12) {
                currentMonthEditText.setError("Enter a valid month");
            } else {
                currentMonthEditText.setError(null); // Clear error
            }
        }
    }

    private void validateCurrentDay() {
        String text = currentDayEditText.getText().toString().trim();
        if (text.isEmpty()) {
            currentDayEditText.setError("Current day is required");
        } else {
            int day = Integer.parseInt(text);
            if (day < 1 || day > 31) {
                currentDayEditText.setError("Enter a valid day");
            } else {
                currentDayEditText.setError(null); // Clear error
            }
        }
    }

    private void plotBiorythms(int birthYear, int birthMonth, int birthDay,
                               int currentYear, int currentMonth, int currentDay) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(birthYear, birthMonth - 1, birthDay);

        Calendar currentDate = Calendar.getInstance();
        currentDate.set(currentYear, currentMonth - 1, currentDay);

        long daysBetween = (currentDate.getTimeInMillis() - birthDate.getTimeInMillis()) / (1000 * 60 * 60 * 24);

        ArrayList<Entry> physicalData = new ArrayList<>();
        ArrayList<Entry> emotionalData = new ArrayList<>();
        ArrayList<Entry> intellectualData = new ArrayList<>();
        ArrayList<String> xAxisLabels = new ArrayList<>();

        Calendar plottingDate = (Calendar) currentDate.clone();

        for (int i = 0; i < 30; i++) {
            float physical = (float) Math.sin((2 * Math.PI * (daysBetween + i)) / 23);
            float emotional = (float) Math.sin((2 * Math.PI * (daysBetween + i)) / 28);
            float intellectual = (float) Math.sin((2 * Math.PI * (daysBetween + i)) / 33);

            physicalData.add(new Entry(i, physical));
            emotionalData.add(new Entry(i, emotional));
            intellectualData.add(new Entry(i, intellectual));

            xAxisLabels.add(String.format("%02d/%02d",
                    plottingDate.get(Calendar.MONTH) + 1,
                    plottingDate.get(Calendar.DAY_OF_MONTH)));

            plottingDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        LineDataSet physicalSet = new LineDataSet(physicalData, "Physical");
        LineDataSet emotionalSet = new LineDataSet(emotionalData, "Emotional");
        LineDataSet intellectualSet = new LineDataSet(intellectualData, "Intellectual");

        physicalSet.setColor(getResources().getColor(R.color.physical_color));
        emotionalSet.setColor(getResources().getColor(R.color.emotional_color));
        intellectualSet.setColor(getResources().getColor(R.color.intellectual_color));

        LineData data = new LineData(physicalSet, emotionalSet, intellectualSet);

        chart.setData(data);
        chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) Math.floor(value); // Safely convert to an integer index
                if (index >= 0 && index < xAxisLabels.size()) {
                    return xAxisLabels.get(index); // Return the corresponding label
                }
                return ""; // Default fallback
            }
        });

        chart.invalidate();
    }

    private void adjustChartLayout() {
        ViewGroup.LayoutParams params = chart.getLayoutParams();

        int chartWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int chartHeight = getResources().getDisplayMetrics().heightPixels / 3;

        params.width = chartWidth;
        params.height = chartHeight;
        chart.setLayoutParams(params);

        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) chart.getLayoutParams();
        marginParams.setMargins(20, 30, 20, 30);
        chart.setLayoutParams(marginParams);
    }

    private boolean isValidDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        try {
            calendar.set(year, month - 1, day);
            calendar.getTime();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCurrentDateAfterBirthDate(int birthYear, int birthMonth, int birthDay,
                                                int currentYear, int currentMonth, int currentDay) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(birthYear, birthMonth - 1, birthDay);

        Calendar currentDate = Calendar.getInstance();
        currentDate.set(currentYear, currentMonth - 1, currentDay);

        return currentDate.after(birthDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
