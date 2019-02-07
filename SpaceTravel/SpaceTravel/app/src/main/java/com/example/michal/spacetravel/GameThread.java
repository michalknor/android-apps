package com.example.michal.spacetravel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

public class GameThread extends Thread {
    ArrayList<Asteroid> asteroids;
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
            if (this.gameSurface.getGameOver()) {
                try {
                    this.sleep(20);
                }
                catch(InterruptedException e)  {

                }
                continue;
            }
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

//    private void generateAsteroids(int count, int maxSize) {
//        asteroids = new ArrayList<Asteroid>();
//        asteroidsThread = new ArrayList<Thread>();
//        Bitmap[] asteroidMaps = {
//                BitmapFactory.decodeResource(this.gameSurface.getResources(),R.drawable.asteroid1),
//                BitmapFactory.decodeResource(this.gameSurface.getResources(),R.drawable.asteroid2),
//                BitmapFactory.decodeResource(this.gameSurface.getResources(),R.drawable.asteroid3)
//        };
//        int maxX = this.gameSurface.getWidth();
//        int maxY = this.gameSurface.getHeight();
//        Random rand = new Random();
//        for (int i = 0; i < count; i++) {
//            int x = rand.nextInt(maxX) - maxSize/2;
//            int y = - rand.nextInt(maxY + 200);
//            double distance = rand.nextDouble() * 5 + 2;
//            int size = rand.nextInt(maxSize) + maxSize;
//            int index = rand.nextInt(3);
//            Bitmap asteroidBitmap = Bitmap.createScaledBitmap(asteroidMaps[i], size, size, false);
//            Asteroid asteroid = new Asteroid(asteroidBitmap, x, y, maxX, maxY, maxSize, distance);
//            asteroids.add(asteroid);
//            Thread asteroidThread = new Thread(asteroid);
//            asteroidThread.start();
//            starsThread.add(asteroidThread);
//        }
//    }
}
