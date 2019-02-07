package com.example.michal.spacetravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    ArrayList<Star> stars;
    ArrayList<Thread> starsThread;
    ArrayList<Asteroid> asteroids;
    ArrayList<Thread> asteroidsThread;
    ArrayList<Planet> planets;
    ArrayList<Thread> planetsThread;
    private GameThread gameThread;
    private SpaceShip spaceShip;
    private Thread spaceShipThread;
    private boolean gameOver = false;
    private double score;
    private double highscore;
    private Paint paint;
    private NumberFormat formatter;
    boolean click = true;

    public GameSurface(Context context)  {
        super(context);
        // Make Game Surface focusable so it can handle events.
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        click = true;
        this.setOnClickListener(new SurfaceView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameOver && click) {
                    click = false;
                    startTheGame();
                    click = true;
                }
            }
        });

    }

    private void calculateCollision() {
        for (Asteroid asteroid: asteroids) {
            double left = Math.pow(asteroid.getSX() - this.spaceShip.getSX(), 2) + Math.pow(asteroid.getSY() - this.spaceShip.getSY(), 2);
            double right = Math.pow(asteroid.getSize() + this.spaceShip.getSize(), 2);
            boolean state = left <= right;
            if (state) {
                stopTheGame();
                break;
            }
        }
    }

    private void stopTheGame() {
        for (Star star: stars) {
            star.stop();
        }
        for (Asteroid asteroid: asteroids) {
            asteroid.stop();
        }
        for (Planet planet: planets) {
            planet.stop();
        }
        this.spaceShip.stop();
        gameOver = true;
    }

    public void draw(Canvas canvas)  {
        score += 0.02;
        super.draw(canvas);
        for (Star star : stars) {
            star.draw(canvas);
        }
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(canvas);
        }
        for (Planet planet : planets) {
            planet.draw(canvas);
        }
        this.spaceShip.draw(canvas);
        canvas.drawText("Highscore: " + formatter.format(highscore), 10, 40, paint);
        canvas.drawText("Score: " + formatter.format(score), 10, 80, paint);
        calculateCollision();
        if (gameOver) {
            canvas.drawText("GAME OVER", this.getWidth() / 2 - 100, this.getHeight() / 2 - 20, paint);
            canvas.drawText("(click to restart)", this.getWidth() / 2 - 130, this.getHeight() / 2 + 20, paint);
            MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.explosion);
            mp.start();
            if (score > highscore) {
                saveHighscore(score);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startTheGame();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(32);
        formatter = new DecimalFormat("#0.00");
        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    private void startTheGame() {
        loadHighscore();
        score = 0;
        int size = this.getWidth() / 10;
        generateShip(size);
        generateStars(50);
        generatePlanets();
        generateAsteroids(3, size);
        gameOver = false;
    }

    private void generateShip(int size) {
        int x = (this.getWidth() / 2) - (size / 2);
        int y = this.getHeight() / 10 * 9;
        Bitmap spaceShipBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.spaceship);
        spaceShipBitmap = Bitmap.createScaledBitmap(spaceShipBitmap, size, size, false);
        this.spaceShip = new SpaceShip(spaceShipBitmap, x, y, this.getWidth() - size, size);
        this.spaceShipThread = new Thread(this.spaceShip);
        spaceShipThread.start();
    }

    private void generateStars(int count) {
        stars = new ArrayList<Star>();
        starsThread = new ArrayList<Thread>();
        Bitmap[] starMaps = {
                BitmapFactory.decodeResource(this.getResources(),R.drawable.star1),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.star2),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.star2)
        };
        int maxX = this.getWidth();
        int maxY = this.getHeight();
        int maxSize = maxX / 100;
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(maxX) - maxSize/2;
            int y = rand.nextInt(maxY + 200) - maxSize/2 - 200;
            double distance = rand.nextDouble() / 50 + 0.2;
            int size = rand.nextInt(maxSize) + 10;
            int index = rand.nextInt(3);
            Bitmap starBitmap = Bitmap.createScaledBitmap(starMaps[index], size, size, false);
            Star star = new Star(starBitmap, x, y, maxY, distance);
            stars.add(star);
            Thread starThread = new Thread(star);
            starThread.start();
            starsThread.add(starThread);
        }
    }

    private void generateAsteroids(int count, int maxSize) {
        asteroids = new ArrayList<Asteroid>();
        asteroidsThread = new ArrayList<Thread>();
        Bitmap[] asteroidMaps = {
                BitmapFactory.decodeResource(this.getResources(),R.drawable.asteroid1),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.asteroid2),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.asteroid3)
        };
        int maxX = this.getWidth();
        int maxY = this.getHeight();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(maxX) - maxSize/2;
            int y = - rand.nextInt(maxY + 200);
            double distance = rand.nextDouble() + 0.5;
            int size = rand.nextInt(maxSize) + maxSize;
            Bitmap asteroidBitmap = Bitmap.createScaledBitmap(asteroidMaps[i], size, size, false);
            Asteroid asteroid = new Asteroid(asteroidBitmap, x, y, maxX, maxY, size, maxSize, distance);
            asteroids.add(asteroid);
            Thread asteroidThread = new Thread(asteroid);
            asteroidThread.start();
            asteroidsThread.add(asteroidThread);
        }
    }

    private void generatePlanets() {
        planets = new ArrayList<Planet>();
        planetsThread = new ArrayList<Thread>();
        Bitmap[] planetMaps = {
                BitmapFactory.decodeResource(this.getResources(),R.drawable.planet1),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.planet2),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.planet3)
        };
        int maxX = this.getWidth();
        int maxY = this.getHeight();
        int maxSize = maxX / 10;
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            int x = rand.nextInt(maxX) - maxSize/2;
            int y = rand.nextInt(maxY + 200) - maxSize/2 - 200;
            double distance = rand.nextDouble() / 50 + 0.4;
            int size = rand.nextInt(maxSize) + 10;
            Bitmap starBitmap = Bitmap.createScaledBitmap(planetMaps[i], size, size, false);
            Star star = new Star(starBitmap, x, y, maxY, distance);
            stars.add(star);
            Thread starThread = new Thread(star);
            starThread.start();
            starsThread.add(starThread);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }

    public boolean getGameOver() {
        return gameOver;
    }


    public void saveHighscore(double score) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putString("highscore", score+"");
        mEdit1.commit();
    }

    public void loadHighscore() {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        highscore = Double.parseDouble(mSharedPreference1.getString("highscore", "0"));
    }
}