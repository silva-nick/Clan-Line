package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

public abstract class Entity {
    private static final String TAG = "Entity";
    private String name;
    private int speed,  damage, attackSpeed, health;
    private int x, y;
    private boolean side, lane;
    private ArrayList<Entity> enemies = new ArrayList<Entity>();
    static Resources resources;

    public Entity(String name, int speed, int damage, int attackSpeed, int health, boolean side, boolean lane, Resources maps) {
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.health = health;
        this.side = side;
        this.lane = lane;
        resources = maps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isSouth() {
        return side;
    }

    public boolean isLeft(){
        return lane;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    static Resources getResources(){
        return resources;
    }

    public boolean isCollided(Entity e){
        if(isSouth() && e.isSouth() || !isSouth() && !e.isSouth()){
            return false;
        }
        else if((isSouth() && !e.isSouth()) && (this.getY()+210 > e.getY()-20)){
            return true;
        }
        else if((!isSouth() && e.isSouth()) && (this.getY()-20 < e.getY()+210)){
            return true;
        }
        return false;
    }

    public void setFighting(Entity e){
        if(e==null){
            enemies.remove(e);
            Log.d(TAG, "Enemy removed");
        }
        if(enemies.contains(e)){return;}
        enemies.add(e);
    }

    public boolean isFighting(){
        return !enemies.isEmpty();
    }

    public ArrayList<Entity> getEnemies() {
        return enemies;
    }

    public void fighting(Entity enemy, int frame){
        if(frame%enemy.getAttackSpeed()==0){
            this.setHealth(this.getHealth()-enemy.getDamage());
        }
    }

    public abstract void draw(Canvas canvas, int frame);

    public abstract void update(int frame);
}
