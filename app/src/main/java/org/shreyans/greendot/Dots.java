package org.shreyans.greendot;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Dots extends Activity {

    ListView mainListView;
    int numWeeks = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dots);

        // construct data source
        int currentWeek = getCurrentWeek();
        ArrayList<Week> weeksArray = new ArrayList<Week>();
        for (int i=0; i < numWeeks; i++) {
            int weekNumber = currentWeek + i;
            ArrayList<Integer> dots = WeeksStorage.getDotsForWeek(this, weekNumber);
            WeeksStorage.getDotsForWeek(this, weekNumber);
            Week w = new Week(currentWeek + i, dots, currentWeek);
            weeksArray.add(i, w);
        }

        // create adapter to convert the array to views
        WeeksAdapter adapter = new WeeksAdapter(this, weeksArray);

        // attach the adapter to a ListView
        mainListView = (ListView) findViewById(R.id.mainListView);
        mainListView.setAdapter(adapter);
        mainListView.setDivider(null);
    }

    private int getCurrentWeek() {
        Calendar cal = new GregorianCalendar();
        Date date = new Date();
        cal.setTime(date);
        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        return currentWeek;
    }
}