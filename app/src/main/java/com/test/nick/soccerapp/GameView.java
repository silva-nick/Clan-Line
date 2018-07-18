package com.test.nick.soccerapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "GameView";
    private GameThread mainThread;
    private Robot test;
    private Robot test2;
    private int count = 0;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        mainThread = new GameThread(getHolder(),this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mainThread.setRunning(true);
        mainThread.start();
        test = new Robot(getResources(), false);
        test2 = new Robot(getResources(), true);
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
        test.update();
        test2.update();
    }

    @Override
    public void draw(Canvas canvas){
        count++;
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            test.draw(canvas, count);
            test2.draw(canvas, count);
        }
    }
}
