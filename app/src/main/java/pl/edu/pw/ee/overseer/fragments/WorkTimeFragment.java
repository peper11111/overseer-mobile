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
import pl.edu.pw.ee.overseer.services.WorkTimeService;
import pl.edu.pw.ee.overseer.utilities.ChartUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class WorkTimeFragment extends Fragment {
    private Activity mContext;
    private PieChart mPieChart;

    private WorkTimeUpdateReceiver mWorkTimeUpdateReceiver;
    private long mMaximum = 86400;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_worktime, container, false);

        mContext = getActivity();
        mContext.setTitle("Work Time");
        mWorkTimeUpdateReceiver = new WorkTimeUpdateReceiver();

        mPieChart = (PieChart) v.findViewById(R.id.today_chart);
        ChartUtility.createPieChar(mMaximum, mPieChart);
        mPieChart.invalidate();

        updateChart(new SharedPreferencesUtility(mContext).getLong(SharedPreferencesUtility.KEY_DIFF, 0));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WorkTimeService.ACTION_NEW_TIME);
        mContext.registerReceiver(mWorkTimeUpdateReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        mContext.unregisterReceiver(mWorkTimeUpdateReceiver);
        super.onStop();
    }

    private class WorkTimeUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateChart(intent.getLongExtra("millis", 0));
        }
    }

    private void updateChart(long millis) {
        long time = millis / 1000;
        long seconds = time % 60;
        long minutes = time / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        long workTime = time > mMaximum ? mMaximum : time;
        long remaining = mMaximum - workTime;
        long overtime = time > mMaximum ? (time - mMaximum) : 0;

        mPieChart.setCenterText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        mPieChart.getData().getDataSetByIndex(0).getEntryForIndex(1).setY(workTime);
        mPieChart.getData().getDataSetByIndex(0).getEntryForIndex(2).setY(remaining);
        mPieChart.getData().getDataSetByIndex(0).getEntryForIndex(0).setY(overtime);
        mPieChart.notifyDataSetChanged();
        mPieChart.invalidate();
    }
}