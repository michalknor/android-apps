package com.example.michal.spacetravel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Asteroid implements Runnable {

    protected Bitmap image;
    protected int x;
    protected double y;
    protected int sX;
    protected int sY;
    protected int maxX;
    protected int maxY;
    protected int size;
    protected int maxSize;
    protected double distance;
    protected boolean running;

    public Asteroid(Bitmap image, int x, double y, int maxX, int maxY, int size, int maxSize, double distance)  {
        this.image = image;
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
        this.size = size / 2;
        this.sX = x + this.size;
        this.sY = (int)y + this.size;
        this.maxSize = maxSize;
        this.distance = distance;
        this.running = true;
    }

    public int getX()  {
        return this.x;
    }

    public double getY()  {
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

    public int getMaxY()  {
        return this.maxY;
    }

    public int getSize()  {
        return this.size;
    }

    public int getMaxSize()  {
        return this.maxSize;
    }

    public double getDistance()  {
        return this.distance;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, (int) y, null);
    }

    @Override
    public void run() {
        long startTime, now, waitTime;
        Random rand = new Random();
        while (true) {
            startTime = System.nanoTime();
            if (y > maxY) {
                x = rand.nextInt(maxX) - maxSize / 2;
                y = -rand.nextInt(400) - 100 - size;
                this.sX = x + size;
                distance += 0.1;
            } else {
                y += distance;
            }
            this.sY = (int)y + size;
            now = System.nanoTime();
            waitTime = (now - startTime)/10000000;
            if(waitTime < 10)  {
                waitTime = 10; // Millisecond.
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
