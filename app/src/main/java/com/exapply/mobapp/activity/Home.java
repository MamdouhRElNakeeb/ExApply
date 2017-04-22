package com.exapply.mobapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.exapply.mobapp.R;
import com.exapply.mobapp.adapter.HomeViewPagerAdapter;
import com.exapply.mobapp.fragment.FeedFragment;
import com.exapply.mobapp.fragment.SavedFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mamdouhelnakeeb on 2/13/17.
 */

public class Home extends AppCompatActivity {

    Toolbar toolbar;
    TabItem homeFeed, savedFeed;
    TabLayout tabLayout;
    ViewPager viewPager;

    FloatingActionButton addExFAB;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    FeedFragment feedFragment;
    private static final String TAG = Home.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

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

        addExFAB = (FloatingActionButton) findViewById(R.id.add_experience_btn);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);

/*
        homeFeed = (TabItem) findViewById(R.id.homeTabItem);
        savedFeed = (TabItem) findViewById(R.id.favTabItem);
*/

/*

        homeFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        savedFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager.setCurrentItem(1);
            }
        });
*/
        addExFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddExperience.class));
            }
        });

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            //loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();
        }
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
                        return true;

                    case R.id.nav_profile:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        startActivity(new Intent(Home.this, Profile.class));
                        navigationView.getMenu().getItem(1).setChecked(true);
                        return true;

                    case R.id.nav_add_event:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        startActivity(new Intent(getBaseContext(), AddExperience.class));
                        navigationView.getMenu().getItem(2).setChecked(true);
                        return true;

                    case R.id.nav_settings:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        startActivity(new Intent(Home.this, Profile.class));
                        navigationView.getMenu().getItem(3).setChecked(true);
                        return true;

                    case R.id.nav_about:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(4).setChecked(true);
                        startActivity(new Intent(getBaseContext(), About.class));
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
                        startActivity(new Intent(Home.this, Login.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        feedFragment = new FeedFragment();
        adapter.addFragment(feedFragment, "");
        adapter.addFragment(new SavedFragment(), "");
        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab homeTab = tabLayout.getTabAt(0);
        homeTab.setIcon(R.drawable.home_btn_tab);



        TabLayout.Tab savedTab = tabLayout.getTabAt(1);
        savedTab.setIcon(R.drawable.unfavorite_btn);
    }

}