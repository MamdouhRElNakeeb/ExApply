package ml.exapply.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exapply.app.R;
import ml.exapply.app.fragment.ScheduleFragment;


/**
 * Created by mamdouhelnakeeb on 3/24/17.
 */

public class PlayedWorkoutsAdapter extends BaseAdapter {
    // Variables
    private Context mContext;
    private static ViewHolder mHolder;

    private static class ViewHolder {
        TextView mTitle;
        TextView mAbout;
        ImageView mImageView;
        View mDivider;
    }

    // Constructor
    public PlayedWorkoutsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (ScheduleFragment.mNumEventsOnDay != 0 || ScheduleFragment.mNumEventsOnDay != -1) {
            return ScheduleFragment.mNumEventsOnDay;
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.schedule_played_workout_item, parent, false);

            mHolder = new ViewHolder();

            if (convertView != null) {
                // FindViewById
                mHolder.mTitle = (TextView) convertView.findViewById(R.id.saved_event_title_textView);
                mHolder.mAbout = (TextView) convertView.findViewById(R.id.saved_event_about_textView);
                mHolder.mImageView = (ImageView) convertView.findViewById(R.id.saved_event_imageView);
                convertView.setTag(mHolder);
            }
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        // Animates in each cell when added to the ListView
        Animation animateIn = AnimationUtils.loadAnimation(mContext, R.anim.listview_top_down);
        if (convertView != null && animateIn != null) {
            convertView.startAnimation(animateIn);
        }

        if (mHolder.mTitle != null) {
            setEventTitle();
        }

        if (mHolder.mAbout != null) {
            setEventAbout();
        }

        if (mHolder.mImageView != null) {
            setEventImage();

        }

        return convertView;
    }

    private void setEventTitle() {
        mHolder.mTitle.setText("Workout 1");
    }

    private void setEventAbout() {
        mHolder.mAbout.setText("Pushup, Dip, Squat");
    }

    private void setEventImage() {
        // Set event item image (bitmap, drawable, uri...)
        //mHolder.mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_calendar));
    }
}
