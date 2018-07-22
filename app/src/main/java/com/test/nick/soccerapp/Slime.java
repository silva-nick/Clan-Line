package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

class Slime extends Entity {
    private Bitmap[] southArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south4_1), 100, 100, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south4_2), 100, 100, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south4_3), 100, 100, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south4_4), 100, 100, false)};
    private Bitmap[] northArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north4_1), 100, 100, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north4_2), 100, 100, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north4_3), 100, 100, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north4_4), 100, 100, false)};


    private Bitmap currentBitmap = northArray[0];

    public Slime(Resources resources, boolean side, boolean lane){
        super("Slime",4,10,12,400, side, lane, resources);

        if(isSouth()){
            setX(isLeft() ? (180) : (Resources.getSystem().getDisplayMetrics().widthPixels-340));
        }
        else{
            setX(Resources.getSystem().getDisplayMetrics().widthPixels/2-50);
        }


        if(isSouth()) {
            setY(-50);
        }
        else{
            setY(Resources.getSystem().getDisplayMetrics().heightPixels-50);
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
        if(getY()>Resources.getSystem().getDisplayMetrics().heightPixels-680&&
                getY()<Resources.getSystem().getDisplayMetrics().heightPixels-390){
            setDiagonal(true);
        }
        else{
            setDiagonal(false);
        }

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
            fighting(e, frame);
        }
    }
}
