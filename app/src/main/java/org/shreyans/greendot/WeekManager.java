package org.shreyans.greendot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by shreyans on 9/14/14.
 */
public final class WeekManager {
    private static Context context;
    private static final String sharedPrefName = "startingWeekDB";
    private static final String startingWeekKey = "startingWeek";

    public WeekManager(Context context) {
        this.context = context;
    }

    public int getStartingWeek() {
        SharedPreferences db = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        String startingWeek = db.getString(startingWeekKey, null);
        if (startingWeek == null) {
            int currentWeek = getCurrentWeek() - 1;
            db.edit()
                    .putString(startingWeekKey, Integer.toString(currentWeek))
                    .apply();
            return currentWeek;
        } else {
            return Integer.parseInt(startingWeek);
        }
    }

    public int getCurrentWeek() {
        Calendar cal = new GregorianCalendar();
        Date date = new Date();
        cal.setTime(date);
        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        return currentWeek;
    }
}
