package pl.edu.pw.ee.overseer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

import pl.edu.pw.ee.overseer.tasks.StartTask;
import pl.edu.pw.ee.overseer.tasks.StopTask;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class WorkTimeService extends Service {
    public static final String ACTION_NEW_TIME = "NEW_TIME";

    private Handler mHandler;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    private long mDiff;
    private long mStart;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            long now = new Date().getTime();
            long millis = (now - mStart) + mDiff;
            mSharedPreferencesUtility.putLong(SharedPreferencesUtility.KEY_WORK, now).apply();

            Intent intent = new Intent();
            intent.setAction(ACTION_NEW_TIME);
            intent.putExtra("millis", millis);
            sendBroadcast(intent);
            mHandler.postDelayed(this, 500);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferencesUtility = new SharedPreferencesUtility(this);

        mStart = mSharedPreferencesUtility.getLong(SharedPreferencesUtility.KEY_TIME, new Date().getTime());
        mDiff = mSharedPreferencesUtility.getLong(SharedPreferencesUtility.KEY_DIFF, 0);

        new StartTask().execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""));
        mHandler = new Handler();

        if (!compareDates(mStart))
            mSharedPreferencesUtility.putLong(SharedPreferencesUtility.KEY_DIFF, 0).apply();

        mStart = new Date().getTime();
        mSharedPreferencesUtility.putLong(SharedPreferencesUtility.KEY_TIME, mStart).apply();
        mHandler.postDelayed(timer, 0);
    }

    private boolean compareDates(long old) {
        Calendar calendar = Calendar.getInstance();
        int now = calendar.get(Calendar.DATE);
        calendar.setTimeInMillis(old);
        int past = calendar.get(Calendar.DATE);
        return now == past;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(timer);
        mDiff += new Date().getTime() - mStart;
        new StopTask().execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""), "" + new Date().getTime());
        mSharedPreferencesUtility
                .putLong(SharedPreferencesUtility.KEY_DIFF, mDiff)
                .putLong(SharedPreferencesUtility.KEY_WORK, 0)
                .apply();
        super.onDestroy();
    }
}