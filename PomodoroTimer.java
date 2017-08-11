package com.example.admin.pomodoroproductivity;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static java.lang.String.*;

/**
 * Contains all of the functionality of a Pomodoro timer
 */

class PomodoroTimer {

    private TextView displayTime;
    private Vibrator vibrateOnClick;

    private ImageButton startButton, stopButton, pauseButton;

    /**
     * workTimer represents the working phase of the pomodoro technique.
     * breakTimer represents the break phase of the pomodoro technique.
     * pauseTimer represents the small window at the end of each timer where it is required to pause for a couple of seconds for better aesthetic.
     */
    private CountDownTimer workTimer;
    private CountDownTimer breakTimer;

    //is it counting down?, is it work time?, is it break time?, time for an extra long break?
    private boolean active;
    private boolean working;
    private boolean breaking;
    private boolean pausing;
    private String baseWorkTime;
    private String baseBreakTime;

    //represents completed cycles (possible used in reward system.
    private int cycles;
    private long workTime;
    private long breakTime;
    private long pauseTimeZero;
    private long remainder;

    PomodoroTimer(TextView displayTime, Vibrator vibrateOnClick, ImageButton startButton, ImageButton pauseButton, ImageButton stopButton) {
        this.displayTime = displayTime;
        this.vibrateOnClick = vibrateOnClick;
        this.startButton = startButton;
        this.pauseButton = pauseButton;
        this.stopButton = stopButton;

        workTime = 25 * /*60 **/ 1000;
        breakTime = 5 */*60 **/  1000;
        pauseTimeZero = 1100;

        remainder = 0;
        cycles = 0;

        baseWorkTime = "25:00";
        baseBreakTime = "00:00";

        active = false;
        pausing = false;

        this.displayTime.setText(baseWorkTime);

        startTime();
        stopTime();
        pauseTime();
    }

    private void startTime() {
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!active && !pausing) {
                    vibrateOnClick.vibrate(20);
                    displayWorkTime();
                }
            }
        });
    }

    private void stopTime() {
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
                pausing = false;
                working = false;
                breaking = false;
                displayTime.setText(baseWorkTime);
            }
        });
    }

    private void pauseTime() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (active) {
                    active = false;
                    pausing = true;

                    vibrateOnClick.vibrate(20);

                    if (working) {
                        workTimer.cancel();
                    }
                    else if (breaking) {
                        breakTimer.cancel();
                    }
                }
                else if (pausing) {
                    active = true;
                    pausing = false;

                    vibrateOnClick.vibrate(20);

                    //continues the timer after the pause has ended
                    workTimer = new CountDownTimer(getRemainder(), 1000) {
                        public void onTick(long millisUntilFinished) {
                            getTime(millisUntilFinished);
                        }

                        public void onFinish() {
                            if (!working) {
                                displayWorkTime();
                            } else {
                                displayBreakTime();
                            }
                        }
                    };
                    workTimer.start();
                }
            }
        });
    }

    private void displayWorkTime() {
        active = true;
        working = true;

        workTimer = new CountDownTimer(workTime, 1000) {
            public void onTick(long millisUntilFinished) {
                getTime(millisUntilFinished);
            }

            public void onFinish() {
                cycles++;
                //MediaPlayer breakSound = MediaPlayer.create(getApplication(), R.raw.break_time);
                //breakSound.start();
                pauseAtZero();
            }

        };
        workTimer.start();
    }

    private void displayBreakTime() {
        active = true;
        breaking = true;

        if (((cycles % 4) == 0) && (cycles != 0)) {
            breakTime = 12000;
        }
        else {
            breakTime = 10000;
        }

        breakTimer = new CountDownTimer(breakTime, 1000) {
            public void onTick(long millisUntilFinished) {
                getTime(millisUntilFinished);
            }

            public void onFinish() {
                //MediaPlayer workSound = MediaPlayer.create(getApplication(), R.raw.work_time);
                //workSound.start();
                pauseAtZero();
            }

        };
        breakTimer.start();
    }

    private long getRemainder() {
        return remainder - 1000;
    }

    /*
    Does all of the needed requirements during countdown that is consistent between actions (button clicks).
     */
    private void getTime(long millisUntilFinished) {
        long minutes = ((millisUntilFinished / 1000) % 3600) / 60;
        long seconds = (millisUntilFinished / 1000) % 60;
        remainder = millisUntilFinished;
        String timeDisplay = format("%02d:%02d", minutes, seconds);
        displayTime.setText(timeDisplay);
    }

    /*
    This method is used to display "00:00" at the end of each cycle for a nicer display.
     */
    private void pauseAtZero() {
        CountDownTimer pauseTimer = new CountDownTimer(pauseTimeZero, 1000) {
            public void onTick(long millisUntilFinished) {
                displayTime.setText(baseBreakTime);
            }

            public void onFinish() {
                if (!working) {
                    breaking = false;
                    displayWorkTime();
                } else {
                    working = false;
                    displayBreakTime();
                }
            }

        };
        pauseTimer.start();
    }

}
