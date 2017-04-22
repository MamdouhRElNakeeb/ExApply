package com.exapply.mobapp.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.exapply.mobapp.Constants;
import com.exapply.mobapp.Objects.CommentData;
import com.exapply.mobapp.Objects.ExperienceData;
import com.exapply.mobapp.R;
import com.exapply.mobapp.adapter.HomeViewPagerAdapter;
import com.exapply.mobapp.fragment.CommentsFragments;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 2/28/17.
 */

public class ExperienceDetails extends AppCompatActivity {


    private static final float BLUR_RADIUS = 25f;
    ImageView eventImgIV;
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

    StorageReference photoReference;

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
        photoReference = FirebaseStorage.getInstance().getReference();

        initExDetails();

        viewPager = (ViewPager) findViewById(R.id.commentProposalVP);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.commentProposalTabs);
        tabLayout.setupWithViewPager(viewPager);


        commentFab = (FloatingActionButton) findViewById(R.id.comment_fab);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        eventImgIV = (ImageView) findViewById(R.id.eventImageIV);

        int eventImgPlaceholder;
        switch (experienceData.category){
            case "Travel":
                eventImgPlaceholder = Constants.categoryImgs[0];
                break;
            case "Sport":
                eventImgPlaceholder = Constants.categoryImgs[1];
                break;
            case "Charity":
                eventImgPlaceholder = Constants.categoryImgs[2];
                break;
            case "Parties":
                eventImgPlaceholder = Constants.categoryImgs[3];
                break;
            case "Courses":
                eventImgPlaceholder = Constants.categoryImgs[4];
                break;
            default:
                eventImgPlaceholder = Constants.categoryImgs[0];
                break;
        }


        eventImgIV.setImageBitmap(blur(BitmapFactory.decodeResource(getResources(), eventImgPlaceholder)));

        photoReference = photoReference.child("eventImages/" + experienceData.exID + ".jpg");

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(photoReference)
                .asBitmap()
                .placeholder(eventImgPlaceholder)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        eventImgIV.setImageBitmap(resource);
                    }
                });


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
