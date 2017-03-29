package com.exapply.app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.mysterybuilders.royalhayah.ApplicationHelper.Constants;
//import com.mysterybuilders.royalhayah.Utilities.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MakeAppointementActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mNameTextView;
    private  EditText mPhoneTextView;
    private EditText mEmailTextView;
    private DatePicker dpResult;

    int mStatusCode;
    ProgressDialog pDialog;
    String mUserName ;
    String mEmail ;
    String mPhone;
    String mComplain ;
    String mTime;
    String mDate ;

    private int year;
    private int month;
    private int day;
Calendar myCalendar;
    static final int DATE_DIALOG_ID = 999;
    private EditText mComplainEditText;
    private AppCompatButton mSendButton;
    private TextInputLayout mNameTextInputLayout;
    private TextInputLayout mPhoneTextInputLayout;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mComplainTextInputLayout;
    private AppCompatTextView mDateAppCompatTextView;
    private AppCompatTextView mTimeAppCompatTextView;
   private String mCategoryEn;
    String mHeight;
    String mWeight;
    String mAge;
    String mGender;
    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

if (getIntent().getExtras()!=null){
mCategoryEn = getIntent().getStringExtra(Constants.Extra.TAG_OBESITY_CATEGORY_EN);
    mSendButton.setOnClickListener(this);

}


    }
    private void initView(){
        myCalendar = Calendar.getInstance();
        mSharedPreferences = getSharedPreferences(Constants.Extra.TAG_SHARED_PREFS,MODE_PRIVATE);
        mAge = mSharedPreferences.getString(Constants.Extra.TAG_AGE,"");
        mHeight = mSharedPreferences.getString(Constants.Extra.TAG_HEIGHT,"");
        mWeight = mSharedPreferences.getString(Constants.Extra.TAG_WEIGHT,"");
        mGender = mSharedPreferences.getString(Constants.Extra.TAG_GENDER,"Male");
        mComplainEditText =(EditText) findViewById(R.id.input_name);
        mSendButton = (AppCompatButton) findViewById(R.id.send_button);
        mEmailTextView =(EditText) findViewById(R.id.email_edit_text);
        mPhoneTextView =(EditText) findViewById(R.id.phone_edit_text);
        mNameTextView =(EditText)findViewById(R.id.name_text_view);

        mNameTextInputLayout =(TextInputLayout)findViewById(R.id.name_text_input);

        mEmailTextInputLayout =(TextInputLayout) findViewById(R.id.email_text_layout);
        mPhoneTextInputLayout =(TextInputLayout)findViewById(R.id.phone_text_layout);
        mComplainTextInputLayout =(TextInputLayout) findViewById(R.id.to_text_input_layout);
        mDateAppCompatTextView = (AppCompatTextView) findViewById(R.id.date_text_view);
        mTimeAppCompatTextView = (AppCompatTextView) findViewById(R.id.time_text_view);
        mDateAppCompatTextView.setOnClickListener(this);
        mTimeAppCompatTextView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_button:
                if (Utils.isNetworkConnected(MakeAppointementActivity.this)) {
                     mUserName = mNameTextView.getText().toString();
                     mEmail = mEmailTextView.getText().toString();
                     mPhone = mPhoneTextView.getText().toString();
                     mComplain = mComplainEditText.getText().toString();
                     mTime = mTimeAppCompatTextView.getText().toString();
                     mDate = mDateAppCompatTextView.getText().toString();
                    if (mUserName.trim().equalsIgnoreCase("")) {
                        mNameTextInputLayout.setErrorEnabled(true);
                        mNameTextInputLayout.setError(getResources().getString(R.string.name_error));
                    } else if (!Utils.CheckEmailAddress(mEmail)) {
                        mEmailTextInputLayout.setErrorEnabled(true);
                        mEmailTextInputLayout.setError(getResources().getString(R.string.email_error));
                    } else if (!Utils.validatePhoneNumber(mPhone)) {
                        mPhoneTextInputLayout.setErrorEnabled(true);
                        mPhoneTextInputLayout.setError(getResources().getString(R.string.phone_error));
                    } else if (mComplain.trim().equalsIgnoreCase("")) {
                        mComplainTextInputLayout.setErrorEnabled(true);
                        mComplainTextInputLayout.setError(getResources().getString(R.string.complain_error));
                    } else if (mTime.trim().equalsIgnoreCase("")) {
                        Toast.makeText(MakeAppointementActivity.this, getResources().getString(R.string.time_error), Toast.LENGTH_SHORT).show();

                    } else if (mDate.trim().equalsIgnoreCase("")) {
                        Toast.makeText(MakeAppointementActivity.this, getResources().getString(R.string.date_error), Toast.LENGTH_SHORT).show();

                    } else {
                        String mUrl = "http://clinicalbariatricapp.com/webservices.asmx/BookAppointment?Name="+mUserName+"&Email="+mEmail+"&Phone="+mPhone+"&Query="+mComplain+"&Height="+mHeight+"&Weight="+mWeight+"&Gender="+mGender+"&Age="+mAge+"&Obesity="+mCategoryEn+"&Date="+mDate+"&Time="+mTime;
makeAppointement(mUrl);
                    }


                } else {
                    Toast.makeText(MakeAppointementActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.date_text_view:
                // TODO Auto-generated method stub
                new DatePickerDialog(MakeAppointementActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.time_text_view:
                showTimePicker(mTimeAppCompatTextView);
                break;

        }
    }

    // display current date
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateAppCompatTextView.setText(sdf.format(myCalendar.getTime()));
    }


    //code from: http://developer.android.com/guide/topics/ui/controls/pickers.html
    public void showTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @SuppressLint("ValidFragment")
    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = String.valueOf(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(hourOfDay).append(":").append(minute));
            mTimeAppCompatTextView.setText(time);
        }
    }
    private void makeAppointement(String mUrl ) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismiss();

                        if (mStatusCode == 200) {
                            String mResponse="";
                            try{

                                JSONObject mJsonObject =new JSONObject(response);
                                JSONArray mJsonArray = mJsonObject.getJSONArray("Data1");
                                JSONObject mResultJsonObject = mJsonArray.getJSONObject(0);
                                String mResult = mResultJsonObject.getString("result");
                                if (mResult.trim().equalsIgnoreCase("1")){
Toast.makeText(MakeAppointementActivity.this,getResources().getString(R.string.appointement_success),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MakeAppointementActivity.this,getResources().getString(R.string.appointement_fail),Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception e){

                            }

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pDialog.dismiss();

                    }
                })

        {
            //            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("x1",mUserName);
//                params.put("x2",mPhoneNumber);
//                params.put("x3",mEmail);
//
//                return params;
//            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(MakeAppointementActivity.this);
        requestQueue.add(stringRequest);

        pDialog = new ProgressDialog(MakeAppointementActivity.this);
        pDialog.setMessage(getResources().getString(R.string.wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

    }
}

