package com.test.nick.soccerapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
    private int count = 0;
    private ArrayList<Entity> charList;
    private ConnectedThread messageThread;
    private Bitmap background;

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
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background),
                Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels-120, false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) { }

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

    public void sendThread(ConnectedThread thread){
        messageThread = thread;
    }

    public void update(){
        if(charList.get(0).getHealth()<=0){
            messageThread.write(new byte[]{-1});
        }

        for(Entity e : charList){

            for(Entity e2 : charList){
                if(e.isCollided(e2) && e != e2 && e.isLeft()==e2.isLeft() || e.isCollided(e2) && e != e2 && (e.getName().equals("Base") || e2.getName().equals("Base"))){
                    e.setFighting(e2);
                    e2.setFighting(e);
                }
            }
            if(e.getHealth()<=0){
                if (e.isFighting()) {
                    ArrayList<Entity> enemies = e.getEnemies();
                    for(Entity remove : enemies){
                        remove.getEnemies().remove(e);
                    }
                }

                charList.remove(e);
                e = null;
                break;
            }
            if(e.getY() < -80){
                byte[] messageArray = new byte[2];
                switch(e.getName()){
                    case "Witch":
                        messageArray[0] = 0;
                        break;
                    case "Fighter":
                        messageArray[0] = 1;
                        break;
                    case "Robot":
                        messageArray[0] = 2;
                        break;
                    case "Slime":
                        messageArray[0] = 3;
                        break;
                    case "Goblin":
                        messageArray[0] = 4;
                        break;
                    case "Fireball":
                        messageArray[0] = 5;
                        break;
                    case "Catapult":
                        messageArray[0] = 6;
                        break;
                    case "Rock":
                        messageArray[0] = 7;
                        break;
                        default: break;
                }
                messageArray[1] = (byte) (e.isLeft() ? 1 : 0);
                charList.remove(e);
                e = null;
                messageThread.write(messageArray);
            }
            e.update(count);
        }
    }

    @Override
    public void draw(Canvas canvas){
        count++;
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawBitmap(background, 0, 0, null);
            for(Entity e : charList){
                e.draw(canvas, count);
            }
        }
    }

    public void add(Entity entity){
        charList.add(entity);
    }

    public void destroy(){
        mainThread.setRunning(false);

    }
}

//sources:
//http://www.independent-software.com/android-speeding-up-canvas-drawbitmap.html/
//https://developer.android.com/topic/performance/graphics/load-bitmap
//https://code.tutsplus.com/tutorials/working-with-git-in-android-studio--cms-30514