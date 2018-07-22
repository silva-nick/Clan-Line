package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

class Fighter extends Entity {
    private Bitmap[] southArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south2_1), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south2_2), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south2_3), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south2_4), 120, 180, false)};
    private Bitmap[] northArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north2_1), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north2_2), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north2_3), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north2_4), 120, 180, false)};
    private Bitmap[] diagArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag2_1), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag2_2), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag2_3), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diag2_4), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr2_1), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr2_2), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr2_3), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagr2_4), 120, 180, false)};
    private Bitmap[] diagupArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup2_1), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup2_2), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup2_3), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagup2_4), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup2_1), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup2_2), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup2_3), 120, 180, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.diagrup2_4), 120, 180, false)};

    private Bitmap currentBitmap = northArray[0];

    public Fighter(Resources resources, boolean side, boolean lane){
        super("Fighter",3,20,8,700, side, lane, resources);

        if(isSouth()){
            setX(isLeft() ? (180) : (Resources.getSystem().getDisplayMetrics().widthPixels-340));
        }
        else{
            setX(Resources.getSystem().getDisplayMetrics().widthPixels/2-60);
        }


        if(isSouth()) {
            setY(-50);
        }
        else{
            setY(Resources.getSystem().getDisplayMetrics().heightPixels-60);
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
        if(getY()>Resources.getSystem().getDisplayMetrics().heightPixels-780&&
                getY()<Resources.getSystem().getDisplayMetrics().heightPixels-500){
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
