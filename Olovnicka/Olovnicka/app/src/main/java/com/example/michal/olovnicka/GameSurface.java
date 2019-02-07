package com.example.michal.olovnicka;

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
    private GameThread gameThread;
    private Paint paint;
    private NumberFormat formatter;
    private Thread olovnickaThread;
    private Olovnicka olovnicka;

    public GameSurface(Context context)  {
        super(context);
        // Make Game Surface focusable so it can handle events.
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    public void draw(Canvas canvas)  {
        super.draw(canvas);
        olovnicka.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.olovnicka = new Olovnicka(this.getWidth(), this.getHeight());
        this.olovnickaThread = new Thread(this.olovnicka);
        olovnickaThread.start();
        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
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
}