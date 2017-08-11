package com.example.admin.pomodoroproductivity;

/**
 *All of the base time values, including those that can be bought from the shop.
 */

public class TimeHelper {

    long defaultWorkTime, defaultBreakTime, defaultLongBreak, smallBreak, mediumBreak, smallWork, mediumWork;

    TimeHelper() {
        defaultWorkTime = 25 * 60 * 1000;
        defaultBreakTime = 5 * 60 * 1000;
        defaultLongBreak = 10 * 60 * 1000;

        smallBreak = 2 * 60 * 1000;
        mediumBreak = 6 * 60 * 1000;

        smallWork = 2 * 60 * 1000;
        mediumWork = 6 * 60 * 1000;
    }

    long getDefaultWorkTime() {
        return defaultWorkTime;
    }

    long getDefaultBreakTime() {
        return defaultBreakTime;
    }

    long getDefaultLongBreak() {
        return defaultLongBreak;
    }

    long getSmallBreak() {
        return smallBreak;
    }

    long getMediumBreak() {
        return mediumBreak;
    }

    long getSmallWork() {
        return smallWork;
    }

    long getMediumWork() {
        return mediumWork;
    }

}
