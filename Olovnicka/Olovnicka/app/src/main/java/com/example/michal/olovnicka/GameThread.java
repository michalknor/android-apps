package com.example.michal.olovnicka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

public class GameThread extends Thread {
    ArrayList<Thread> asteroidsThread;
    private boolean running;
    private GameSurface gameSurface;
    private SurfaceHolder surfaceHolder;

    public GameThread(GameSurface gameSurface, SurfaceHolder surfaceHolder)  {
        this.gameSurface = gameSurface;
        this.surfaceHolder = surfaceHolder;
        int size = this.gameSurface.getWidth() / 10;
//        generateAsteroids(3, size);
    }

    @Override
    public void run()  {
        long startTime;

        while(running)  {
            startTime = System.nanoTime();
            Canvas canvas = null;
            try {
                // Get Canvas from Holder and lock it.
                canvas = this.surfaceHolder.lockCanvas();

                // Synchronized
                synchronized (canvas)  {
                    this.gameSurface.draw(canvas);
                }
            }
            catch(Exception e)  {
                // Do nothing.
            }
            finally {
                if(canvas != null)  {
                    // Unlock Canvas.
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.nanoTime() ;
            // Interval to redraw game
            // (Change nanoseconds to milliseconds)
            long waitTime = (now - startTime)/10000000;
            if(waitTime < 20)  {
                waitTime= 20; // Millisecond.
            }

            try {
                // Sleep.
                this.sleep(waitTime);
            }
            catch(InterruptedException e)  {

            }
        }
    }

    public void setRunning(boolean running)  {
        this.running= running;
    }
}

