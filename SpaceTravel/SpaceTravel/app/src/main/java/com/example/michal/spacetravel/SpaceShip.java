package com.example.michal.spacetravel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;

import java.util.Random;

import static java.lang.Thread.sleep;

public class SpaceShip implements Runnable {

    protected Bitmap image;
    protected int x;
    protected int y;
    protected int sX;
    protected int sY;
    protected int maxX;
    public double acceleration;
    protected int size;
    protected boolean running;

    public SpaceShip(Bitmap image, int x, int y, int maxX, int size)  {
        this.image = image;
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.acceleration = 0;
        this.size = size / 2;
        this.sX = x + this.size / 2;
        this.sY = y + this.size / 2;
        this.running = true;
    }

    public int getX()  {
        return this.x;
    }

    public int getY()  {
        return this.y;
    }

    public int getSX()  {
        return this.sX;
    }

    public int getSY()  {
        return this.sY;
    }

    public int getMaxX()  {
        return this.maxX;
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public int getSize() {
        return this.size;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    @Override
    public void run() {
        long startTime, now, waitTime;
        Random rand = new Random();
        while (running) {
            startTime = System.nanoTime();
            //Acceleration change
            //The bigger acceleration is the harder is to control the ship
            acceleration += MainActivity.getAcceleration() / 20;
            //Move ship
            x += acceleration;
            if (x < 10) {
                x = 10;
            }
            else if (x > maxX - 10) {
                x = maxX - 10;
            }
            this.sX = x + size;
            now = System.nanoTime() ;
            waitTime = (now - startTime)/10000000;
            if(waitTime < 20)  {
                waitTime = 20; // Millisecond.
            }
            try {
                sleep(waitTime);
            } catch (InterruptedException e) {

            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
