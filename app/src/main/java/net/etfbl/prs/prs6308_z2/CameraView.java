package net.etfbl.prs.prs6308_z2;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/****************************************************************************
 *
 * Copyright (c) 2017 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file CameraView.java
 * \brief
 * This class represents custom camera view that we will use in our application
 *
 * Created on 30.04.2017.
 *
 * @Author Milan Gogic
 *
 **********************************************************************/


public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Camera mCamera;

    public CameraView(Context context, Camera camera) {

        super(context);

        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch(IOException e) {
            Log.d(getResources().getString(R.string.error), "Camera error on surfaceCreated" + e.getMessage());
        }

    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int i, int j, int k) {

        if(holder.getSurface() == null) {
            return;
        }
        try{
            mCamera.stopPreview();

        } catch(Exception e) {

        }

        try {

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch(IOException e) {
            Log.d(getResources().getString(R.string.error), "Camera error on surface changed" + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        mCamera.stopPreview();
        mCamera.release();
    }
}
