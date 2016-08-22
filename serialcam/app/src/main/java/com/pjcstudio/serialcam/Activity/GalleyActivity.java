package com.pjcstudio.serialcam.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pjcstudio.serialcam.Adapter.GalleyAdapter;
import com.pjcstudio.serialcam.ApplicationClass;
import com.pjcstudio.serialcam.R;
import com.pjcstudio.serialcam.Views.ScrollableGridView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class GalleyActivity extends AppCompatActivity implements View.OnClickListener {

    public static GalleyActivity app;

    private GridView galleyView;
    private GalleyAdapter adapter;
    private int currentPosition;
    private ArrayList<String> checked_Array;
    private File file[];
    private int mode;

    // 액티비티 실행모드 정의
    static final int MODE_SEND = 0;
    static final int MODE_MOVE = 1;
    static final int MODE_REVIEW = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galley);

        app = this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {

        mode = Integer.parseInt(getIntent().getStringExtra("mode"));

        galleyView = (GridView) findViewById(R.id.gridgalley);
        galleyView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        adapter = new GalleyAdapter(this, galleyView.getMeasuredWidth(), galleyView.getMeasuredHeight());
        checked_Array = new ArrayList<String>();

        //File rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //Environment.DIRECTORY_PICTURE

        File rootPath = null;
        if(mode == MODE_REVIEW) {
            rootPath = new File("/mnt/extSdCard/");
        }
        else if(mode == MODE_MOVE) {
            rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        else if(mode == MODE_SEND) {
            rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }

        if(rootPath.canRead() && rootPath != null) {
            File files[] = rootPath.listFiles();
            file = files;

            //numberOfFile = file.length;

            Arrays.sort(file, new Comparator() {
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

            int len;
            currentPosition = 0;
            if (file.length < 6) {
                len = file.length;
            } else {
                len = 6;
            }

            for (int i = 0; i < len; i++) {
                Log.d("Name", file[i].toString());
                adapter.addItem(file[i].toString());
            }
            galleyView.setAdapter(adapter);

            if (mode == MODE_REVIEW) {
                galleyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectItem = (String) adapterView.getAdapter().getItem(i);
                        if (!selectItem.equals("NONE")) {
                            Intent intent = new Intent(GalleyActivity.this, ShowPhotoActivity.class);
                            intent.putExtra("path", selectItem);
                            startActivity(intent);
                        }
                    }
                });
            } else {

                galleyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                        String selectItem = (String) adapterView.getAdapter().getItem(pos);
                        int sameFlag = 0;
                        for (int i = 0; i < checked_Array.size(); i++) {
                            if (selectItem.equals(checked_Array.get(i))) {
                                checked_Array.remove(i);
                                sameFlag = 1;
                                break;
                            }
                        }

                        if (sameFlag == 0) {
                            checked_Array.add(selectItem);
                            ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
                            imageView.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                        } else if (sameFlag == 1) {
                            ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
                            imageView.clearColorFilter();
                        }

                        for (int i = 0; i < checked_Array.size(); i++) {
                            Log.d("image", checked_Array.get(i));
                        }

                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.appWorkState = 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app = null;
        MainActivity.cameraFragment.isTakePhoto = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.appWorkState = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_left:
                setImages(item.getItemId());
                break;
            case R.id.action_right:
                setImages(item.getItemId());
                break;
            case R.id.action_send:
                switch (mode)
                {
                    case MODE_SEND:
                        sendImage();
                        break;
                    case MODE_MOVE:
                        copyImage();
                        break;
                }
                break;
        }
        return true;
    }

    public void moveLeft() {
        setImages(R.id.action_left);
    }

    public void moveRight() {
        setImages(R.id.action_right);
    }



    private void setImages(int btn) {

        for(int i=0;i<galleyView.getChildCount();i++) {
            View view = galleyView.getChildAt(i);
            ImageView child = (ImageView) view.findViewById(R.id.item_image);
            child.clearColorFilter();
        }

        checked_Array.clear();

        if(btn == R.id.action_left)
        {
            if(currentPosition >= 6)
            {
                adapter.m_List.clear();
                currentPosition -= 6;
                for(int i=currentPosition;i<currentPosition+6;i++) {
                    adapter.addItem(file[i].toString());
                }
                adapter.notifyDataSetChanged();
            }
        }
        else if(btn == R.id.action_right)
        {
            if(currentPosition+6 < file.length) {
                adapter.m_List.clear();

                currentPosition += 6;
                for(int i=currentPosition;i<(currentPosition+6);i++) {
                    if(i >= file.length) {
                        adapter.addItem("NONE");
                        Log.d("state", "NONE");
                    } else {
                        Log.d("str", file[i].toString());
                        adapter.addItem(file[i].toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void copyImage() {
        String pathSdcard = ((ApplicationClass) getApplicationContext()).findSdcardPath();

        ProgressDialog progressDialog = ProgressDialog.show(this, "파일을 복사중입니다.", null);
        if(pathSdcard != null) {
            for (int i = 0; i < checked_Array.size(); i++) {
                File src = new File(checked_Array.get(i));
                copyFile(src, pathSdcard+src.getName());
            }
            checked_Array.clear();
            Toast.makeText(this, "복사되었습니다.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "SD카드가 없습니다.", Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

    private boolean copyFile(File file , String save_file) {
        boolean result;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fis.read(buffer,0,1024))!= -1){
                    newfos.write(buffer,0,readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    private void sendImage() {
        if(checked_Array.size() != 0) {
            new SendImage(checked_Array).start();
            for(int i=0;i<adapter.getCount();i++) {
                View view = (View) galleyView.getChildAt(i);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
                imageView.clearColorFilter();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

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
                }
            }
        }

        private void showProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = ProgressDialog.show(GalleyActivity.this, "", "잠시만 기다려주세요.", true);
                }
            });

        }

        private void exitProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checked_Array.clear();
                    progressDialog.dismiss();
                }
            });
        }
    }
}
