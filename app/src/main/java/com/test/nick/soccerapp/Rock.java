package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

class Rock extends Entity {

    private Bitmap currentBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south_rock), 60, 100, false);

    public Rock(Resources resources, boolean side, boolean lane){
        super("Rock",24,300,14,1, 15, true, side, lane, resources);

        if(isSouth()){
            setX(isLeft() ? (180) : (Resources.getSystem().getDisplayMetrics().widthPixels-340));
        }
        else{
            setX(Resources.getSystem().getDisplayMetrics().widthPixels/2-50);
        }

        if(isSouth()) {
            currentBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south_rock), 60, 100, false);
        } else {
            currentBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north_rock), 60, 100, false);
        }

        if(isSouth()) {
            setY(-50);
        } else{
            setY(Resources.getSystem().getDisplayMetrics().heightPixels-120);
        }
    }

    @Override
    public void draw(Canvas canvas, int frame){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        canvas.drawBitmap(currentBitmap, getX(), getY(), null);
    }

    @Override
    public void update(int frame){
        setDiagonal(getY() > Resources.getSystem().getDisplayMetrics().heightPixels - 680 &&
                getY() < Resources.getSystem().getDisplayMetrics().heightPixels - 390);

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
