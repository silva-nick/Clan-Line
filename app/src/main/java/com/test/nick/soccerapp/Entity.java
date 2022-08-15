package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

public abstract class Entity {
    private static final String TAG = "Entity";
    private String name;
    private final int speed;
    private int damage;
    private final int attackSpeed;
    private int health;
    private final int range;
    private int x, y;
    private final boolean side;
    private final boolean lane;
    private final boolean splashDamage;
    private boolean diagonal;
    private final ArrayList<Entity> enemies = new ArrayList<Entity>();
    static Resources resources;

    public Entity(String name, int speed, int damage, int attackSpeed, int health, int range, boolean splashDamage, boolean side, boolean lane, Resources maps) {
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.health = health;
        this.range = range;
        this.splashDamage = splashDamage;
        this.side = side;
        this.lane = lane;
        resources = maps;
    }

    public boolean getSide() {return side;}

    public boolean getLane() {return lane;}

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

    public void setDamage(int damage){
        this.damage = damage;
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

    public int getRange() { return this.range; }

    public boolean getSplashDamage() { return this.splashDamage; }

    public boolean isSouth() {
        return side;
    }

    public boolean isLeft(){
        return lane;
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
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
        else if((isSouth() && !e.isSouth()) && (this.getY()+200+range > e.getY())){
            return true;
        }
        else return (!isSouth() && e.isSouth()) && (this.getY() < e.getY()+200+range);
    }

    public void setFighting(Entity e){
        if(e == null){
            enemies.remove(e);
            Log.d(TAG, "Enemy removed");
        }

        if(enemies.contains(e)){ return; }
        enemies.add(e);
    }

    public boolean isFighting(){
        return !enemies.isEmpty();
    }

    public ArrayList<Entity> getEnemies() {
        return enemies;
    }

    public void attack(Entity enemy, int frame) {
        if (enemy.getName().equals("Fireball") || enemy.getName().equals("Rock")) {
            return;
        }

        if(frame % this.getAttackSpeed() == 0){
            enemy.setHealth(enemy.getHealth() - this.getDamage());
        }
    }

    public abstract void draw(Canvas canvas, int frame);

    public abstract void update(int frame);
}
