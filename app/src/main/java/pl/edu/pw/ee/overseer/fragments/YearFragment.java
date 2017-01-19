package pl.edu.pw.ee.overseer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.utilities.ChartUtility;

public class YearFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_year, container, false);
        StatisticsFragment parent = (StatisticsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        try {
            LineChart lineChart = (LineChart) v.findViewById(R.id.year_chart);
            ChartUtility.createLineChart(parent.mStatistics.getJSONObject("year"), parent.mHistory.getJSONObject("year"), lineChart);
            lineChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}