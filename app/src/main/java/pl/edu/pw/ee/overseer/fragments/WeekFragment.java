package pl.edu.pw.ee.overseer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.edu.pw.ee.overseer.R;

public class WeekFragment extends Fragment {
    private LineChart mLineChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week, container, false);

        StatisticsFragment parent = (StatisticsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        JSONObject week = parent.mWeek;

        List<Entry> currentWeek = new ArrayList<>();
        try {
            Iterator<String> keys = week.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                currentWeek.add(new Entry(Integer.parseInt(key), week.getLong(key) / 86400000.0f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LineDataSet currentDataSet = new LineDataSet(currentWeek, "Current Week");
        currentDataSet.setColor(Color.parseColor("#4DB6AC"));
        currentDataSet.setCircleColor(Color.parseColor("#4DB6AC"));
        currentDataSet.setFillColor(Color.parseColor("#4DB6AC"));
        currentDataSet.setDrawFilled(true);
        currentDataSet.setLineWidth(2);
        currentDataSet.setDrawHighlightIndicators(false);

//        List<Entry> past = new ArrayList<Entry>();
//        past.add(new Entry(1, 12f));
//        past.add(new Entry(2, 3.2f));
//        past.add(new Entry(3, 1.2f));
//        past.add(new Entry(4, 12.1f));
//        past.add(new Entry(5, 6.1f));
//        past.add(new Entry(6, 7.1f));
//        past.add(new Entry(7, 0));
//
//        LineDataSet pastDataSet = new LineDataSet(past, "Previous Week");
//        pastDataSet.setColor(Color.parseColor("#E0E0E0"));
//        pastDataSet.setCircleColor(Color.parseColor("#E0E0E0"));
//        pastDataSet.setFillColor(Color.parseColor("#E0E0E0"));
//        pastDataSet.setDrawFilled(true);
//        pastDataSet.setLineWidth(2);
//        pastDataSet.setDrawHighlightIndicators(false);
//
//        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//        dataSets.add(pastDataSet);
//        dataSets.add(currentDataSet);

        LineData lineData = new LineData(currentDataSet);
        lineData.setDrawValues(false);

        mLineChart = (LineChart) v.findViewById(R.id.week_chart);
        mLineChart.setData(lineData);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisLeft().setAxisMinimum(0);
        mLineChart.getAxisLeft().setDrawTopYLabelEntry(true);
        mLineChart.getAxisLeft().setGranularityEnabled(true);
        mLineChart.getAxisLeft().setGranularity(1);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setPinchZoom(false);
        Description description = new Description();
        description.setText("");
        mLineChart.setDescription(description);
        mLineChart.invalidate();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLineChart.invalidate();
    }
}
