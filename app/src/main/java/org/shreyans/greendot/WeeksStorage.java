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
public final class WeeksStorage {
    private static final String sharedPrefName = "dotsDB";
    private static final String defaultWeekAsString = "0,0,0,0";
    private static final int weekLength = 4;

    private WeeksStorage() {}

    public static void saveDotForWeek(Context context, int dotNumber, int dotValue, int weekNumber) {
        Log.v("saveDotForWeek", "" + weekNumber + ":" + dotNumber + ":" + dotValue);

        SharedPreferences db = getSharedPreferences(context);
        SharedPreferences.Editor edit = db.edit();
        String key = getWeekKey(weekNumber);

        // get the current saved values from the db and update them
        ArrayList<Integer> currentDotsForWeek = getDotsForWeek(context, weekNumber);

        currentDotsForWeek.set(dotNumber, dotValue);

        // convert the list into a string
        String weekAsString = TextUtils.join(",", currentDotsForWeek);

        Log.v("saveDotForWeek, key =>", key);
        Log.v("saveDotForWeek: weekAsString => ", "" + weekAsString);

        edit.putString(key, weekAsString);
        edit.commit();
    }

    public static ArrayList<Integer> getDotsForWeek(Context context, int weekNumber) {
        Log.v("getDotsForWeek, weekNumer => ", "" + weekNumber);

        SharedPreferences db = getSharedPreferences(context);

        // get the week's data from the db
        String key = getWeekKey(weekNumber);
        String weekAsString = db.getString(key, defaultWeekAsString);

        Log.v("getDotsForWeek, key =>", key);
        Log.v("getDotsForWeek, weekAsString => ", "" + weekAsString);

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
        return "week:" + weekNumber;
    }
}