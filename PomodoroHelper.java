package com.example.admin.pomodoroproductivity;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * A place to put hardcoded values to make project more scalable when new themes are added.
 */
class PomodoroHelper {

    private static long workTime, breakTime, pauseTimeZero, remainder, longBreak, smallBonusBreak, mediumBonusBreak;
    private static long defaultWorkTime, defaultBreakTime;
    private static String baseWorkTime, basePauseTime;
    private static boolean active, pendingWorkBonus, pendingBreakBonus;
    private static int smallPrice, mediumPrice, largePrice, veryLargePrice;


    PomodoroHelper() {

        workTime = 25 * 60 * 1000;
        breakTime = 5 * 60 *  1000;
        longBreak = 10 * 60 * 1000;
        smallBonusBreak = breakTime + 2 * 60 * 1000;
        mediumBonusBreak = breakTime + 6 * 60 *1000;

        pauseTimeZero = 1000;
        remainder = 0;

        //to be removed when not testing
        long testBreakTime = 10 * 1000;
        long testWorkTime = 15 * 1000;
        workTime = testWorkTime;
        breakTime = testBreakTime;

        smallPrice = 2;
        mediumPrice = 5;
        largePrice = 25;
        veryLargePrice = 250;

        baseWorkTime = "25:00";
        basePauseTime = "00:00";

        defaultWorkTime = 25 * 60 * 1000;
        defaultBreakTime = 5 * 60 * 1000;

    }

    long getWorkTime() {
        return workTime;
    }

    long getBreakTime() {
        return breakTime;
    }

    long getPauseTimeZero() {
        return pauseTimeZero;
    }

    long getRemainder() {
        return remainder;
    }

    long getLongBreak() {
        return longBreak;
    }

    String getBaseWorkTime() {
        return baseWorkTime;
    }

    String getBasePauseTime() {
        return basePauseTime;
    }

    boolean isLongBreak(int cycles) {
        return ((cycles % 4 == 0) && cycles != 0);
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }
    /* Item legend:
     * 0 = +2 minutes break [ 2 points]
     * 1 = +6 minutes break [ 5 points ]
     * 2 = -2 minutes work [ 3 points ]
     * 3 = -6 minutes work [ 6 points ]
     * 4 = Origin Theme 1 [ 25 points ]
     * 5 = Origin Theme 2 [ 25 points ]
     * 6 = Placeholder Theme [ 250 points ]
     */
    void setBreakTime(int item) {
        this.pendingBreakBonus = true;
        if (item == 0) {
            this.breakTime = smallBonusBreak;
        }
        if (item == 1) {
            this.breakTime = mediumBonusBreak;
        }
    }

    void setWorkTime(int item) {
        this.pendingWorkBonus = true;
        if (item == 2) {
            this.workTime = workTime - 2 * 60 * 1000;
        }
        if (item == 3) {
            this.workTime = workTime - 6 * 60 * 1000;
        }
    }

    void setTheme(int item) {
        if (item == 4) {

        }
        if (item == 5) {

        }
        if (item == 6) {

        }
    }

    void resetDefaultWorkTime() {
        workTime = defaultWorkTime;
    }

    void resetDefaultBreakTime() {
        breakTime = defaultBreakTime;
    }

    int workPrice(int priceType) {
        return (priceType % 2) + priceType;
    }

    int getSmallPrice() {
        return smallPrice;
    }

    int getMediumPrice() {
        return mediumPrice;
    }

    int getLargePrice() {
        return largePrice;
    }

    int getVeryLargePrice() {
        return veryLargePrice;
    }

}
