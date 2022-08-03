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
    private Bitmap[] diagArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag3_1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag3_2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag3_4), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr3_1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr3_2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr3_4), 160, 210, false)};
    private Bitmap[] diagupArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup3_1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup3_2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup3_4), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup3_1), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup3_2), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup3_3), 160, 210, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup3_4), 160, 210, false)};

    private Bitmap currentBitmap = northArray[0];

    public Robot(Resources resources, boolean side, boolean lane){
        super("Robot",2,50,36,2000, 25, true, side, lane, resources);

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
                if(isDiagonal()){
                    if(isLeft()){
                        currentBitmap = diagArray[(frame/4)%4];
                    }
                    else{
                        currentBitmap = diagArray[(frame/4)%4+4];
                    }
                }
                else {
                    currentBitmap = southArray[(frame/4)%4];
                }
            }
            else {
                if(isDiagonal()){
                    if(isLeft()){
                        currentBitmap = diagupArray[(frame/4)%4+4];
                    }
                    else{
                        currentBitmap = diagupArray[(frame/4)%4];
                    }
                }
                else {
                    currentBitmap = northArray[(frame/4)%4];
                }
            }
        }
        canvas.drawBitmap(currentBitmap, getX(), getY(), null);
    }

    @Override
    public void update(int frame){
        if(getY()>Resources.getSystem().getDisplayMetrics().heightPixels-820&&
                getY()<Resources.getSystem().getDisplayMetrics().heightPixels-520){
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
            if (!getSplashDamage()) break;
        }
    }
}
