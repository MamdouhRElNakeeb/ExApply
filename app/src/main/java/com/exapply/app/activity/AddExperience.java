package com.exapply.app.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exapply.app.Objects.ExperienceData;
import com.exapply.app.Objects.UserData;
import com.exapply.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 2/28/17.
 */

public class AddExperience extends AppCompatActivity {

    TextView saveExBtn;
    TextInputEditText titleET, descriptionET, locationET;
    UserData userData;

    AppCompatSpinner categoryS;

    Toolbar toolbar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_experience);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post Event");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        titleET = (TextInputEditText) findViewById(R.id.titleET);
        descriptionET = (TextInputEditText) findViewById(R.id.descriptionET);
        categoryS = (AppCompatSpinner) findViewById(R.id.categoryS);
        locationET = (TextInputEditText) findViewById(R.id.locationET);
        //saveExBtn = (TextView) findViewById(R.id.saveExBtn);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categoryS.setAdapter(adapter);


        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setUserData();

        /*
        saveExBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewExperience();
                finish();
            }
        });
        */
    }

    // [START write_fan_out]
    private void postNewExperience() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String title = titleET.getText().toString().trim();
        String description = descriptionET.getText().toString().trim();
        String category = categoryS.getSelectedItem().toString().trim();
        String location = locationET.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || category.isEmpty()){
            Toast.makeText(this, "Missing Details", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = mDatabase.child("/users/" + mFirebaseUser.getUid() + "/posts/").push().getKey();
        ExperienceData experienceData = new ExperienceData(mFirebaseUser.getUid(),
                userData.name,
                key,
                titleET.getText().toString().trim(),
                descriptionET.getText().toString().trim(),
                categoryS.getSelectedItem().toString().trim(),
                locationET.getText().toString().trim());

        Map<String, Object> postValues = experienceData.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/experiences/" + key, postValues);
        childUpdates.put("/users/" + mFirebaseUser.getUid() + "/posts/" + key, postValues);
        childUpdates.put("/experiences/" + categoryS.getSelectedItem().toString().trim() + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        finish();
    }
    // [END write_fan_out]

    private void setUserData(){
        SharedPreferences prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);
        userData = new UserData(prefs.getString("name", ""), mFirebaseUser.getEmail(), prefs.getString("mobile", ""), prefs.getString("type", ""));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do your stuff here, eg: finish();
                finish();
                return true;

            case R.id.action_menu_done:
                postNewExperience();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}