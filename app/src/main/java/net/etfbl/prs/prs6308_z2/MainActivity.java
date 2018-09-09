package net.etfbl.prs.prs6308_z2;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.*;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/****************************************************************************
 *
 * Copyright (c) 2017 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file CameraView.java
 * \brief
 * This our main activity for azimuth calculation application.
 *
 * Created on 30.04.2017.
 *
 * @Author Milan Gogic
 *
 **********************************************************************/

public class MainActivity extends AppCompatActivity  implements SensorEventListener {

    public static final int FORTY_FIVE_DEGREES_IN_RADIAN = 45;
    public static final int ONE_THIRTY_FIVE_DEGREES_IN_RADIAN = 135;


    private Camera mCamera;
    private CameraView cameraView;

    private Sensor sensorAccelerometer;
    private TextView tv;
    private SensorManager sensorManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.txtView);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ROTATION_VECTOR);
        try {
            mCamera = Camera.open();

        } catch(Exception e) {
            Log.d(getResources().getString(R.string.error), "Failed to get camera: " + e.getMessage());
        }
        if(mCamera != null) {
            cameraView = new CameraView(this, mCamera);
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(cameraView);
        }
        ImageButton imgX = (ImageButton) findViewById(R.id.imgX);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int azimuth = 0;
        float[] mRotationMatrix = new float[9];
        float[] orientationVals = new float[3];
        SensorManager.getRotationMatrixFromVector(mRotationMatrix,
                event.values);
        float orientation = (float) Math.acos(mRotationMatrix[8]);

        SensorManager.remapCoordinateSystem(mRotationMatrix,
                    SensorManager.AXIS_X, SensorManager.AXIS_Z,
                    mRotationMatrix);
        SensorManager.getOrientation(mRotationMatrix, orientationVals);
        orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
        azimuth = Math.round(orientationVals[0]);
        if(azimuth < 0) {
            azimuth += 360;
        }
        tv.setText(String.valueOf(azimuth));
    }
}
