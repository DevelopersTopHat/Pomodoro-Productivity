package com.example.admin.pomodoroproductivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    Switch pauseOnExit, screenAwake, disableWifi;
    WifiManager wifiManager;
    Intent about, settings, shop;
    PomodoroHelper pomodoroHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //pomodoroHelper = new PomodoroHelper();

        pauseOnExit = (Switch) findViewById(R.id.pause_on_exit);
        screenAwake = (Switch) findViewById(R.id.keep_screen_awake);
        disableWifi = (Switch) findViewById(R.id.disable_wifi);

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        setDisableWifi();
        setPauseOnExit();
        setScreenAwake();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            if (about == null) {
                about = new Intent(this, AboutActivity.class);
                startActivity(about);
            }
            else {
                onNewIntent(about);
            }
        }/*
        else if (item.getItemId() == R.id.menu_settings) {
            if (settings == null) {
                settings = new Intent(this, MainActivity.class);
                startActivity(settings);
            }
            else {
                onNewIntent(settings);
            }
        }*/
        else if (item.getItemId() == R.id.menu_shop) {
            if (shop == null) {
                shop = new Intent(this, ShopActivity.class);
                startActivity(shop);
            }
            else {
                onNewIntent(shop);
            }
        }
        return true;
    }

    /*
    A method that pauses the timer on exit/minimizing of the application.
     */
    public void setPauseOnExit() {
        pauseOnExit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                }else{

                }

            }
        });
    }

    /*
    A method that prevents the system sleep function from activating after inactivity.
     */
    public void setScreenAwake() {
        pauseOnExit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                while (isChecked){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
    }

    /*
    A method that disables the wifi when the work timer is active.
     */

    public void setDisableWifi() {
        pauseOnExit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked && wifiManager.isWifiEnabled()){
                    if (pomodoroHelper.isActive()) {
                        wifiManager.setWifiEnabled(false);
                    }
                    else {
                        wifiManager.setWifiEnabled(true);
                    }
                }
            }
        });
    }

}
