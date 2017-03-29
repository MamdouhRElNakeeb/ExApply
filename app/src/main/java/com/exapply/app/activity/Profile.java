package com.exapply.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.exapply.app.Objects.UserData;
import com.exapply.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mamdouhelnakeeb on 3/3/17.
 */

public class Profile extends AppCompatActivity {

    TextView name, brief, specialization, workDays;
    TextInputEditText specializationET, workDaysET;
    ImageView specializationPencilIV, workDaysPencilIV;
    RatingBar ratingBar;

    // [START declare_auth]
    private FirebaseAuth mFirebaseAuth;
    // [END declare_auth]
    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        UserData userData = getIntent().getExtras().getParcelable("userData");

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        name = (TextView) findViewById(R.id.company_profile_name);
        brief = (TextView) findViewById(R.id.company_profile_short_bio);
        specialization = (TextView) findViewById(R.id.company_specialization);
        workDays = (TextView) findViewById(R.id.company_work_days);

       // specializationET = (TextInputEditText) findViewById(R.id.company_specialization_ET);
       // workDaysET = (TextInputEditText) findViewById(R.id.company_work_days_ET);

        specializationPencilIV = (ImageView) findViewById(R.id.specializationPencilIV);
      //  workDaysPencilIV = (ImageView) findViewById(R.id.workDaysPencilIV);

        ratingBar = (RatingBar) findViewById(R.id.company_rating);

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
    }
}