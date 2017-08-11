package com.example.admin.pomodoroproductivity;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    TextView displayTime, displayPoints;
    ImageButton startButton, stopButton, pauseButton;
    CountDownTimer workTimer, breakTimer, pauseTimer;
    Intent about, settings, shop;
    PendingIntent pIntentlogin;

    //is it counting down?, is it work time?, is it break time?, time for an extra long break?
    boolean active, working, breaking, pausing, isBackground;

    long workTime, breakTime, pauseTimeZero, remainder, minutes, seconds;
    String timeDisplay, baseWorkTime, baseBreakTime, POINT_FILE;

    Vibrator vibrateOnClick;
    PomodoroHelper pomodoroHelper;
    TimeHelper timeHelper;

    int points, NOTIFICATION_ID = 1234, longBreakCounter = 0;
    String SMALL_BREAK, MEDIUM_BREAK, SMALL_WORK, MEDIUM_WORK;;
    SharedPreferences.Editor editor, smallBreakEdit, mediumBreakEdit, smallWorkEdit, mediumWorkEdit, tomatoThemeEdit;
    SharedPreferences sharedPoints, sharedSmallBreak, sharedMediumBreak, sharedSmallWork, sharedMediumWork, sharedTomatoTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();

        //default screen creation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //helper objects
        pomodoroHelper = new PomodoroHelper();
        timeHelper = new TimeHelper();
        vibrateOnClick = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //default value initialization
        workTime = pomodoroHelper.getWorkTime();
        breakTime = pomodoroHelper.getBreakTime();
        pauseTimeZero = pomodoroHelper.getPauseTimeZero();
        baseWorkTime = pomodoroHelper.getBaseWorkTime();
        baseBreakTime = pomodoroHelper.getBasePauseTime();
        remainder = pomodoroHelper.getRemainder();

        active = false;
        pausing = false;
        isBackground = false;
        pomodoroHelper.setActive(active);

        //display widget binding
        startButton = (ImageButton) findViewById(R.id.startButton);
        stopButton = (ImageButton) findViewById(R.id.stopButton);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        displayTime = (TextView) findViewById(R.id.displayTime);
        displayPoints = (TextView) findViewById(R.id.display_points);

        displayTime.setText(baseWorkTime);

        POINT_FILE = "points.txt";
        SMALL_BREAK = "small_break.txt";
        MEDIUM_BREAK = "medium_break.txt";
        SMALL_WORK = "small_work.txt";
        MEDIUM_WORK = "medium_work.txt";

        sharedPoints = getSharedPreferences(POINT_FILE, Context.MODE_PRIVATE);
        editor = sharedPoints.edit();
        points = getPoints();
        displayPoints.setText(Integer.toString(points));

        sharedSmallBreak = getSharedPreferences(SMALL_BREAK, Context.MODE_PRIVATE);
        smallBreakEdit = sharedSmallBreak.edit();

        sharedMediumBreak = getSharedPreferences(MEDIUM_BREAK, Context.MODE_PRIVATE);
        mediumBreakEdit = sharedMediumBreak.edit();

        sharedSmallWork = getSharedPreferences(SMALL_WORK, Context.MODE_PRIVATE);
        smallWorkEdit = sharedSmallWork.edit();

        sharedMediumWork = getSharedPreferences(MEDIUM_WORK, Context.MODE_PRIVATE);
        mediumWorkEdit = sharedMediumWork.edit();

        startTime();
        stopTime();
        pauseTime();
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
        else if (item.getItemId() == R.id.menu_about) {
            if (about == null) {
                about = new Intent(this, AboutActivity.class);
                startActivity(about);
            }
            else {
                startActivity(about);
            }
        }
        return true;
    }

    public void startTime() {
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!active && !pausing) {
                    vibrateOnClick.vibrate(20);
                    displayWorkTime();
                }
                points = getPoints();
                displayPoints.setText(Integer.toString(points));
            }
        });
    }

    public void stopTime() {
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (working) {
                    vibrateOnClick.vibrate(20);
                    workTimer.cancel();
                }
                else if (breaking) {
                    vibrateOnClick.vibrate(20);
                    breakTimer.cancel();
                }
                active = false;
                pomodoroHelper.setActive(active);
                pausing = false;
                working = false;
                breaking = false;
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NOTIFICATION_ID);
                displayTime.setText(baseWorkTime);
                points = getPoints();
                displayPoints.setText(Integer.toString(points));

                workTime = timeHelper.getDefaultWorkTime();
                breakTime = timeHelper.getDefaultBreakTime();
            }
        });
    }

    public void pauseTime() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (active && !pausing) {
                    active = false;
                    pausing = true;
                    pomodoroHelper.setActive(active);

                    vibrateOnClick.vibrate(20);

                    if (working) {
                        workTimer.cancel();
                    }
                    else if (breaking) {
                        breakTimer.cancel();
                    }
                }
                else if (pausing && !active){
                    active = true;
                    pomodoroHelper.setActive(active);
                    pausing = false;

                    vibrateOnClick.vibrate(20);

                    //continues the timer after the pause has ended
                        workTimer = new CountDownTimer(getRemainder(), 1000) {
                            public void onTick(long millisUntilFinished) {
                                getTime(millisUntilFinished);
                            }

                            public void onFinish() {
                                if (!working) {
                                    pauseAtZero();
                                } else {
                                    longBreakCounter++;
                                    setDisplayPoints();
                                    pauseAtZero();
                                }
                            }
                        };
                    workTimer.start();
                }
                points = getPoints();
                displayPoints.setText(Integer.toString(points));
            }
        });
    }

    public void displayWorkTime() {
        active = true;
        pomodoroHelper.setActive(active);
        working = true;

        workTime = timeHelper.getDefaultWorkTime();

        if (sharedSmallWork.getBoolean("small_work.txt", false)) {
            workTime -= timeHelper.getSmallWork();
            smallWorkEdit.putBoolean("small_work.txt", false).apply();
        }
        else if (sharedMediumWork.getBoolean("medium_work.txt", false)) {
            workTime -= timeHelper.getMediumWork();
            mediumWorkEdit.putBoolean("medium_work.txt", false).apply();
        }

        workTimer = new CountDownTimer(getWorkTime(), 1000) {
            public void onTick(long millisUntilFinished) {
                getTime(millisUntilFinished);
            }
            public void onFinish() {
                longBreakCounter++;
                setDisplayPoints();
                workTime = timeHelper.getDefaultWorkTime();
                pauseAtZero();
            }
        };
        workTimer.start();
    }

    public void displayBreakTime() {
        active = true;
        pomodoroHelper.setActive(active);
        breaking = true;

        breakTime = timeHelper.getDefaultBreakTime();

        if ((longBreakCounter % 4) == 0) {
            breakTime = timeHelper.getDefaultLongBreak();
        }

        if (sharedSmallBreak.getBoolean("small_break.txt", false)) {
            breakTime += timeHelper.getSmallBreak();
            smallBreakEdit.putBoolean("small_break.txt", false).apply();
        }
        else if (sharedMediumBreak.getBoolean("medium_break.txt", false)) {
            breakTime += timeHelper.getMediumBreak();
            mediumBreakEdit.putBoolean("medium_break.txt", false).apply();
        }

        breakTimer = new CountDownTimer(getBreakTime(), 1000) {
            public void onTick(long millisUntilFinished) {
               getTime(millisUntilFinished);
            }

            public void onFinish() {
                breakTime = timeHelper.getDefaultBreakTime();
                pauseAtZero();
            }

        };
        breakTimer.start();
    }

    public long getRemainder() {
        return remainder - 1000;
    }

    /*
    Does all of the needed requirements during countdown that is consistent between actions (button clicks).
     */
    public void getTime(long millisUntilFinished) {
        minutes = ((millisUntilFinished / 1000) % 3600) / 60;
        seconds = (millisUntilFinished / 1000) % 60;
        remainder = millisUntilFinished;
        timeDisplay = String.format("%02d:%02d", minutes, seconds);
        displayTime.setText(timeDisplay);
        sendNotification();
    }

    /*
    This method is used to display "00:00" at the end of each cycle for a nicer display.
     */
    public void pauseAtZero() {
        if (!pausing) {
            pauseTimer = new CountDownTimer(pomodoroHelper.getPauseTimeZero(), 500) {
                public void onTick(long millisUntilFinished) {
                    displayTime.setText(pomodoroHelper.getBasePauseTime());
                    sendNotification();
                }

                public void onFinish() {
                    if (!working && !pausing) {
                        breaking = false;
                        MediaPlayer workSound = MediaPlayer.create(getApplication(), R.raw.work_time);
                        workSound.start();
                        displayWorkTime();
                    } else {
                        working = false;
                        MediaPlayer breakSound = MediaPlayer.create(getApplication(), R.raw.break_time);
                        breakSound.start();
                        displayBreakTime();
                    }
                }

            };
            pauseTimer.start();
        }
    }


    public void sendNotification() {
        if (isBackground) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setAutoCancel(true);
            builder.setContentTitle("Pomodoro Productivity Timer");
            builder.setContentText(displayTime.getText());

            builder.setSmallIcon(R.drawable.note_hour);

            builder.setContentIntent(PendingIntent.getActivity(this, 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT));
            //builder.addAction(R.drawable.note_pause, "Pause", null);
            //builder.addAction(R.drawable.note_stop, "Stop", null);

            builder.setPriority(Notification.PRIORITY_HIGH);

            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, notification);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        // When user presses home page
        if (active) {
            isBackground = true;
            sendNotification();
        }
        super.onUserLeaveHint();
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
        points += 4;
        setPoints(points);
        displayPoints.setText(Integer.toString(points));
    }

    private long getWorkTime() {
        return workTime;
    }

    private long getBreakTime() {
        return breakTime;
    }

    private void setTheme() {
        sharedTomatoTheme = getSharedPreferences("tomato_theme.txt", Context.MODE_PRIVATE);
        tomatoThemeEdit = sharedTomatoTheme.edit();
        if (sharedTomatoTheme.getBoolean("tomato_theme.txt", false)) {
            tomatoThemeEdit.putBoolean("tomato_theme.txt", true);
        }
    }
}
