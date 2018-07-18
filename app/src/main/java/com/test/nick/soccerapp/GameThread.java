package com.test.nick.soccerapp;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    final static String TAG = "GameThread";
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    private static Canvas canvas;

    public GameThread(SurfaceHolder holder, GameView view){
        super();
        this.surfaceHolder = holder;
        this.gameView = view;
    }

    @Override
    public void run(){
        while(running){
            canvas = null;

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameView.update();
                    gameView.draw(canvas);
                }
            }   catch (Exception e) {}finally {
                if(canvas!=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        Log.e(TAG, "Error ",e);
                    }
                }
            }
        }
    }

    public void setRunning(boolean b){
        running = b;
    }
}
