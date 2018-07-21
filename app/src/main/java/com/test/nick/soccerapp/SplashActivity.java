package com.test.nick.soccerapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int BOUNCE_HEIGHT = -500;
    private AnimatedVectorDrawableCompat avd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starting program");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Calls imageView avd animation
        ImageView avdView = (ImageView)findViewById(R.id.imageView);
        Drawable d = avdView.getDrawable();
        avd = (AnimatedVectorDrawableCompat)d;
        avd.start();

        new Thread() {
            public void run() {
                int i = 0;
                while (i<4) {
                    try {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                animation(findViewById(R.id.clan));
                                animation(findViewById(R.id.line));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i++;
                }
            }
        }.start();
    }


    //Called on screen click, moves to HomeActivity
    public void leaveSplash(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    //Uses SpringAnimation to animate game title
    private void animation(View view){
        SpringAnimation animation = new SpringAnimation(view, DynamicAnimation.TRANSLATION_Y);
        SpringForce force = new SpringForce();
        force.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW);
        animation.setSpring(force);
        animation.animateToFinalPosition(BOUNCE_HEIGHT);
        animation.animateToFinalPosition(0);
    }
}