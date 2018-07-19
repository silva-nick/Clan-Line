package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

class Robot extends Entity {
    private Bitmap[] southArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south4), 160, 210, false)};
    private Bitmap[] northArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north4), 160, 210, false)};

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
            setY(getY() - 2);
        }
    }
}
