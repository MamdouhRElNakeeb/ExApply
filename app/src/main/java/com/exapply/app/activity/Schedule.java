package com.exapply.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.MonthDisplayHelper;
import android.widget.CalendarView;

import com.exapply.app.R;
import com.exapply.app.fragment.ScheduleFragment;


/**
 * Created by mamdouhelnakeeb on 3/23/17.
 */

public class Schedule extends AppCompatActivity {


    CalendarView scheduleCalendar;
    MonthDisplayHelper monthDisplayHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        //scheduleCalendar = (CalendarView) findViewById(R.id.scheduleCalendar);

        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.main_container, new ScheduleFragment());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }

    }

}
