package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

class Fireball extends Entity {
    private final Bitmap southSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.south6_1), 60, 100, false);
    private final Bitmap northSprite = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.north6_1), 60, 100, false);

    private final Bitmap[] deathArray = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.explode6_1), 70, 70, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.explode6_2), 70, 70, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.explode6_3), 70, 70, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.explode6_4), 70, 70, false)};


    private Bitmap currentBitmap = northSprite;

    private int startFrame = 0;

    public Fireball(Resources resources, boolean side, boolean lane){
        super("Fireball",16,500,12,1, 15, true, side, lane, resources);

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
            setY(Resources.getSystem().getDisplayMetrics().heightPixels-120);
        }
    }

    @Override
    public void draw(Canvas canvas, int frame){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        if (frame % 4 == 0 && startFrame==0) {
            if(isSouth()) {
                currentBitmap = southSprite;
            } else {
                currentBitmap = northSprite;
            }
        }

        if(!getEnemies().isEmpty() && startFrame==0){
            startFrame = frame;
        }

        if(startFrame>1){
            currentBitmap = deathArray[Math.floorDiv(frame - startFrame, 10)];
        }

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
            fighting(e, frame);
            if (getSplashDamage()) break;
        }
    }
}
