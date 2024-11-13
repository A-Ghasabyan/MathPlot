package com.example.myapplication.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionAdapter extends ArrayAdapter<Map.Entry<String, String>> {
    private List<Map.Entry<String, String>> originalFunctions;
    private List<Map.Entry<String, String>> filteredFunctions;

    public FunctionAdapter(@NonNull Context context, List<Map.Entry<String, String>> functions) {
        super(context, 0, functions);
        this.originalFunctions = new ArrayList<>(functions);
        this.filteredFunctions = functions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.function_item, parent, false);
        }

        Map.Entry<String, String> function = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.function_name);
        TextView formulaTextView = convertView.findViewById(R.id.function_formula);

        if (function != null) {
            nameTextView.setText(function.getKey());
            formulaTextView.setText(function.getValue());
        }

        return convertView;
    }

    @Nullable
    @Override
    public Map.Entry<String, String> getItem(int position) {
        return filteredFunctions.get(position);
    }

    @Override
    public int getCount() {
        return filteredFunctions.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Map.Entry<String, String>> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalFunctions);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Map.Entry<String, String> entry : originalFunctions) {
                        if (entry.getKey().toLowerCase().contains(filterPattern) ||
                                entry.getValue().toLowerCase().contains(filterPattern)) {
                            filteredList.add(entry);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredFunctions.clear();
                filteredFunctions.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}

