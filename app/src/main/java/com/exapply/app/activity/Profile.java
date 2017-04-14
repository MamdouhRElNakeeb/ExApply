package com.exapply.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.exapply.app.Objects.UserData;
import com.exapply.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mamdouhelnakeeb on 3/3/17.
 */

public class Profile extends AppCompatActivity {


    private static final float BLUR_RADIUS = 25f;

    TextView name, brief, specialization, workDays;
    TextInputEditText specializationET, workDaysET;
    ImageView profilePicBgIV, workDaysPencilIV;
    RatingBar ratingBar;

    EditText briefET, mobileNoET, interestsET, emailET;

    // [START declare_auth]
    private FirebaseAuth mFirebaseAuth;
    // [END declare_auth]
    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;

    UserData userData;

    Toolbar toolbar;

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

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


        profilePicBgIV = (ImageView) findViewById(R.id.profile_pic_bg);
        profilePicBgIV.setImageBitmap(blur(BitmapFactory.decodeResource(getResources(), R.drawable.bg_climbing)));

        if (getIntent().hasExtra("userData")){
            userData = getIntent().getExtras().getParcelable("userData");
            Log.d("contains", "userData");
        }
        else {
            Log.d("NotContains", "userData");
        }

        //

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        toolbar.setTitle(mFirebaseUser.getDisplayName());

        briefET = (EditText) findViewById(R.id.company_profile_short_bio);
        emailET = (EditText) findViewById(R.id.user_email);
        mobileNoET = (EditText) findViewById(R.id.user_mobile);
        interestsET = (EditText) findViewById(R.id.user_interestes);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);

                briefET.setText(userData.brief);
                emailET.setText(userData.email);
                mobileNoET.setText(userData.mobile);
                interestsET.setText(userData.interests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addListenerForSingleValueEvent(valueEventListener);

        briefET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseAuth.getCurrentUser()
                        .getUid()).setValue(new UserData(emailET.getText().toString().trim(), mobileNoET.getText().toString().trim(), interestsET.getText().toString().trim()));

            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseAuth.getCurrentUser()
                        .getUid()).setValue(new UserData(emailET.getText().toString().trim(), mobileNoET.getText().toString().trim(), interestsET.getText().toString().trim()));

            }
        });

        mobileNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseAuth.getCurrentUser()
                        .getUid()).setValue(new UserData(emailET.getText().toString().trim(), mobileNoET.getText().toString().trim(), interestsET.getText().toString().trim()));

            }
        });

        interestsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseAuth.getCurrentUser()
                        .getUid()).setValue(new UserData(emailET.getText().toString().trim(), mobileNoET.getText().toString().trim(), interestsET.getText().toString().trim()));

            }
        });



        /*
        name = (TextView) findViewById(R.id.company_profile_name);
        brief = (TextView) findViewById(R.id.company_profile_short_bio);
        specialization = (TextView) findViewById(R.id.company_specialization);
        workDays = (TextView) findViewById(R.id.company_work_days);
        */

       // specializationET = (TextInputEditText) findViewById(R.id.company_specialization_ET);
       // workDaysET = (TextInputEditText) findViewById(R.id.company_work_days_ET);
/*
        specializationPencilIV = (ImageView) findViewById(R.id.specializationPencilIV);
      //  workDaysPencilIV = (ImageView) findViewById(R.id.workDaysPencilIV);

        ratingBar = (RatingBar) findViewById(R.id.company_rating);
/*
        if (userData != null) {
            name.setText(userData.name);
            brief.setText(userData.brief);
            specialization.setText(userData.brief);
            workDays.setText(userData.brief);
            ratingBar.setMax(5);
            ratingBar.setProgress(Integer.parseInt(userData.rate));

        }


        specializationPencilIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (specialization.getVisibility() == View.VISIBLE) {
                    specializationPencilIV.setImageResource(R.drawable.pencil_icon);
                    specialization.setVisibility(View.GONE);
                    specializationET.setVisibility(View.VISIBLE);
                }
                else {
                    specializationPencilIV.setImageResource(R.drawable.correct_mark);
                    specialization.setVisibility(View.VISIBLE);
                    specializationET.setVisibility(View.GONE);
                }
            }
        });

        workDaysPencilIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workDays.getVisibility() == View.VISIBLE) {
                    workDaysPencilIV.setImageResource(R.drawable.pencil_icon);
                    workDays.setVisibility(View.GONE);
                    workDaysET.setVisibility(View.VISIBLE);
                }
                else {
                    workDaysPencilIV.setImageResource(R.drawable.correct_mark);
                    workDays.setVisibility(View.VISIBLE);
                    workDaysET.setVisibility(View.GONE);
                }
            }
        });
        */
    }

    private void initDrawer(){

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
                        startActivity(new Intent(getBaseContext(), About.class));
                        navigationView.getMenu().getItem(4).setChecked(true);
                        return true;

                    case R.id.nav_contact:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        navigationView.getMenu().getItem(5).setChecked(true);
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

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

//Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

}