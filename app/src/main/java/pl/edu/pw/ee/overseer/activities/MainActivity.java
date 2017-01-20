package pl.edu.pw.ee.overseer.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.fragments.LocationFragment;
import pl.edu.pw.ee.overseer.fragments.ProfileFragment;
import pl.edu.pw.ee.overseer.fragments.StatisticsFragment;
import pl.edu.pw.ee.overseer.fragments.SubordinatesFragment;
import pl.edu.pw.ee.overseer.fragments.WorkTimeFragment;
import pl.edu.pw.ee.overseer.services.LocationService;
import pl.edu.pw.ee.overseer.services.WorkTimeService;
import pl.edu.pw.ee.overseer.tasks.StopTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        long start = mSharedPreferencesUtility.getLong(SharedPreferencesUtility.KEY_TIME, new Date().getTime());
        long diff = mSharedPreferencesUtility.getLong(SharedPreferencesUtility.KEY_DIFF, 0);
        long lastTime = mSharedPreferencesUtility.getLong(SharedPreferencesUtility.KEY_WORK, 0);
        if (lastTime != 0) {
            new StopTask().execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""), "" + lastTime);
            diff += lastTime - start;
            mSharedPreferencesUtility.putLong(SharedPreferencesUtility.KEY_DIFF, diff).apply();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isServiceRunning("pl.edu.pw.ee.overseer.services.WorkTimeService")) {
                    mContext.stopService(new Intent(mContext, LocationService.class));
                    mContext.stopService(new Intent(mContext, WorkTimeService.class));
                    fab.setImageResource(R.drawable.ic_briefcase);
                } else {
                    mContext.startService(new Intent(mContext, LocationService.class));
                    mContext.startService(new Intent(mContext, WorkTimeService.class));
                    fab.setImageResource(R.drawable.ic_home);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_profile);

        View header = navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.drawer_name)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_NAME, ""));
        ((TextView) header.findViewById(R.id.drawer_email)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_EMAIL, ""));

        try {
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), ExternalStorageUtility.getFileInputStream("avatar/avatar_0.ovs"));
            drawable.setCornerRadius(150);
            ((ImageView) header.findViewById(R.id.drawer_avatar)).setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isServiceRunning(String name) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : services) {
            if (service.service.getClassName().equals(name))
                return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(123123123);
        if (isServiceRunning("pl.edu.pw.ee.overseer.services.WorkTimeService")) {
            fab.setImageResource(R.drawable.ic_home);
        }
    }

    @Override
    protected void onStop() {
        if (isServiceRunning("pl.edu.pw.ee.overseer.services.WorkTimeService")) {
            buildNotification();
        }
        super.onStop();
    }

    private void buildNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Overseer")
                .setContentText("Overseer is running in the background");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123123123, mBuilder.build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            mContext.moveTaskToBack(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_location:
                fragment = new LocationFragment();
                break;
            case R.id.nav_work_time:
                fragment = new WorkTimeFragment();
                break;
            case R.id.nav_statistics:
                fragment = new StatisticsFragment();
                break;
            case R.id.nav_subordinates:
                fragment = new SubordinatesFragment();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        if (isServiceRunning("pl.edu.pw.ee.overseer.services.WorkTimeService")) {
            fab.performClick();
        }

        new SharedPreferencesUtility(mContext)
                .remove(SharedPreferencesUtility.KEY_UPDATE)
                .remove(SharedPreferencesUtility.KEY_TOKEN)
                .remove(SharedPreferencesUtility.KEY_TIME)
                .remove(SharedPreferencesUtility.KEY_DIFF)
                .remove(SharedPreferencesUtility.KEY_EMAIL)
                .remove(SharedPreferencesUtility.KEY_NAME)
                .apply();
        ExternalStorageUtility.deleteRootFolder();
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        mContext.finish();
    }
}