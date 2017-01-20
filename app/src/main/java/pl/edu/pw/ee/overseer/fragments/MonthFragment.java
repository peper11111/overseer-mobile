package pl.edu.pw.ee.overseer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.utilities.ChartUtility;

public class MonthFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        StatisticsFragment parent = (StatisticsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        try {
            if (parent.mStatistics != null) {
                LineChart lineChart = (LineChart) v.findViewById(R.id.line_chart);
                ChartUtility.createLineChart(parent.mStatistics.getJSONObject("current").getJSONObject("month"), parent.mStatistics.getJSONObject("previous").getJSONObject("month"), lineChart);
                lineChart.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}