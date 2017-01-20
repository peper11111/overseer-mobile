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


public class DetailsWorkTimeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);

        DetailsFragment parent = (DetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        try {
            if (parent.mSubordinate != null && parent.mSubordinate.has("worktime")) {
                LineChart lineChart = (LineChart) v.findViewById(R.id.line_chart);
                ChartUtility.createLineChart(parent.mSubordinate.getJSONObject("worktime").getJSONObject("current").getJSONObject("month"), parent.mSubordinate.getJSONObject("worktime").getJSONObject("previous").getJSONObject("month"), lineChart);
                lineChart.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}