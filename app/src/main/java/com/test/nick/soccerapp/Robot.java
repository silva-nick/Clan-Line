package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

class Robot extends Entity {
    private Bitmap[] southArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south3_1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south3_2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south3_4), 160, 210, false)};
    private Bitmap[] northArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north3_1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north3_2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north3_4), 160, 210, false)};

    private Bitmap currentBitmap = northArray[0];

    public Robot(Resources resources, boolean side, boolean lane){
        super("Robot",10,10,10,1000, side, lane, resources);
        if(isSouth()) {
            if(isLeft()){setX(Resources.getSystem().getDisplayMetrics().widthPixels/3-southArray[0].getWidth());}
            else{setX(Resources.getSystem().getDisplayMetrics().widthPixels*2/3);}
            setY(0);
        }
        else{
            if(isLeft()){setX(Resources.getSystem().getDisplayMetrics().widthPixels/3-southArray[0].getWidth());}
            else{setX(Resources.getSystem().getDisplayMetrics().widthPixels*2/3);}
            setY(Resources.getSystem().getDisplayMetrics().heightPixels-50);
        }

    }

    @Override
    public void draw(Canvas canvas, int frame){
        canvas.drawBitmap(currentBitmap, getX(), getY(), null);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        if(isSouth()) {
            canvas.drawText(String.valueOf(getHealth()), getX()-5, getY()-5, paint);
        }
        else {
            canvas.drawText(String.valueOf(getHealth()), getX()-5, getY()+215, paint);
        }
        if (frame % 4 == 0) {
            if(isSouth()) {
                currentBitmap = southArray[(frame/4)%4];
            }
            else {
                currentBitmap = northArray[(frame/4)%4];
            }
        }

    }

    @Override
    public void update(int frame){
        if(isSouth() && !isFighting()) {
            setY(getY() + 2);
        }
        else if(!isFighting()){
            setY(getY() - 2);
        }
        ArrayList<Entity> enemies = getEnemies();
        for(Entity e : enemies){
            fighting(e, frame);
        }
    }
}
