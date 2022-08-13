package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

class Goblin extends Entity {
    private final Bitmap[] southArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south5_1), 90, 120, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south5_2), 90, 120, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south5_3), 90, 120, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south5_4), 90, 120, false)};
    private final Bitmap[] northArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north5_1), 90, 120, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north5_2), 90, 120, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north5_3), 90, 120, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north5_4), 90, 120, false)};

    private Bitmap currentBitmap = northArray[0];

    public Goblin(Resources resources, boolean side, boolean lane){
        super("Goblin",8,12,8,600, 20, false, side, lane, resources);

        if(isSouth()){
            setX(isLeft() ? (180) : (Resources.getSystem().getDisplayMetrics().widthPixels-340));
        }
        else{
            setX(Resources.getSystem().getDisplayMetrics().widthPixels/2-80);
        }

        if(isSouth()) {
            setY(-50);
        }
        else{
            setY(Resources.getSystem().getDisplayMetrics().heightPixels-120);
        }

    }

    @Override
    public void draw(Canvas canvas, int frame){
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
        canvas.drawBitmap(currentBitmap, getX(), getY(), null);
    }

    @Override
    public void update(int frame){
        setDiagonal(getY() > Resources.getSystem().getDisplayMetrics().heightPixels - 820 &&
                getY() < Resources.getSystem().getDisplayMetrics().heightPixels - 520);

        if(isSouth() && !isFighting()) {
            if(isDiagonal()){
                setX(isLeft() ? (getX()+getSpeed()) : (getX()-getSpeed()));
            }
            setY(getY() + getSpeed());
        }
        else if(!isFighting()){
            if(isDiagonal()){
                setX(isLeft() ? (getX()-getSpeed()) : (getX()+getSpeed()));
            }
            setY(getY() - getSpeed());
        }
        ArrayList<Entity> enemies = getEnemies();
        for(Entity e : enemies){
            attack(e, frame);
            if (!getSplashDamage()) break;
        }
    }
}
