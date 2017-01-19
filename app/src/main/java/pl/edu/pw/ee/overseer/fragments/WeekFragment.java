package pl.edu.pw.ee.overseer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.utilities.LineChartUtility;

public class WeekFragment extends Fragment {
    private LineChart mLineChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week, container, false);

        mLineChart = (LineChart) v.findViewById(R.id.week_chart);

        StatisticsFragment parent = (StatisticsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        try {
            LineChartUtility.createChart(parent.mStatistics.getJSONObject("week"), parent.mHistory.getJSONObject("week"), mLineChart);
            mLineChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLineChart.invalidate();
    }
}
