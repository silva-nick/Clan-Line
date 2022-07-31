package com.test.nick.soccerapp;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int BOUNCE_HEIGHT = -500;
    private Animatable avd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starting program");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Calls imageView avd animation
        ImageView avdView = findViewById(R.id.imageView);
        Drawable d = avdView.getDrawable();

        avd = (Animatable)d;
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