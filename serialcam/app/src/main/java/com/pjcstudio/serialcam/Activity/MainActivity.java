package com.pjcstudio.serialcam.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pjcstudio.serialcam.ApplicationClass;
import com.pjcstudio.serialcam.DialogFragment.MoveFileDialogFragment;
import com.pjcstudio.serialcam.DialogFragment.OptionDialogFragment;
import com.pjcstudio.serialcam.Fragment.CameraFragment;
import com.pjcstudio.serialcam.Items.SupportPictureSizeItems;
import com.pjcstudio.serialcam.R;
import com.pjcstudio.serialcam.Service.ScreenService;
import com.pjcstudio.serialcam.Utility.OnBackPress;
import com.pjcstudio.serialcam.Utility.UsbRunable;
import com.pjcstudio.serialcam.Utility.Values;
import com.pjcstudio.serialcam.Views.MyCameraSurface;
import com.pjcstudio.serialcam.usbserial.driver.UsbSerialDriver;
import com.pjcstudio.serialcam.usbserial.driver.UsbSerialPort;
import com.pjcstudio.serialcam.usbserial.driver.UsbSerialProber;
import com.pjcstudio.serialcam.usbserial.util.SerialInputOutputManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    public OnBackPress press;

    @Override
    protected void onDestroy() {
        Log.d("TAG", "destroy");
        super.onDestroy();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_CAMERA == keyCode) {
            Log.d("Camera", "True");
            takePhoto();
        }
        return true;
    }



    public static MainActivity app;
    public static CameraFragment cameraFragment;
    private ApplicationClass applicationClass;
    private Toolbar toolbar;
    private DrawerLayout dlDrawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle dtToggle;
    private int sendThreadFlag = 0;
    public ArrayList<SupportPictureSizeItems> mResolutionList;

    private UsbManager mUsbManager;
    private UsbSerialPort sPort = null;
    private TextView textState;

    PowerManager.WakeLock wl;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    static public int appWorkState;

    public static final int DIALOG_COMPANYNAME = 0;
    public static final int DIALOG_DEVICENAME = 1;
    public static final int DIALOG_ANDROID_IPSET = 2;
    public static final int DIALOG_COMPUTER_IPSET = 3;
    public static final int DIALOG_PASSWORDSET = 4;
    public static final int DIALOG_PHOTOCOUNTER = 5;
    public static final int DIALOG_DAYS = 6;


    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d("onRunError", "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(appWorkState == 1) {
                                if (data.length == 1) {

                                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                                    List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);
                                    String topActivityName = Info.get(0).topActivity.getShortClassName();

                                    if (new String(data).equals("Q")) {
                                        if (topActivityName.equals(Values.ACTIVITY_MAIN)) {
                                            Intent reviewIntent = new Intent(MainActivity.this, GalleyActivity.class);
                                            reviewIntent.putExtra("mode", "2");
                                            startActivity(reviewIntent);
                                        }
                                    } else if (new String(data).equals("W")) {
                                        if (topActivityName.equals(Values.ACTIVITY_MAIN)) {
                                            File rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                            File files[] = rootPath.listFiles();

                                            if(files.length >=2) {

                                                Arrays.sort(files, new Comparator() {
                                                    public int compare(Object o1, Object o2) {

                                                        if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                                                            return -1;
                                                        } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                                                            return +1;
                                                        } else {
                                                            return 0;
                                                        }
                                                    }
                                                });

                                                ArrayList<String> arraySend = new ArrayList<String>();
                                                arraySend.add(files[0].toString());
                                                arraySend.add(files[1].toString());

                                                if(sendThreadFlag == 0) {
                                                    new SendImage(arraySend).start();
                                                }
                                            }
                                        }
                                    } else if (new String(data).equals("E")) {
                                        if (topActivityName.equals(Values.ACTIVITY_MAIN)) {
                                            if (!dlDrawer.isDrawerOpen(GravityCompat.START)) {
                                                dlDrawer.openDrawer(GravityCompat.START);
                                            }
                                        }
                                    } else if (new String(data).equals("Z")) {
                                        if (topActivityName.equals(Values.ACTIVITY_MAIN)) {
                                            cameraFragment.nopluszoom();
                                        } else if (topActivityName.equals(Values.ACTIVITY_GALLEY)) {
                                            GalleyActivity.app.moveLeft();
                                        }
                                    } else if (new String(data).equals("C")) {
                                        if (topActivityName.equals(Values.ACTIVITY_MAIN)) {
                                            cameraFragment.pluszoom();
                                        } else if (topActivityName.equals(Values.ACTIVITY_GALLEY)) {
                                            GalleyActivity.app.moveRight();
                                        }
                                    } else if (new String(data).equals("G")) {
                                        if (topActivityName.equals(Values.ACTIVITY_MAIN)) {
                                            takePhoto();
                                        }
                                    }
                                }
                            }

                        }
                    });
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent mirrorIntent = new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG");
        //startActivityForResult(mirrorIntent, 2);
        //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

        app = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        Log.d("height", "" + mActionBarSize);


        init();
        setLayout();
        getSupportActionBar().hide();
        //Intent intent = new Intent(this, ScreenService.class);
        //startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause", "Called");
        if(press !=null) press.onBack();
        appWorkState = 0;
        cameraFragment.mSurface.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appWorkState = 1;
        cameraFragment.mSurface.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "Called");
        appWorkState = 1;
        Log.d("appWorkState", "" + appWorkState);
    }

    private void setLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);
        dtToggle.syncState();

        cameraFragment = new CameraFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, cameraFragment).commit();
    }

    private void init() {
        applicationClass = (ApplicationClass) getApplicationContext();
        mResolutionList = new ArrayList<SupportPictureSizeItems>();
        initResolution();

        connectUSBDriver();
    }

    private void connectUSBDriver() {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        findDevice();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.drawer_review:
                Intent reviewIntent = new Intent(MainActivity.this, GalleyActivity.class);
                reviewIntent.putExtra("mode", "2");
                startActivity(reviewIntent);
                break;
            case R.id.drawer_send:
                Intent sendIntent = new Intent(MainActivity.this, GalleyActivity.class);
                sendIntent.putExtra("mode", "0");
                startActivity(sendIntent);
                break;

            case R.id.drawer_move:
                Intent moveIntent = new Intent(MainActivity.this, GalleyActivity.class);
                moveIntent.putExtra("mode", "1");
                startActivity(moveIntent);
                break;

            case R.id.drawer_setting:
                autoPassword();
                break;
        }

        dlDrawer.closeDrawers();
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openDrawer() {


            if (dlDrawer.isDrawerOpen(GravityCompat.START)) {
                dlDrawer.closeDrawer(GravityCompat.START);

                //timerDrawer();
            } else {
                dlDrawer.openDrawer(GravityCompat.START);
            }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);

        Log.d("Pointer Y", ev.getY() + "");
        Log.d("Pointer X", ev.getX() + "");
        if(ev.getAction() == MotionEvent.ACTION_UP && ev.getX() >= 620) {

            if (dlDrawer.isDrawerOpen(GravityCompat.START)) {
                dlDrawer.closeDrawer(GravityCompat.START);

                //timerDrawer();
            } else {
                dlDrawer.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }
    

    private void timerDrawer() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (dlDrawer.isDrawerOpen(GravityCompat.START)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //getSupportActionBar().hide();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSupportActionBar().hide();
                        }
                    });
                }
            }
        }, 4000);
    }

    private void initResolution() {

        Camera camera = Camera.open();
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPictureSizes();

        for(int i=0;i<sizeList.size();i++) {
            if((sizeList.get(i).width == 1024 && sizeList.get(i).height == 768) ||
                    (sizeList.get(i).width == 1920 && sizeList.get(i).height == 1080) ||
                    (sizeList.get(i).width == 1984 && sizeList.get(i).height == 1488) ||
                    (sizeList.get(i).width == 4096 && sizeList.get(i).height == 2304) ||
                    (sizeList.get(i).width == 2592 && sizeList.get(i).height == 1944) ||
                    (sizeList.get(i).width == 3648 && sizeList.get(i).height == 2736) ||
                    (sizeList.get(i).width == 4608 && sizeList.get(i).height == 2592))
            {
                String listStr = null;
                if((sizeList.get(i).width == 1024 && sizeList.get(i).height == 768) ||
                        (sizeList.get(i).width == 1984 && sizeList.get(i).height == 1488) ||
                        (sizeList.get(i).width == 2592 && sizeList.get(i).height == 1944) ||
                        (sizeList.get(i).width == 3648 && sizeList.get(i).height == 2736)) {
                    listStr = sizeList.get(i).width + " X " + sizeList.get(i).height + " (4:3)";
                } else {
                    listStr = sizeList.get(i).width + " X " + sizeList.get(i).height + " (16:9)";
                }
                mResolutionList.add(new SupportPictureSizeItems(
                        listStr,
                        sizeList.get(i).width,
                        sizeList.get(i).height
                ));
            }
        }
        camera.release();
        camera = null;
    }

    public void formatDevice() {
        ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "진행중", "잠시만 기다리세요.", true);
        File f = new File("/mnt/extSdCard/");
        if(f.canRead()) {
            File[] listFile = f.listFiles();
            for(int i=0;i<listFile.length;i++) {
                listFile[i].delete();
            }
        }
        progressDialog.dismiss();
    }

    /* 다이얼로그 생성하는 메서드 */
    public void makeInputDialog(String title, String comment, final int type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext= new EditText(this);
        alert.setMessage(comment);
        alert.setTitle(title);

        alert.setView(edittext);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("SAVE", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(type == DIALOG_COMPANYNAME) {
                    if(!YouEditTextValue.equals("")) {
                        editor.putString("COMPNAYNAME", YouEditTextValue);
                        applicationClass.companyName = YouEditTextValue;
                    }
                }
                else if(type == DIALOG_DEVICENAME) {
                    if(!YouEditTextValue.equals("")) {
                        editor.putString("DEVICENAME", YouEditTextValue);
                        applicationClass.deviceName = YouEditTextValue;
                    }
                }
                else if(type == DIALOG_ANDROID_IPSET) {
                    if(!YouEditTextValue.equals("")) {
                        editor.putString("IPANDROID", YouEditTextValue);
                        applicationClass.ipAndroid = YouEditTextValue;
                    }
                }
                else if(type == DIALOG_COMPUTER_IPSET) {
                    if(!YouEditTextValue.equals("")) {
                        editor.putString("IPCOMPUTER", YouEditTextValue);
                        applicationClass.ipComputer = YouEditTextValue;
                    }
                }
                else if(type == DIALOG_PASSWORDSET) {
                    if(!YouEditTextValue.equals("")) {
                        editor.putString("PASSWORD", YouEditTextValue);
                        applicationClass.password = YouEditTextValue;
                    }
                }
                else if(type == DIALOG_PHOTOCOUNTER) {
                    if(!YouEditTextValue.equals("")) {
                        editor.putString("PHOTOCOUNTER", YouEditTextValue);
                        applicationClass.photoCounter = YouEditTextValue;
                    }
                }
                editor.commit();
                Toast.makeText(MainActivity.this, "등록을 완료했습니다!", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.show();
    }

    private int changePassword() {
        final SharedPreferences pref = getSharedPreferences("SAVE", MODE_PRIVATE);
        final EditText editText = new EditText(this);

        if(pref.getString("PASSWORD", "").equals(""))
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setMessage("설정할 비밀번호를 입력하세요.")
                    .setTitle("새로운 비밀번호 설정")
                    .setView(editText);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(editText.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("PASSWORD", editText.getText().toString());
                        editor.commit();
                    }
                }
            });
            alert.show();
        }
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setMessage("기존 비밀번호를 입력하세요.")
                    .setTitle("새로운 비밀번호 설정")
                    .setView(editText);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(pref.getString("PASSWORD", "").equals(editText.getText().toString()))
                    {
                        final EditText editText2 = new EditText(MainActivity.this);
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("새로운 비밀번호를 입력하세요")
                                .setTitle("새로운 비밀번호 설정")
                                .setView(editText2);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("PASSWORD", editText2.getText().toString());
                                editor.commit();
                                Toast.makeText(MainActivity.this, "설정이 완료되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alert.show();

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "패스워드가 다릅니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alert.show();
        }
        return 1;
    }

    private int autoPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final SharedPreferences pref = getSharedPreferences("SAVE", MODE_PRIVATE);

        if(pref.getString("PASSWORD", "").equals(""))
        {
            final EditText editText2 = new EditText(this);
            AlertDialog.Builder alert2 = new AlertDialog.Builder(this)
                    .setMessage("설정할 비밀번호를 입력하세요.")
                    .setTitle("새로운 비밀번호 설정")
                    .setView(editText2);
            alert2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (editText2.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("PASSWORD", editText2.getText().toString());
                        editor.commit();
                        Toast.makeText(MainActivity.this, "패스워드 등록을 성공했습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alert2.show();
        } else {
            final EditText edittext = new EditText(this);
            alert.setMessage("비밀번호를 입력하세요");
            alert.setTitle("패스워드 인증");
            alert.setView(edittext);

            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SharedPreferences pref = getSharedPreferences("SAVE", MODE_PRIVATE);
                    String password = pref.getString("PASSWORD", "");

                    if (password.equals("")) {
                        Toast.makeText(MainActivity.this, "패스워드를 설정하세요", Toast.LENGTH_SHORT).show();
                    } else if (password.equals(edittext.getText().toString())) {
                        OptionDialogFragment dialogFragment = new OptionDialogFragment();
                        dialogFragment.show(getSupportFragmentManager(), "OPTIONS");
                    } else {
                        Toast.makeText(MainActivity.this, "패스워드가 틀렸습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
            alert.show();
        }

        return 1;
    }

    /* 해상도 변경하는 다이얼로그 */
    public void makeResolutionChangeDialog() {

        ArrayList<String> str = new ArrayList<String>();
        for (int i = 0; i < mResolutionList.size(); i++) {
            str.add(mResolutionList.get(i).strResolution);
        }
        final String[] items = str.toArray(new String[str.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("해상도 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                cameraFragment.setResolution(mResolutionList.get(which).width, mResolutionList.get(which).height);
                Toast.makeText(MainActivity.this, "해상도변경 완료!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void findDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final List<UsbSerialDriver> drivers =
                                UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                        final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();


                        for (final UsbSerialDriver driver : drivers) {
                            sPort = driver.getPorts().get(0);
                            if (sPort != null) {
                                Toast.makeText(MainActivity.this, "시리얼 장비 감지!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "시리얼 장치를 찾지 못했습니다", Toast.LENGTH_SHORT).show();
                                sPort = null;
                            }
                        }

                        if(sPort != null) {
                            UsbDeviceConnection connection = mUsbManager.openDevice(sPort.getDriver().getDevice());
                            if (connection == null) {
                                Toast.makeText(MainActivity.this, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "연결에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            }

                            try {
                                sPort.open(connection);
                                sPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                            } catch (IOException e) {

                                try {
                                    sPort.close();
                                    sPort = null;
                                } catch (IOException s) {
                                    s.printStackTrace();
                                }

                            }

                            if (sPort != null) {
                                onDeviceStateChange();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();



    }

    @Override
    public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) {
        SharedPreferences pref = getSharedPreferences("SAVE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("YEAR", String.valueOf(i));
        editor.putString("MONTH", String.valueOf(i1));
        editor.putString("DAY", String.valueOf(i2));
        editor.commit();
    }

    private void takePhoto() {
        if(CameraFragment.isTakePhoto == false) {
            //getSupportActionBar().hide();
            cameraFragment.TakePicture();
        } else {
            //Toast.makeText(MainActivity.this, "이미 사진 찍는중입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public class SendImage extends Thread {

        private ArrayList<String> files;
        private String ipAddr = null;
        private ProgressDialog progressDialog;

        static final int PORT = 2128;

        public SendImage(ArrayList<String> array) {
            files = array;
            ipAddr = getSharedPreferences("SAVE", MODE_PRIVATE).getString("IPCOMPUTER", "");
        }

        @Override
        public void run() {
            sendThreadFlag = 1;
            if(ipAddr != null) {
                try {
                    showProgress();

                    Socket socket = new Socket();
                    InetSocketAddress socketAddress = new InetSocketAddress(ipAddr, PORT);
                    socket.connect(socketAddress, 3000);


                    if(socket != null)
                    {
                        OutputStream outputStream = socket.getOutputStream();
                        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

                        dOut.writeInt(files.size());
                        for(int i=0;i<files.size();i++) {
                            File oFile = new File(files.get(i));
                            dOut.writeInt((int) oFile.length());
                            Log.d("size", "" + oFile.length());
                        }

                        for(int i=0;i<files.size();i++) {
                            Date date = new Date(new File(files.get(i)).lastModified());
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
                            String dateStr = format.format(date);
                            dOut.writeUTF(dateStr);
                        }

                        int nRead;
                        for(int i=0;i<files.size();i++) {

                            byte[] bytes = new byte[1024];
                            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                            FileInputStream fileInputStream = new FileInputStream(files.get(i));

                            while((nRead = fileInputStream.read(bytes, 0, bytes.length)) > 0) {
                                buffer.write(bytes, 0, nRead);
                            }

                            dOut.write(buffer.toByteArray());
                        }

                        dOut.close();
                        outputStream.close();

                    } else {
                        exitProgress();
                    }

                    socket.close();

                    exitProgress();
                } catch (IOException e) {
                    exitProgress();
                    e.printStackTrace();
                    sendThreadFlag = 0;
                }
            }
            sendThreadFlag = 0;
        }

        private void showProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = ProgressDialog.show(MainActivity.this, "", "잠시만 기다려주세요.", true);
                }
            });

        }

        private void exitProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
        }
    }
}
