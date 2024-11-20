package com.example.myapplication.ui.biorythms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentBiorythmsBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;

public class BiorythmsFragment extends Fragment {

    private FragmentBiorythmsBinding binding;
    private EditText birthYearEditText, birthMonthEditText, birthDayEditText;
    private LineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBiorythmsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        birthYearEditText = binding.birthYearEditText;
        birthMonthEditText = binding.birthMonthEditText;
        birthDayEditText = binding.birthDayEditText;
        chart = binding.lineChart;

        // Setup button to plot biorythms
        binding.plotButton.setOnClickListener(v -> {
            int year = Integer.parseInt(birthYearEditText.getText().toString());
            int month = Integer.parseInt(birthMonthEditText.getText().toString()) - 1; // Month is 0-based
            int day = Integer.parseInt(birthDayEditText.getText().toString());

            plotBiorythms(year, month, day);
        });

        return root;
    }

    private void plotBiorythms(int year, int month, int day) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month, day);

        Calendar currentDate = Calendar.getInstance();
        long daysBetween = (currentDate.getTimeInMillis() - birthDate.getTimeInMillis()) / (1000 * 60 * 60 * 24); // days since birth

        // Initialize ArrayLists to hold data
        ArrayList<Entry> physicalData = new ArrayList<>();
        ArrayList<Entry> emotionalData = new ArrayList<>();
        ArrayList<Entry> intellectualData = new ArrayList<>();

        for (int i = 0; i < 30; i++) { // Plot biorythms for the next month (30 days)
            float physical = (float) Math.sin((2 * Math.PI * (daysBetween + i)) / 23);
            float emotional = (float) Math.sin((2 * Math.PI * (daysBetween + i)) / 28);
            float intellectual = (float) Math.sin((2 * Math.PI * (daysBetween + i)) / 33);

            // Add entries for each day (x-axis: day number, y-axis: value)
            physicalData.add(new Entry(i, physical));
            emotionalData.add(new Entry(i, emotional));
            intellectualData.add(new Entry(i, intellectual));
        }

        // Create LineDataSet with Entry data
        LineDataSet physicalSet = new LineDataSet(physicalData, "Physical");
        LineDataSet emotionalSet = new LineDataSet(emotionalData, "Emotional");
        LineDataSet intellectualSet = new LineDataSet(intellectualData, "Intellectual");

        // Set colors for the lines (optional)
        physicalSet.setColor(getResources().getColor(R.color.physical_color)); // Define these colors in resources
        emotionalSet.setColor(getResources().getColor(R.color.emotional_color));
        intellectualSet.setColor(getResources().getColor(R.color.intellectual_color));

        // Add data sets to a list of ILineDataSet
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(physicalSet);
        dataSets.add(emotionalSet);
        dataSets.add(intellectualSet);

        // Create LineData and set it to the chart
        LineData data = new LineData(dataSets); // This constructor accepts a List<ILineDataSet>
        chart.setData(data);
        chart.invalidate(); // Refresh the chart
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
