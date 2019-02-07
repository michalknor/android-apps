package com.example.michal.spacetravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static Context context;
    static protected double acceleration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        this.setContentView(new GameSurface(this));
        SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        int sensorType = Sensor.TYPE_ACCELEROMETER;
        sm.registerListener(myOrientationListener,
                sm.getDefaultSensor(sensorType),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    final SensorEventListener myOrientationListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                acceleration = -sensorEvent.values[0] / 2;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public static double getAcceleration() {
        return acceleration;
    }
}
