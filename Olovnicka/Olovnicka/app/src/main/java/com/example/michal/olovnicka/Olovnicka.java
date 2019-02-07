package com.example.michal.olovnicka;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Olovnicka implements Runnable {

    private int width;
    private int height;
    private int offsetX = 0;
    private int size = 0;
    private Paint white = new Paint();
    private Paint red = new Paint();
    protected boolean running;

    public Olovnicka(int width, int height)  {
        this.width = width;
        this.height = height;
        this.running = true;
        this.white.setARGB(255, 255, 255, 255);
        this.red.setARGB(255, 255, 100, 100);
    }

    public int getWidth()  {
        return this.width;
    }

    public int getHeight()  {
        return this.height;
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(width/3 + offsetX + size, height/3, width/3*2 + offsetX - size, height/3, white);
        canvas.drawLine(width/3*2 + offsetX - size, height/3, width/3*2, height/3*2, white);
        canvas.drawLine(width/3*2, height/3*2, width/3, height/3*2, white);
        canvas.drawLine(width/3, height/3*2, width/3 + offsetX + size, height/3, white);

        canvas.drawLine(width/2 + offsetX, height/3, width/2, height/3*2, red);
    }

    @Override
    public void run() {
        long startTime, now, waitTime;
        Random rand = new Random();
        while (running) {
            startTime = System.nanoTime();
            offsetX = (int) (MainActivity.getAccelerationX() * width / 18);
            size = (int) (-MainActivity.getAccelerationZ() * width / 36);
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
