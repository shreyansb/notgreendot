package org.shreyans.greendot;

import java.util.ArrayList;

/**
 * Created by shreyans on 9/13/14.
 */
public class Week {
    public int weekNumber;
    public int currentWeek;
    public ArrayList<Integer> days;

    public Week(int weekNumber, ArrayList<Integer> days, int currentWeek) {
        this.weekNumber = weekNumber;
        this.days = days;
        this.currentWeek = currentWeek;
    }
}