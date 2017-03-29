package com.exapply.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.exapply.app.R;
import com.exapply.app.adapter.HomeViewPagerAdapter;
import com.exapply.app.fragment.FeedFragment;
import com.exapply.app.fragment.SavedFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mamdouhelnakeeb on 2/13/17.
 */

public class Home extends AppCompatActivity {

    Toolbar toolbar;
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
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        addExFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, AddExperience.class));
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_find:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        return true;

                    case R.id.nav_add_event:
                        startActivity(new Intent(Home.this, AddExperience.class));
                        navigationView.getMenu().getItem(0).setChecked(true);
                        return true;

                 //   case R.id.nav_notifications:
                   //     mDrawerLayout.closeDrawer(Gravity.LEFT);
                    //    navigationView.getMenu().getItem(0).setChecked(true);
                     //   return true;

                    case R.id.nav_settings:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        return true;

                    case R.id.nav_about:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        return true;

                    case R.id.nav_contact:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(0).setChecked(true);
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
        adapter.addFragment(feedFragment, "Feed");
        adapter.addFragment(new SavedFragment(), "Saved");
        viewPager.setAdapter(adapter);
    }

}