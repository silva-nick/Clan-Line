package com.test.nick.soccerapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Entity {
    private String name;
    private int speed,  damage, attackSpeed, health;
    private int x, y;
    private boolean side;
    static Resources resources;

    public Entity(String name, int speed, int damage, int attackSpeed, int health, boolean side, Resources maps) {
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.health = health;
        this.side = side;
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

    public abstract void draw(Canvas canvas, int frame);

    public abstract void update();
}
