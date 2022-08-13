package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

class Base extends Entity {
    private final Bitmap[] bitmaps = new Bitmap[]{Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.base), 240, 240, false),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Entity.getResources(), R.drawable.base_damage), 240, 240, false)};


    private Bitmap currentBitmap = bitmaps[0];

    public Base(Resources resources, boolean side, boolean lane){
        super("Base",10,15,10,3000, 25, true, side, lane, resources);
        setX(Resources.getSystem().getDisplayMetrics().widthPixels/2-120);
        setY(Resources.getSystem().getDisplayMetrics().heightPixels-570);
    }

    @Override
    public void draw(Canvas canvas, int frame){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        canvas.drawText(String.valueOf(getHealth()), getX()-5, getY()-5, paint);

        if (isFighting() && frame%12==0){
            if (currentBitmap == bitmaps[0]){
                currentBitmap = bitmaps [1];
            }
            else{
                currentBitmap = bitmaps[0];
            }
        }
        else {
            canvas.drawBitmap(currentBitmap, getX(), getY(), null);
        }
    }

    @Override
    public void update(int frame){
        ArrayList<Entity> enemies = getEnemies();
        for(Entity e : enemies){
            attack(e, frame);
        }
    }
}
