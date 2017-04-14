package com.exapply.app.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.exapply.app.Objects.ExperienceData;
import com.exapply.app.Objects.UserData;
import com.exapply.app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 2/28/17.
 */

public class AddExperience extends AppCompatActivity {

    TextView dateTV, timeTV, uploadImageCancelTV, uploadImageTV;
    EditText titleET, descriptionET, locationET, minNo, maxNo, keywordsET;
    UserData userData;

    ImageView uploadImgIV;
    RelativeLayout uploadImageRL;


    AppCompatSpinner categoryS, privacyS;
    AppCompatButton createEventBtn;

    DateFormat formatDate = DateFormat.getDateInstance();
    DateFormat formatTime = DateFormat.getTimeInstance();
    Calendar dateCalender = Calendar.getInstance();
    Calendar timeCalender = Calendar.getInstance();

    private Bitmap bitmap = null;

    public Uri photoPath;

    Toolbar toolbar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    StorageReference photoReferenece;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post Event");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        uploadImageRL = (RelativeLayout) findViewById(R.id.upload_imageRL);
        uploadImageTV = (TextView) findViewById(R.id.upload_imageTV);
        uploadImageCancelTV = (TextView) findViewById(R.id.image_upload_cancelTV);
        uploadImgIV = (ImageView) findViewById(R.id.event_image_upload);

        titleET = (EditText) findViewById(R.id.titleET);
        descriptionET = (EditText) findViewById(R.id.descriptionET);
        categoryS = (AppCompatSpinner) findViewById(R.id.categoryS);
        privacyS = (AppCompatSpinner) findViewById(R.id.privacyS);
        locationET = (EditText) findViewById(R.id.locationET);
        keywordsET = (EditText) findViewById(R.id.keywordsTV);
        minNo = (EditText) findViewById(R.id.min_no);
        maxNo = (EditText) findViewById(R.id.max_no);
        dateTV = (TextView) findViewById(R.id.dateTV);
        timeTV = (TextView) findViewById(R.id.timeTV);
        //saveExBtn = (TextView) findViewById(R.id.saveExBtn);

        createEventBtn = (AppCompatButton) findViewById(R.id.event_create_btn);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> privacyAdapter = ArrayAdapter.createFromResource(this, R.array.privacy_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        categoryS.setAdapter(adapter);
        privacyS.setAdapter(privacyAdapter);


        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        photoReferenece = FirebaseStorage.getInstance().getReference();

        setUserData();


        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddExperience.this, d, dateCalender.get(Calendar.YEAR), dateCalender.get(Calendar.MONTH), dateCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(AddExperience.this, t, timeCalender.get(Calendar.HOUR_OF_DAY), timeCalender.get(Calendar.MINUTE), false).show();
            }
        });

        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewExperience();
            }
        });

        uploadImageRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(AddExperience.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddExperience.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                else {
                    showFileChooser();
                }
            }
        });

        uploadImageCancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImgIV.setImageResource(R.drawable.image_upload);
                uploadImageCancelTV.setVisibility(View.GONE);
                uploadImageTV.setVisibility(View.VISIBLE);
                bitmap = null;
            }
        });

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

        //showDialog();

        String key = mDatabase.child("/users/" + mFirebaseUser.getUid() + "/posts/").push().getKey();
        ExperienceData experienceData = new ExperienceData(mFirebaseUser.getUid(),
                userData.name,
                key,
                titleET.getText().toString().trim(),
                descriptionET.getText().toString().trim(),
                categoryS.getSelectedItem().toString().trim(),
                locationET.getText().toString().trim(),
                dateTV.getText().toString().trim(),
                timeTV.getText().toString().trim(),
                keywordsET.getText().toString().trim(),
                privacyS.getSelectedItem().toString().trim(),
                maxNo.getText().toString().trim(),
                minNo.getText().toString().trim());
/*
        String userID, String userName, String userImg, String exID, String title,
                String eventImg, String description, String category, String location, String eventDate,
                String keywords, String privacy, String maxNo, String minNo
                */
        Map<String, Object> postValues = experienceData.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/experiences/" + key, postValues);
        childUpdates.put("/users/" + mFirebaseUser.getUid() + "/posts/" + key, postValues);
        childUpdates.put("/posts/" + categoryS.getSelectedItem().toString().trim() + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);


        if (bitmap != null) {
            photoReferenece = photoReferenece.child("eventImages/" + key + ".jpg");
            photoReferenece.putFile(photoPath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //hideDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //hideDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddExperience.this);
                            builder.setMessage(e.getMessage())
                                    .setTitle("Error!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return;
                        }
                    });
        }
        finish();
    }
    // [END write_fan_out]

    private void setUserData(){
        SharedPreferences prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);
        userData = new UserData(prefs.getString("name", ""), mFirebaseUser.getEmail(), prefs.getString("mobile", ""), prefs.getString("type", ""));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            showFileChooser();
        }
        else {
            Toast.makeText(getBaseContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoPath = data.getData();
            try {

                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), photoPath);

                uploadImgIV.setImageBitmap(bitmap);
                uploadImageTV.setVisibility(View.GONE);
                uploadImageCancelTV.setVisibility(View.VISIBLE);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateCalender.set(Calendar.YEAR, year);
            dateCalender.set(Calendar.MONTH, monthOfYear);
            dateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateTV.setText(formatDate.format(dateCalender.getTime()));
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeCalender.set(Calendar.MINUTE, minute);
            timeTV.setText(formatTime.format(timeCalender.getTime()));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.done_menu, menu);

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}