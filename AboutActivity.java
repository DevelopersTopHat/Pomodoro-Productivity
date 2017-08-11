package com.example.admin.pomodoroproductivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    ConstraintLayout aboutLayout;
    TextView aboutSection;
    String aboutText;
    Intent about, settings, shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // Find the toolbar view inside the activity layout
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        aboutText = "Pomodoro Productivity is an application designed to optimize productive working time. " +
                "You work for 25 minutes, and then you get a 5 minute break; after 4 cycles of working, you get a 10 minute break. " +
                "During the working period, you are expected to focus on your task, but during your break, you are free to do whatever you like. \n\n" +
                //"Pomodoro Productivity utilizes classical and operant conditioning (positive and negative reinforcement), to enhance your working experience." +
                "Pomodoro Productivity utilizes classical conditioning (positive reinforcement), to enhance your working experience." +
                "After every completed working cycle, you earn a point which you may spend in the shop for various rewards. " +
                //"However, if you pause for too long, or try to cheat the reward system, you will face a penalty to your break, or be forced to watch an advertisement. " +
                "Ultimately, Pomodoro Productivity is an application built on personal accountability, work hard, and you will be rewarded. \n\n" +
                "The name Pomodoro Productivity comes from the pomodoro technique. A technique named after a tomato timer used for productivity. " +
                "This application is my first, built by Abdeali Daginawala, if you have any questions or concerns please contact abdeali1997@gmail.com. ";

        aboutSection = (TextView) findViewById(R.id.aboutSection);
        aboutLayout = (ConstraintLayout) findViewById(R.id.aboutLayout);

        aboutSection.setText(aboutText);
        aboutSection.setTextColor(Color.parseColor("#323232"));


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
        }*/ else if (item.getItemId() == R.id.menu_shop) {
            if (shop == null) {
                shop = new Intent(this, ShopActivity.class);
                startActivity(shop);
            }
            else {
                startActivity(shop);
            }
        }
        else {
            finish();
        }
        return true;
    }

}
