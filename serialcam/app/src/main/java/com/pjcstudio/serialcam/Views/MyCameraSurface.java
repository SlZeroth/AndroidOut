package com.pjcstudio.serialcam.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.pjcstudio.serialcam.Fragment.CameraFragment;

import java.io.IOException;
import java.util.List;

public class MyCameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;
    public Camera mCamera = null;
    public Context mContext;
    public Camera.Size mPreviewSize;
    public List<Camera.Size> mSupportedPreviewSizes;

    public int mCurrentZoomLevel = 10;
    public int mCurrentPhotoWidth = 1984;
    public int mCurrentPhotoHeight = 1488;

    public int runFirst = 0;

    public MyCameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mContext = context;
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public MyCameraSurface(Context context) {
        super(context);
        mHolder = getHolder();
        mContext = context;
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(mCamera != null) {
                if(CameraFragment.isTakePhoto == false) {
                    try {
                        mCamera.autoFocus(null);
                        Camera.Size size = mCamera.getParameters().getPictureSize();
                        Log.d("SIZE", "h : " + size.height + "   w : " + size.width);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if(mCamera == null) {
            mCamera = Camera.open();
        } else {
            mCamera.startPreview();
        }
        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        try {
            //mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);

        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }


    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();


            mCamera.release();
            mCamera = null;

            Log.d("Destory", "de");
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
        /*
        Log.d("surfaceChagned", "Called");
        Camera.Parameters mParameters = mCamera.getParameters();
        Camera.Size previewSize = null;
        Camera.Size pictureSize = null;

        List<Camera.Size> sizeList = mParameters.getSupportedPreviewSizes();
        List<Camera.Size> sizePicture = mParameters.getSupportedPictureSizes();
        previewSize = sizeList.get(0);

        for(int i=0;i<sizeList.size();i++) {
            Log.d("PreviewSize", "width : " + sizeList.get(i).width + " height : " + sizeList.get(i).height);
        }
        pictureSize = sizePicture.get(sizePicture.size()/2);
        Log.d("PictureSize", "width : " + pictureSize.width + " height : " + pictureSize.height);

        Log.d("MAX ZOOM", "" + mParameters.getMaxZoom());

        mParameters.setPreviewSize(previewSize.width, previewSize.height);
        mParameters.setPictureSize(pictureSize.width, pictureSize.height);;
        Log.d("Quality", ""+mParameters.getJpegQuality());
        List<String> modes = mParameters.getSupportedFocusModes();
        for(int i=0;i<modes.size();i++) {
            Log.d("MODE", modes.get(i));
        }
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        Log.d("Current Mode", mParameters.getFocusMode());
        mCamera.setParameters(mParameters);
        mCamera.startPreview();

        requestLayout();
        Camera.Parameters mParameterss = mCamera.getParameters();
        mParameterss.setZoom(0);
        mCamera.setParameters(mParameterss);

        android.view.ViewGroup.LayoutParams lp = this.getLayoutParams();
        */

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            Log.d("before", parameters.getJpegQuality() + "");
            parameters.setJpegQuality(100);
            SharedPreferences pref = mContext.getSharedPreferences("SAVE", Context.MODE_PRIVATE);
            String h = pref.getString("SCREENSIZE_H", "");
            String w = pref.getString("SCREENSIZE_W", "");
            if(h.equals("") || w.equals("")) {
                Log.d("KKK", "KKK");
                parameters.setPictureSize(mCurrentPhotoWidth, mCurrentPhotoHeight);
            } else {
                parameters.setPictureSize(Integer.parseInt(w), Integer.parseInt(h));
            }
            Log.d("now", parameters.getJpegQuality() + "");
            mCamera.startPreview();

                parameters.setZoom(mCurrentZoomLevel);

            mCamera.setParameters(parameters);

            Log.d("WhiteBalance", mCamera.getParameters().getWhiteBalance());
        } catch (Exception e){
            Log.d("CAM", "Error starting camera preview: " + e.getMessage());
        }

    }

    public void setScreenSize(int width, int height) {
        android.view.ViewGroup.LayoutParams lp = this.getLayoutParams();
        if((width == 1024 && height == 768) || (width == 1984 && height == 1488) || (width == 2592 && height == 1944) || (width == 3648 && height == 2736)) {
            lp.width = 720; // required width
            lp.height = 480; // required height
            this.setLayoutParams(lp);
        } else {
            /*
            DisplayMetrics displayMetrics = new DisplayMetrics();
            lp.width = 960; // required width
            //lp.height = 540; // required height
            this.setLayoutParams(lp); */
        }
    }

    public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


}