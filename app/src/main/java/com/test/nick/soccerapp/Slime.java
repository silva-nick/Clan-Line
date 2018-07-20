package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

class Slime extends Entity {
    private Bitmap[] southArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south1_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south2_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south4_3), 160, 210, false)};
    private Bitmap[] northArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north1_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north2_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north4_3), 160, 210, false)};

    private Bitmap currentBitmap = northArray[0];

    public Slime(Resources resources, boolean side, boolean lane){
        super("Slime",10,10,10,100, side, lane, resources);
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
