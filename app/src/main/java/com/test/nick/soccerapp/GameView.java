package com.test.nick.soccerapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;

class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private static final String TAG = "GameView";
    private GameThread mainThread;
    private Robot test;
    private Robot test2;
    private int count = 0;
    private ArrayList<Entity> charList;

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        mainThread = new GameThread(getHolder(),this);
        setFocusable(true);
        charList = new ArrayList<Entity>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update(){
        for(Entity e : charList){
            e.update();
        }
    }

    @Override
    public void draw(Canvas canvas){
        count++;
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            for(Entity e : charList){
                e.draw(canvas, count);
            }
        }
    }

    public void add(Entity entity){
        charList.add(entity);
    }
}

//sources:
//http://www.independent-software.com/android-speeding-up-canvas-drawbitmap.html/
//https://developer.android.com/topic/performance/graphics/load-bitmap
//https://code.tutsplus.com/tutorials/working-with-git-in-android-studio--cms-30514