package com.exapply.app.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exapply.app.Objects.CommentData;
import com.exapply.app.Objects.ExperienceData;
import com.exapply.app.R;
import com.exapply.app.adapter.HomeViewPagerAdapter;
import com.exapply.app.fragment.CommentsFragments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 2/28/17.
 */

public class ExperienceDetails extends AppCompatActivity {

    FloatingActionButton commentFab;

    CoordinatorLayout coordinatorLayout;

    TextView titleTV, descriptionTV, categoryTV, locationTV, userNameTV, labelSeparator;
    BottomSheetBehavior behavior;
    boolean showFAB = true;

    EditText commentET, priceET;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    CommentsFragments commentsFragment, proposalFragment;

    String type;
    ExperienceData experienceData;

    SharedPreferences prefs;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_details);

        prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);

        experienceData = getIntent().getExtras().getParcelable("exItem");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        collapsingToolbarLayout.setTitle(experienceData.title);

        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initExDetails();

        viewPager = (ViewPager) findViewById(R.id.commentProposalVP);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.commentProposalTabs);
        tabLayout.setupWithViewPager(viewPager);


        commentFab = (FloatingActionButton) findViewById(R.id.comment_fab);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        // To handle FAB animation upon entrance and exit
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_shrink);

        commentFab.setVisibility(View.VISIBLE);
        commentFab.startAnimation(growAnimation);

        final LinearLayout bottomSheetLL = (LinearLayout) findViewById(R.id.bottomSheetLL);
        bottomSheetLL.setVisibility(View.INVISIBLE);

        final View bottomSheet = coordinatorLayout.findViewById(R.id.comment_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        commentET = (EditText) findViewById(R.id.commentContent);
        priceET = (EditText) findViewById(R.id.commentPrice);

        if (prefs.getString("type", "").equals("planner")){
            type = "proposals";
            priceET.setVisibility(View.VISIBLE);
        }
        else {
            type = "comments";
            priceET.setVisibility(View.GONE);
        }

        commentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    commentFab.startAnimation(shrinkAnimation);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    String comment = commentET.getText().toString().trim();
                    String price = priceET.getText().toString().trim();
                    if(comment.isEmpty() && !price.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Comment is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (!comment.isEmpty()){
                        postNewComment(type);
                    }
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (showFAB) {
                            commentFab.startAnimation(shrinkAnimation);
                        }
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        showFAB = true;
                        commentFab.setImageResource(R.drawable.ic_discuss);
                        commentFab.setVisibility(View.VISIBLE);
                        commentFab.startAnimation(growAnimation);
                        bottomSheetLL.setVisibility(View.INVISIBLE);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetLL.setVisibility(View.VISIBLE);
                        commentFab.setImageResource(R.drawable.ic_done);
                        showFAB = false;
                        break;
                }

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

    }

    private void initExDetails(){
        titleTV = (TextView) findViewById(R.id.exTitle);
        descriptionTV = (TextView) findViewById(R.id.exDescription);
        categoryTV = (TextView) findViewById(R.id.exCategory);
        locationTV = (TextView) findViewById(R.id.exLocation);
        userNameTV = (TextView) findViewById(R.id.event_owner_name);

        labelSeparator = (TextView) findViewById(R.id.label_separator);

        titleTV.setText(experienceData.title);
        descriptionTV.setText(experienceData.description);
        categoryTV.setText(experienceData.category);
        locationTV.setText(experienceData.location);
        userNameTV.setText(experienceData.userName);
    }

    private void setupViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        commentsFragment = new CommentsFragments(experienceData, "comments");
        proposalFragment = new CommentsFragments(experienceData, "proposals");
        adapter.addFragment(commentsFragment, "Comments");
        adapter.addFragment(proposalFragment, "Proposals");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        labelSeparator.setText("Comments");
                        break;
                    case 1:
                        labelSeparator.setText("Proposals");
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // [START write_fan_out]
    private void postNewComment(String commOrProp) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        String key = mDatabase.child("experiences").child(experienceData.category).child(experienceData.exID).child(commOrProp).push().getKey();
        CommentData commentData = new CommentData(commentET.getText().toString().trim(),
                priceET.getText().toString().trim(),
                prefs.getString("name", ""),
                currentDateTimeString,
                key, experienceData.exID);

        Map<String, Object> postValues = commentData.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/experiences/" + experienceData.category + "/" + experienceData.exID + "/" + commOrProp + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do your stuff here, eg: finish();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
