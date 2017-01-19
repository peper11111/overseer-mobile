package pl.edu.pw.ee.overseer.utilities;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ChartUtility {
    public static void createLineChart(JSONObject current, JSONObject previous, LineChart chart) {
        try {
            List<Entry> currentEntries = new ArrayList<>();
            Iterator<String> currentKeys = current.keys();
            while (currentKeys.hasNext()) {
                String key = currentKeys.next();
                currentEntries.add(new Entry(Integer.parseInt(key), current.getLong(key) / 3600000.0f));
            }

            Collections.sort(currentEntries, new Comparator<Entry>() {
                @Override
                public int compare(Entry o1, Entry o2) {
                    float result = o1.getX() - o2.getX();
                    if (result > 0)
                        return 1;
                    if (result < 0)
                        return -1;
                    return 0;
                }
            });

            LineDataSet currentDataSet = new LineDataSet(currentEntries, "Current");
            currentDataSet.setColor(Color.parseColor("#4FC3F7"));
            currentDataSet.setCircleColor(Color.parseColor("#4FC3F7"));
            currentDataSet.setFillColor(Color.parseColor("#4FC3F7"));
            currentDataSet.setDrawFilled(true);
            currentDataSet.setLineWidth(2);
            currentDataSet.setDrawHighlightIndicators(false);

            List<Entry> previousEntries = new ArrayList<>();
            Iterator<String> previousKeys = previous.keys();
            while (previousKeys.hasNext()) {
                String key = previousKeys.next();
                previousEntries.add(new Entry(Integer.parseInt(key), previous.getLong(key) / 3600000.0f));
            }

            Collections.sort(previousEntries, new Comparator<Entry>() {
                @Override
                public int compare(Entry o1, Entry o2) {
                    float result = o1.getX() - o2.getX();
                    if (result > 0)
                        return 1;
                    if (result < 0)
                        return -1;
                    return 0;
                }
            });

            LineDataSet previousDataSet = new LineDataSet(previousEntries, "Previous");
            previousDataSet.setColor(Color.parseColor("#E0E0E0"));
            previousDataSet.setCircleColor(Color.parseColor("#E0E0E0"));
            previousDataSet.setFillColor(Color.parseColor("#E0E0E0"));
            previousDataSet.setDrawFilled(true);
            previousDataSet.setLineWidth(2);
            previousDataSet.setDrawHighlightIndicators(false);

            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(previousDataSet);
            dataSets.add(currentDataSet);

            LineData lineData = new LineData(dataSets);
            lineData.setDrawValues(false);

            chart.setData(lineData);
            chart.getAxisRight().setEnabled(false);
            chart.getAxisLeft().setAxisMinimum(0);
            chart.getAxisLeft().setDrawTopYLabelEntry(true);
            chart.getAxisLeft().setGranularityEnabled(true);
            chart.getAxisLeft().setGranularity(1);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.setTouchEnabled(false);
            Description description = new Description();
            description.setText("");
            chart.setDescription(description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createPieChar(long maximum, PieChart chart) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0, "Overtime"));
        entries.add(new PieEntry(0, "Worktime"));
        entries.add(new PieEntry(maximum, "Remaining"));

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(Color.parseColor("#AED581"), Color.parseColor("#4FC3F7"), Color.parseColor("#E0E0E0"));
        set.setSliceSpace(2);

        PieData data = new PieData(set);
        data.setDrawValues(false);

        chart.setData(data);
        chart.setCenterText("00:00:00");
        chart.setCenterTextSize(40);
        chart.setDrawEntryLabels(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.setTouchEnabled(false);
    }
}
