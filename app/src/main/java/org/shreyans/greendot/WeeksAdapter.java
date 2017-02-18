package org.shreyans.greendot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shreyans on 9/13/14.
 * Thanks to: https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class WeeksAdapter extends ArrayAdapter<Week> {

    public WeeksAdapter(Context context, ArrayList<Week> weeks) {
        super(context, R.layout.weekly_row, weeks);
    }

    @Override
    public View getView(int position, View weekView, ViewGroup parent) {
        // Get the data item for this position
        Week week = getItem(position);
        week = new Week(getContext(), week.weekNumber, week.currentWeek);

        // Check if an existing view is being reused, otherwise inflate the view
        if (weekView == null) {
            weekView = LayoutInflater.from(getContext()).inflate(R.layout.weekly_row, parent, false);
        }

        if (week.weekNumber == week.currentWeek) {
            TextView label = (TextView) weekView.findViewById(R.id.weekLabel);
            label.setText("this week");
        }

        int[] dots = new int[] {R.id.circle1, R.id.circle2, R.id.circle3, R.id.heart};
        //ImageView heart = (ImageView) weekView.findViewById(dots[dots.length]);
        for (int dotNumber = 0; dotNumber < dots.length-1; dotNumber++) {
            ImageView dot = (ImageView) weekView.findViewById(dots[dotNumber]);
            new toggleImageView(dot, dotNumber, week, weekView);
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
    private ImageView image;
    private Week week;
    private View weekView;

    private int state;
    private int dotNumber;
    private Boolean isLast = false;

    private final int circleEmpty = R.drawable.circle_empty;
    private final int circleFilled = R.drawable.circle_filled;
    private final int heartEmpty = R.drawable.heart_empty;
    private final int heartFilled = R.drawable.heart_filled;

    public toggleImageView(ImageView image,
                           int dotNumber,
                           Week week,
                           View weekView) {
        this.image = image;
        this.dotNumber = dotNumber;
        this.week = week;
        this.weekView = weekView;
        this.isLast = Week.weekLength - 1 == dotNumber + 1;

        addImageClickListener();
        setAndSaveState(week.dots.get(dotNumber));
    }

    private void addImageClickListener() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // `1 - state` is a trick to toggle between 0 and 1
                setAndSaveState(1 - state);
            }
        });
    }

    /* update the image in the UI, as well as the database
     */
    private void setAndSaveState(int newState) {
        // update the database
        state = newState;
        // TODO update how saveDot works to also update all the dots before this one
        week.saveDot(dotNumber, state);

        // update the tapped dot and all dots before it
        int updatedResource;
        if (newState == 1) {
            updatedResource = circleFilled;
            //image.setImageResource(circleFilled);
        } else {
            updatedResource = circleEmpty;
        }

        for (int i = 0; i <= dotNumber; i++) {
            ImageView dot = (ImageView) this.weekView.findViewById(i);
            dot.setImageResource(updatedResource);
        }

        // also update the heart
        if (this.isLast) {
            ImageView heart = (ImageView) this.weekView.findViewById(week.weekLength - 1);

            if (newState == 1) {
                heart.setImageResource(heartFilled);
            } else {
                heart.setImageResource(heartEmpty);
            }
        }
    }

    private Boolean isStateChangeAllowed(int newState) {
        Week freshWeek = new Week(week.context, week.weekNumber, week.currentWeek);
        if (newState == 1) {
            if (dotNumber == 0) {
                return true;
            } else {
                return freshWeek.dots.get(dotNumber - 1) == 1;
            }
        } else {
            if (dotNumber == freshWeek.dots.size() - 1) {
                return true;
            } else {
                return freshWeek.dots.get(dotNumber + 1) == 0;
            }
        }
    }
}