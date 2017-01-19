package pl.edu.pw.ee.overseer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class WorktimeService extends Service {
    public static final String ACTION_NEW_TIME = "NEW_TIME";

    private Handler mHandler;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    private long mDiff;
    private long mStart;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            long millis = (SystemClock.uptimeMillis() - mStart) + mDiff;
            long time = millis / 1000;

            Intent intent = new Intent();
            intent.setAction(ACTION_NEW_TIME);
            intent.putExtra("time", time);
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
        mHandler = new Handler();

        mDiff = mSharedPreferencesUtility.getLong(SharedPreferencesUtility.KEY_TIMER, 0);
        mStart = SystemClock.uptimeMillis();
        mHandler.postDelayed(timer, 0);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(timer);
        mDiff += SystemClock.uptimeMillis() - mStart;
        mSharedPreferencesUtility.putLong(SharedPreferencesUtility.KEY_TIMER, mDiff).apply();

        super.onDestroy();
    }
}