package com.pjcstudio.serialcam.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pjcstudio.serialcam.Activity.MainActivity;
import com.pjcstudio.serialcam.ApplicationClass;
import com.pjcstudio.serialcam.R;
import com.pjcstudio.serialcam.Tools.WaterMark;
import com.pjcstudio.serialcam.Utility.OnBackPress;
import com.pjcstudio.serialcam.Views.MyCameraSurface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by juchanpark on 2015. 9. 2..
 */
public class CameraFragment extends Fragment implements
        Camera.PreviewCallback,
        Camera.AutoFocusCallback,
        Camera.PictureCallback ,
        OnBackPress{

    private ApplicationClass applicationClass;
    public MyCameraSurface mSurface;
    public static Boolean isCameraOn = true;
    public static Boolean isTakePhoto = false;

    private int runFirst = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).press = this;

        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {
        applicationClass = (ApplicationClass) getActivity().getApplicationContext();
        mSurface = (MyCameraSurface) v.findViewById(R.id.previewFrame);

        v.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pluszoom();
            }
        });

        v.findViewById(R.id.noplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nopluszoom();
            }
        });
    }

    public void TakePicture() {
        if(applicationClass.checkPhotoData() == 0) {
            mSurface.mCamera.autoFocus(this);
        } else {
            Toast.makeText(getActivity(), "업체명 또는 디바이스ID 를 설정하지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        if(b == true) {
                isTakePhoto = true;
                mSurface.mCamera.takePicture(null, null, this);
            Toast.makeText(getActivity(), "사진촬영 완료", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "초점이 안맞습니다", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, final Camera camera) {
        Log.d("onPictureTaken", "Called");

        SharedPreferences pref = getActivity().getSharedPreferences("SAVE", Context.MODE_PRIVATE);

        /* 사진명에 사용할 정보 초기화 */
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String fileNum = pref.getString("PHOTOCOUNTER", "");
        int yearz = 3020;
        int monthz = 30;
        int dayz = 1;

        if(fileNum.equals("")) {
            fileNum = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).listFiles().length);
        }

        /* 사진파일에 워터마크 그리고 파일로 저장 */
        String fileName = "" + year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + fileNum + ".jpeg";
        File targetFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
        Bitmap byteBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Bitmap copyByteBitmap = byteBitmap.copy(Bitmap.Config.ARGB_8888, true);
        WaterMark.setWatermark(getActivity(), copyByteBitmap, applicationClass.companyName, applicationClass.deviceName, fileNum);
        byte[] byteByte = bitmapToByteArray(copyByteBitmap);
        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            fos.write(byteByte);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String pathSdcard = ((ApplicationClass) getActivity().getApplicationContext()).findSdcardPath();
        try {
            if(pathSdcard != null) {
                FileOutputStream fos = new FileOutputStream(new File(pathSdcard+fileName));
                fos.write(byteByte);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PHOTOCOUNTER", String.valueOf(Integer.valueOf(fileNum) + 1));
        editor.commit();

        CameraFragment.camera = camera;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    camera.startPreview();
                    isTakePhoto = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
        System.gc();
    }

    public static Camera camera;

    @Override
    public void onBack() {

        try {
            isTakePhoto = false;
            if (camera != null) camera.startPreview();
            Log.d("tag", "false");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void setResolution(int width, int height) {
        SharedPreferences pref = getActivity().getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SCREENSIZE_H", String.valueOf(height));
        editor.putString("SCREENSIZE_W", String.valueOf(width));
        editor.commit();
        mSurface.mCamera.stopPreview();
        Camera.Parameters parameters = mSurface.mCamera.getParameters();
        mSurface.mCurrentPhotoHeight = height;
        mSurface.mCurrentPhotoWidth = width;
        parameters.setPictureSize(width, height);

        //Camera.Size size = mSurface.getOptimalPreviewSize(mSurface.mCamera.getParameters().getSupportedPreviewSizes(), width, height);
        //parameters.setPreviewSize(size.width, size.height);
        //mSurface.requestLayout();
        //mSurface.setScreenSize(width, height);
        mSurface.mCamera.setParameters(parameters);
        mSurface.mCamera.startPreview();
    }


    public void pluszoom() {
        if(mSurface.mCamera != null) {
            try {
                Camera.Parameters parameters = mSurface.mCamera.getParameters();
                int currentZoom = parameters.getZoom();
                if (currentZoom >= 10) {
                    return;
                } else {
                    parameters.setZoom(currentZoom + 1);
                    mSurface.mCurrentZoomLevel++;
                }
                mSurface.mCamera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void nopluszoom() {
        if(mSurface.mCamera != null) {
            try {
                Camera.Parameters parameters = mSurface.mCamera.getParameters();
                int currentZoom = parameters.getZoom();
                if (currentZoom <= 1) {
                    return;
                } else {
                    parameters.setZoom(currentZoom - 1);
                    mSurface.mCurrentZoomLevel--;
                }
                mSurface.mCamera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void CameraOn() {
        if(isCameraOn == false) {
            mSurface.setVisibility(View.VISIBLE);
            isCameraOn = true;
        }
    }

    public void CameraOff() {
        if(isCameraOn) {
            mSurface.setVisibility(View.INVISIBLE);
            isCameraOn = false;
        }
    }

    public void sendImageData(String ip, int port, byte[] byteImage) {
        Thread thread = new Thread(new SendImageRunable(ip, port, byteImage, getActivity()));
        thread.start();
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        isTakePhoto = false;
    }

    public class SendImageRunable implements Runnable {

        public int ServerPort = 2128;
        public String ServerIP = null;
        public byte[] bytes;
        public Activity mActivity;
        public ProgressDialog progressDialog = null;

        private Socket socket = null;
        private OutputStream outputStream;
        private BufferedReader networkReader;
        private BufferedWriter networkWriter;

        public SendImageRunable(String ip, int port, byte[] bytes, Activity activity) {
            ServerIP = ip;
            ServerPort = port;
            mActivity = activity;

            this.bytes = bytes;
        }

        public int setSocket(String ip, int port) throws IOException {
            try {
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("SAVE", Context.MODE_PRIVATE);
                socket = new Socket(sharedPreferences.getString("IPCOMPUTER", ""), port);
                outputStream = socket.getOutputStream();
                networkWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                return 1;
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        public void run() {

            int isSuccess = 1;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = ProgressDialog.show(mActivity, "", "잠시만 기다려주세요.", true);
                }
            });
            try {
                if(setSocket(ServerIP, ServerPort) == 0) {
                    isSuccess = 0;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "서버에 접속하지 못했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                exitProgress();
            }

            if(isSuccess == 1) {
                try {
                    outputStream.write(bytes);
                    outputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    exitProgress();
                }
            }

            exitProgress();
            //isTakePhoto = false;
        }

        private void exitProgress() {

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
        }

    }
}
