package com.exapply.mobapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.exapply.mobapp.R;

/**
 * Created by mamdouhelnakeeb on 4/12/17.
 */

public class About extends AppCompatActivity {

    Toolbar toolbar;

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.homeDL);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        initDrawer();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,  R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mDrawerToggle.syncState();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerToggle.syncState();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle.syncState();

    }

    private void initDrawer(){

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userNameTV)).setText(getSharedPreferences("UserDetails", MODE_PRIVATE).getString("name", ""));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        startActivity(new Intent(getBaseContext(), Home.class));
                        return true;

                    case R.id.nav_profile:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        startActivity(new Intent(getBaseContext(), Profile.class));
                        navigationView.getMenu().getItem(1).setChecked(true);
                        return true;

                    case R.id.nav_add_event:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        startActivity(new Intent(getBaseContext(), AddExperience.class));
                        navigationView.getMenu().getItem(2).setChecked(true);
                        return true;

                    case R.id.nav_settings:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        startActivity(new Intent(getBaseContext(), Profile.class));
                        navigationView.getMenu().getItem(3).setChecked(true);
                        return true;

                    case R.id.nav_about:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(4).setChecked(true);
                        return true;

                    case R.id.nav_contact:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(5).setChecked(true);
                        String url = "http://exapply,ml";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        return true;

                    case R.id.nav_logout:
                        SharedPreferences prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("login", "0");
                        editor.apply();
                        startActivity(new Intent(getBaseContext(), Login.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
