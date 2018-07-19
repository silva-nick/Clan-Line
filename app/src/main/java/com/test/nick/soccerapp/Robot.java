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

    private Bitmap currentBitmap = northArray[0];

    public Robot(Resources resources, boolean side){
        super("Robot",10,10,10,100, side, resources);
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
        if (frame % 4 == 0) {
            if(isSouth()) {
                currentBitmap = southArray[(frame/4)%4];
            }
            else {
                currentBitmap = northArray[(frame/4)%4];
            }
        }
        canvas.drawBitmap(currentBitmap, getX(), getY(), null);
    }

    @Override
    public void update(){
        if(isSouth()) {
            setY(getY() + 2);
        }
        else{
            setY(getY()-2);
        }
    }
}
