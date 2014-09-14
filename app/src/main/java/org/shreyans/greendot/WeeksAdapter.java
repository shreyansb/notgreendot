package org.shreyans.greendot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shreyans on 9/13/14.
 * Thanks to: https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class WeeksAdapter extends ArrayAdapter<Week> {

    Context context;

    public WeeksAdapter(Context context, ArrayList<Week> weeks) {
        super(context, R.layout.weekly_row, weeks);
        this.context = context;
    }

    @Override
    public View getView(int position, View weekView, ViewGroup parent) {
        // Get the data item for this position
        Week week = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (weekView == null) {
            weekView = LayoutInflater.from(getContext()).inflate(R.layout.weekly_row, parent, false);
        }

        if (week.weekNumber == week.currentWeek) {
            TextView label = (TextView) weekView.findViewById(R.id.weekLabel);
            label.setText("this week");
        }

        int[] dots = new int[] {R.id.circle1, R.id.circle2, R.id.circle3, R.id.heart};
        for (int dotNumber = 0; dotNumber < dots.length; dotNumber++) {
            ImageView dot = (ImageView) weekView.findViewById(dots[dotNumber]);
            new toggleImageView(context, dot, dotNumber, week);
        }

        // Lookup view for data population
        return weekView;
    }
}

/*
`toggleImageView` sets a click listener on the given :image to make
it toggle between the :emptyImage and :filledImage
 */
class toggleImageView {
    private Context context;
    private ImageView image;
    private Week week;

    private int state;
    private int dotNumber;
    private Boolean isLast = false;

    private final int circleEmpty = R.drawable.circle_empty;
    private final int circleFilled = R.drawable.circle_filled;
    private final int heartEmpty = R.drawable.heart_empty;
    private final int heartFilled = R.drawable.heart_filled;

    public toggleImageView(Context context,
                           ImageView image,
                           int dotNumber,
                           Week week) {
        this.context = context;
        this.image = image;
        this.dotNumber = dotNumber;
        this.week = week;
        this.isLast = week.days.size() == dotNumber + 1;
        addImageClickListener();
        setAndSaveState(week.days.get(dotNumber));
    }

    private void addImageClickListener() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAndSaveState(1 - state);
            }
        });
    }

    /* update the image in the UI, as well as the database,
    but only if the state change is allowed to happen, i.e.
    we aren't setting the heart before the others, etc.
     */
    private void setAndSaveState(int newState) {
        Boolean allowed = isStateChangeAllowed(newState);
        if (!allowed) {
            return;
        }

        if (newState == 1) {
            if (isLast) {
                image.setImageResource(heartFilled);
            } else {
                image.setImageResource(circleFilled);
            }
        } else {
            if (isLast) {
                image.setImageResource(heartEmpty);
            } else {
                image.setImageResource(circleEmpty);
            }
        }
        state = newState;
        week.days.set(dotNumber, state);
        WeeksStorage.saveDotForWeek(context, dotNumber, state, week.weekNumber);
    }

    private Boolean isStateChangeAllowed(int newState) {
        if (newState == 1) {
            if (dotNumber == 0) {
                return true;
            } else {
                return week.days.get(dotNumber - 1) == 1;
            }
        } else {
            if (dotNumber == week.days.size() - 1) {
                return true;
            } else {
                return week.days.get(dotNumber + 1) == 0;
            }
        }
    }
}