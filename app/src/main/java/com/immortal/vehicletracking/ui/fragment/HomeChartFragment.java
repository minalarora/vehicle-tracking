package com.immortal.vehicletracking.ui.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.immortal.vehicletracking.R;

import java.util.ArrayList;

public class HomeChartFragment extends Fragment implements View.OnClickListener, OnChartValueSelectedListener {

    private View view;
    private PieChart pieChart;
    private static FragmentManager fragmentManager;

    ArrayList<Entry> yvalues = new ArrayList<Entry>();
    ArrayList<String> xVals = new ArrayList<String>();

    public HomeChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_chart, container, false);
        initId();
        setListners();
        yvalues.add(new Entry(1, 0));
        yvalues.add(new Entry(1, 1));
        yvalues.add(new Entry(1, 2));
        yvalues.add(new Entry(1, 3));
        yvalues.add(new Entry(1, 4));

        PieDataSet dataSet = new PieDataSet(yvalues, "fee chart ");
        xVals.add("Total");
        xVals.add("Running");
        xVals.add("Stop");
        xVals.add("Idle");
        xVals.add("Inactive");

        PieData data = new PieData(xVals, dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);
        pieChart.setOnChartValueSelectedListener(this);

        return view;
    }

    private void setListners() {
    }

    private void initId() {
        pieChart = (PieChart) view.findViewById(R.id.total_piechart);
        fragmentManager = getActivity().getSupportFragmentManager();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.e("Value->>>", e + "," + dataSetIndex + "," + h);
    }

    @Override
    public void onNothingSelected() {

    }

}
