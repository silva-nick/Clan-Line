package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

class Robot extends Entity {
    private Bitmap[] southArray = new Bitmap[]{BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south1),
            BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south2),
            BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south3),
            BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south4)};
    private Bitmap[] northArray = new Bitmap[]{BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north1),
            BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north2),
            BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north3),
            BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north4)};

    public Robot(Resources R, boolean side){
        super("Robot",10,10,10,100, side, R);
        if(isSouth()) {
            setX(Resources.getSystem().getDisplayMetrics().widthPixels/2);
            setY(0);
        }
        else{
            setX(Resources.getSystem().getDisplayMetrics().widthPixels/2);
            setY(Resources.getSystem().getDisplayMetrics().heightPixels);
        }

    }

    @Override
    public void draw(Canvas canvas, int frame){
        if(isSouth()) {canvas.drawBitmap(southArray[frame%4], getX(), getY(), null);}
        else {canvas.drawBitmap(northArray[frame%4], getX(), getY(), null);}
    }

    @Override
    public void update(){
        if(isSouth()) {
            setY(getY() + 1);
        }
        else{
            setY(getY()-1);
        }
    }
}
