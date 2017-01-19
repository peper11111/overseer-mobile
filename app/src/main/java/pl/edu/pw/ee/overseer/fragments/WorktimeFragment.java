package pl.edu.pw.ee.overseer.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.services.WorktimeService;
import pl.edu.pw.ee.overseer.utilities.ChartUtility;

public class WorktimeFragment extends Fragment {
    private Activity mContext;
    private PieChart mPieChart;

    WorktimeUpdateReceiver mWorktimeUpdateReceiver;
    private long mMaximum = 60;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_worktime, container, false);

        mContext = getActivity();
        mContext.setTitle("Worktime");
        mWorktimeUpdateReceiver = new WorktimeUpdateReceiver();

        mPieChart = (PieChart) v.findViewById(R.id.today_chart);
        ChartUtility.createPieChar(mMaximum, mPieChart);
        mPieChart.invalidate();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WorktimeService.ACTION_NEW_TIME);
        mContext.registerReceiver(mWorktimeUpdateReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        mContext.unregisterReceiver(mWorktimeUpdateReceiver);
        super.onStop();
    }

    private class WorktimeUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateChart(intent.getLongExtra("time", 0));
        }
    }

    private void updateChart(long time) {
        long seconds = time % 60;
        long minutes = time / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        long worktime = time > mMaximum ? mMaximum : time;
        long remaining = mMaximum - worktime;
        long overtime = time > mMaximum ? (time - remaining) : 0;

        mPieChart.setCenterText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        mPieChart.getData().getDataSetByIndex(0).getEntryForIndex(1).setY(worktime);
        mPieChart.getData().getDataSetByIndex(0).getEntryForIndex(2).setY(remaining);
        mPieChart.getData().getDataSetByIndex(0).getEntryForIndex(0).setY(overtime);
        mPieChart.notifyDataSetChanged();
        mPieChart.invalidate();
    }
}