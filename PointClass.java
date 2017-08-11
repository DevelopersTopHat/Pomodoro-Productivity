package com.example.admin.pomodoroproductivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *Hopefully deals with all of the shared point information.
 */
class PointClass extends Service {

    SharedPreferences.Editor editor;

    private int points = 0;
    private String pointValue;

    PointClass(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("points", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("points", points);
        editor.apply();
    }

    void updatePoints(int tempPoints) {
        this.points = tempPoints;
        this.pointValue = Integer.toString(points);
        editor.putInt("points", points);
        editor.commit();
    }

    String getPointValue() {
        return pointValue;
    }

    int getPoints() {
        return points;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
