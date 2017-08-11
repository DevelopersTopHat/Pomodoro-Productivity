package com.example.admin.pomodoroproductivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShopActivity extends AppCompatActivity {

    Intent about, settings;
    PomodoroHelper pomodoroHelper = new PomodoroHelper();

    private int cost[] = {
            pomodoroHelper.getSmallPrice(),
            pomodoroHelper.getMediumPrice(),
            3,
            pomodoroHelper.workPrice(pomodoroHelper.getMediumPrice()),
            pomodoroHelper.getLargePrice(),
            pomodoroHelper.getLargePrice(),
            pomodoroHelper.getVeryLargePrice()};

    TextView pointsDisplay;

    int points;
    boolean isSmallBreak, isMediumBreak, isSmallWork, isMediumWork, isTomatoTheme, purchasedTomato;
    String POINT_FILE, SMALL_BREAK, MEDIUM_BREAK, SMALL_WORK, MEDIUM_WORK, OPTION_TOMATO;
    SharedPreferences.Editor editor, smallBreakEdit, mediumBreakEdit, smallWorkEdit, mediumWorkEdit, tomatoThemeEdit, purchaseTomatoEdit;
    SharedPreferences sharedPoints, sharedSmallBreak, sharedMediumBreak, sharedSmallWork, sharedMediumWork, sharedTomatoTheme, sharedPurchaseTomato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pointsDisplay = (TextView) findViewById(R.id.display_points);

        POINT_FILE = "points.txt";
        SMALL_BREAK = "small_break.txt";
        MEDIUM_BREAK = "medium_break.txt";
        SMALL_WORK = "small_work.txt";
        MEDIUM_WORK = "medium_work.txt";
        OPTION_TOMATO = "Tomato-vine theme [ " + cost[4] + " points ]";

        sharedPoints = getSharedPreferences(POINT_FILE, Context.MODE_PRIVATE);
        editor = sharedPoints.edit();
        points = getPoints();
        pointsDisplay.setText(Integer.toString(points));

        sharedSmallBreak = getSharedPreferences(SMALL_BREAK, Context.MODE_PRIVATE);
        smallBreakEdit = sharedSmallBreak.edit();
        isSmallBreak = sharedSmallBreak.getBoolean("small_break.txt", false);

        sharedMediumBreak = getSharedPreferences(MEDIUM_BREAK, Context.MODE_PRIVATE);
        mediumBreakEdit = sharedMediumBreak.edit();
        isMediumBreak = sharedMediumBreak.getBoolean("medium_break.txt", false);

        sharedSmallWork = getSharedPreferences(SMALL_WORK, Context.MODE_PRIVATE);
        smallWorkEdit = sharedSmallWork.edit();
        isSmallWork = sharedSmallWork.getBoolean("small_work.txt", false);

        sharedMediumWork = getSharedPreferences(MEDIUM_WORK, Context.MODE_PRIVATE);
        mediumWorkEdit = sharedMediumWork.edit();
        isMediumWork = sharedMediumWork.getBoolean("medium_work.txt", false);

        sharedTomatoTheme = getSharedPreferences("tomato_theme.txt", Context.MODE_PRIVATE);
        tomatoThemeEdit = sharedTomatoTheme.edit();
        isTomatoTheme = sharedTomatoTheme.getBoolean("tomato_theme.txt", false);

        sharedPurchaseTomato = getSharedPreferences("purchase_tomato.txt", Context.MODE_PRIVATE);
        purchaseTomatoEdit = sharedPurchaseTomato.edit();
        purchasedTomato = sharedPurchaseTomato.getBoolean("purchase_tomato.txt", false);

        if (isTomatoTheme) {
            OPTION_TOMATO = "Tomato-vine theme!";
        }


        String[] options = {
                "+ 2 minutes break     [ " + cost[0] + " points ]",
                "+ 6 minutes break     [ " + cost[1] + " points ]",
                "- 2 minutes work       [ " + cost[2] + " points ]",
                "- 6 minutes work       [ " + cost[3] + " points ]",
                //OPTION_TOMATO,
                //"Rising sun Theme       [ " + cost[5] + " points ]",
                //"Default theme!",
                //"Placeholder Theme    [ " + cost[6] + " points ]"
        };



        ListAdapter optionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        ListView shopList = (ListView) findViewById(R.id.shopList);

        shopList.setAdapter(optionAdapter);
        shopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 || i == 1) {
                    buyBreak(i);
                }
                else if (i == 2 || i == 3) {
                    buyShorterWork(i);
                }
                else if (i == 4 || i == 5 || i == 6) {
                    buyTheme(i);
                }
                else {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }/*
        else if (item.getItemId() == R.id.menu_settings) {
            if (settings == null) {
                settings = new Intent(this, SettingActivity.class);
                startActivity(settings);
            }
            else {
                onNewIntent(settings);
            }
        }*/
        else if (item.getItemId() == R.id.menu_about) {
            if (about == null) {
                about = new Intent(this, AboutActivity.class);
                startActivity(about);
            }
            else {
                startActivity(about);
            }
        }
        else {
            finish();
        }
        return true;
    }

    private void setPoints(int points) {
        editor.putString("points.txt", Integer.toString(points));
        editor.apply();
    }

    private int getPoints() {
        int tempPoints;
        if (sharedPoints.getString("points.txt", null) == null) {
            tempPoints = 0;
        }
        else {
            tempPoints = Integer.valueOf(sharedPoints.getString("points.txt", null));
        }
        return tempPoints;
    }

    private void setDisplayPoints() {
        points = getPoints();
        setPoints(points);
        pointsDisplay.setText(Integer.toString(points));
    }

/*
    private void purchase(int item) {
        tempPoints = pointClass.getPoints();
        tempPoints -= cost[item];
        pointClass.updatePoints(tempPoints);
        pointsDisplay.setText(pointClass.getPointValue());
    } */

    /* Item legend:
    * 0 = +2 minutes break [ 2 points]
    * 1 = +6 minutes break [ 5 points ]
    * 2 = -2 minutes work [ 3 points ]
    * 3 = -6 minutes work [ 6 points ]
    * 4 = Origin Theme 1 [ 25 points ]
    * 5 = Origin Theme 2 [ 25 points ]
    * 6 = Placeholder Theme [ 250 points ]
    */
    private void buyBreak(int item) {
        if (points >= cost[item] && !isSmallBreak && !isMediumBreak) {
            if (item == 0) {

                points -= cost[item];
                setPoints(points);
                setDisplayPoints();

                isSmallBreak = true;
                smallBreakEdit.putBoolean("small_break.txt", true).apply();

                Toast.makeText(this, "You purchased, +2 minutes on your break time.", Toast.LENGTH_SHORT).show();
            }
            if (item == 1) {

                points -= cost[item];
                setPoints(points);
                setDisplayPoints();

                isMediumBreak = true;
                mediumBreakEdit.putBoolean("medium_break.txt", true).apply();

                Toast.makeText(this, "You purchased, +6 minutes on your break time.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (points < cost[item]) {
                Toast.makeText(this, "You do not have enough points, you earn points by completing work cycles.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "You already have a pending break bonus. Once it is done, you can get another one.", Toast.LENGTH_LONG).show();
            }
        }
        setDisplayPoints();
    }

    private void buyShorterWork(int item) {
        if (points >= cost[item] && !isSmallWork && !isMediumWork) {
            if(item == 2) {
                points -= cost[item];
                setPoints(points);
                setDisplayPoints();

                isSmallWork = true;
                smallWorkEdit.putBoolean("small_work.txt", true).apply();

                Toast.makeText(this, "You purchased, -2 minutes on your work time.", Toast.LENGTH_SHORT).show();
            }
            if(item == 3) {
                points -= cost[item];
                setPoints(points);
                setDisplayPoints();

                isMediumWork = true;
                mediumWorkEdit.putBoolean("medium_work.txt", true).apply();

                Toast.makeText(this, "You purchased, -6 minutes on your work time.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (points < cost[item]) {
                Toast.makeText(this, "You do not have enough points, you earn points by completing work cycles.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "You already have a pending work bonus. Once it is done, you can get another one.", Toast.LENGTH_LONG).show();
            }
        }
        setDisplayPoints();
    }

    private void buyTheme(int item) {
        if (points >= cost[item] && !purchasedTomato) {
            if(item == 4) {
                points -= cost[item];
                setPoints(points);
                setDisplayPoints();

                purchasedTomato = true;
                purchaseTomatoEdit.putBoolean("purchase_tomato.txt", true).apply();
                isTomatoTheme = true;
                tomatoThemeEdit.putBoolean("tomato_theme.txt", true);

                Toast.makeText(this, "You purchased, a new origin theme!", Toast.LENGTH_SHORT).show();
            }
            if(item == 5) {
                points -= cost[item];
                setPoints(points);
                setDisplayPoints();

                pomodoroHelper.setTheme(item);

                Toast.makeText(this, "You purchased, a new origin theme!", Toast.LENGTH_SHORT).show();
            }
            if(item == 6) {


                Toast.makeText(this, "You purchased, a new theme!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (purchasedTomato) {
                Toast.makeText(this, "You are now using the tomato-vine theme!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "You do not have enough points, you earn points by completing work cycles.", Toast.LENGTH_LONG).show();
            }
        }
        setDisplayPoints();
    }
}
