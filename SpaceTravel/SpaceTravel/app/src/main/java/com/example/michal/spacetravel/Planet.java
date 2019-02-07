package com.example.michal.spacetravel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Planet implements Runnable{

    protected Bitmap image;
    protected int size;
    protected int x;
    protected double y;
    protected double maxY;
    protected double distance;
    protected boolean running;

    public Planet(Bitmap image, int x, double y, int maxY, double distance)  {
        this.image = image;
        this.x = x;
        this.y = y;
        this.maxY = maxY;
        this.distance = distance;
        this.running = true;
    }

    public int getX()  {
        return this.x;
    }

    public double getY()  {
        return this.y;
    }

    public double getMaxY()  {
        return this.maxY;
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
                y = -200;
            } else {
                y += distance;
            }
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
