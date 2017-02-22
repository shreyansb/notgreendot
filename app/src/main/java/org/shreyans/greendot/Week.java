package org.shreyans.greendot;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by shreyans on 9/13/14.
 */
public class Week {
    public int weekNumber;
    public int currentWeek;
    public ArrayList<Integer> dots;
    public Context context;

    private static final String sharedPrefName = "dotsDB";
    private static final String defaultWeekAsString = "0,0,0";
    public static final int weekLength = 3;

    public Week(Context context, int weekNumber, int currentWeek) {
        this.context = context;
        this.weekNumber = weekNumber;
        this.currentWeek = currentWeek;
        this.dots = getDots();
    }

    public void saveDot(int dotNumber, int dotValue) {
        SharedPreferences db = getSharedPreferences(context);
        String key = getWeekKey(weekNumber);

        // get the current saved values from the db and update them
        ArrayList<Integer> currentDotsForWeek = getDots();

        if (dotValue == 1) {
            for (int i = 0; i < weekLength)
        }
        currentDotsForWeek.set(dotNumber, dotValue);

        // convert the list into a string
        String weekAsString = TextUtils.join(",", currentDotsForWeek);

        db.edit()
                .putString(key, weekAsString)
                .commit();

        getDots();
    }

    public ArrayList<Integer> getDots() {
        SharedPreferences db = getSharedPreferences(context);

        // get the week's data from the db
        String key = getWeekKey(weekNumber);
        String weekAsString = db.getString(key, defaultWeekAsString);

        // convert the stringified list into an ArrayList and return it
        StringTokenizer st = new StringTokenizer(weekAsString, ",");

        ArrayList<Integer> weekAsList = new ArrayList<Integer>();
        while (st.hasMoreTokens()) {
            weekAsList.add(Integer.parseInt(st.nextToken()));
        }

        return weekAsList;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    private static String getWeekKey(int weekNumber) {
        return "dots:week:" + weekNumber;
    }
}