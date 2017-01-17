package pl.edu.pw.ee.overseer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.fragments.ProfileFragment;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Activity mContext;

    private SharedPreferencesUtility mSharedPreferencesUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), ExternalStorageUtility.getFileInputStream("/avatar.png"));
            drawable.setCornerRadius(150);
            ((ImageView) header.findViewById(R.id.drawer_avatar)).setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                break;
            case R.id.nav_work_time:
                break;
            case R.id.nav_statistics:
                break;
            case R.id.nav_subordinates:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                new SharedPreferencesUtility(mContext).remove(SharedPreferencesUtility.KEY_AUTHENTICATED).apply();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                mContext.finish();
                break;
        }

        if (fragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
