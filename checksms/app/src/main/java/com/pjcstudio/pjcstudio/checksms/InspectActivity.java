package com.pjcstudio.pjcstudio.checksms;

import java.io.File;
import java.util.Random;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InspectActivity extends Activity {
	private DownloadManager downloadManager;
	private DownloadManager.Request request;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.i("Beforecall", "SetContextView");
	    setContentView(R.layout.activity_inspect);
	    
	    Intent processintent = getIntent();
	    String link = processintent.getStringExtra("includelink");
	    Log.i("link", link);
	    
	    if(true)
        {
        	 processintent.putExtra("downloadstate", "false");
        	 downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        	 request = new DownloadManager.Request(Uri.parse(link));
        	 request.setTitle("���� ÷������");
             request.setDescription("�˻縦 ���� �ڵ� �ٿ�ε�");
             
             Random random = new Random(); // ���� ��ü
             final int randomint = random.nextInt(90000); // ������ ����
             
             request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, String.valueOf(randomint));
             Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS).mkdirs();
             downloadManager.enqueue(request);

             
             String downloadstate = null;

             
             Handler handler = new Handler();
             handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					File filedownload = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS );
					String defaultdownload = filedownload.getPath(); // �ٿ�ε� �⺻��� ���ؿ�
					Log.i("defaultdownload", defaultdownload);
			        filedownload = new File(filedownload, String.valueOf(randomint));
			        Log.i("beforehash", filedownload.getPath());
			        
			        TextView stateview = (TextView)findViewById(R.id.stateview); // ��� �ؽ�Ʈ�� ��ü�������� 
			        TextView virusview = (TextView)findViewById(R.id.virusview);
			        TextView ipview = (TextView)findViewById(R.id.ipview);
			        TextView hazard = (TextView)findViewById(R.id.hazard);
		        	stateview.setText("해쉬 검사중 ....");
			        
			        try {
						String hash = Sha256Crypt.extractFileHashSHA256(filedownload.getPath());
						
						Toast.makeText(getApplicationContext(), hash, Toast.LENGTH_LONG);
						VirusCheckThread thread = new VirusCheckThread(hash, "8294d7e4b02fabaaccbf26e44ce5cf9dc05725139c8f0e7cf2a5c2d3764024b9");
						thread.start();
						
						stateview.setText("바이러스 검사중 ...");
						
						while(true)
						{
							if(thread.Getstate() == true) {
								break;
							}
						}
						
						int vulnscore = 0;
						if(thread.Getisvirse() == true)
						{
							TelephonyManager telephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
							String mu_phone_deviceid = telephony.getDeviceId();    //device id

							Util.getNetworkService().regHash(hash, mu_phone_deviceid, new Callback<Response>() {
										@Override
										public void success(Response response, Response response2) {
											Log.d("init", "complete");
										}

										@Override
										public void failure(RetrofitError error) {
											error.printStackTrace();
										}
									});

									virusview.setText("Virus check : 바이러스로 감지");

							vulnscore += 80;
						}
						else
						{
							virusview.setText("Virus check : 바이러스가 아님");
						}
						
						stateview.setText("호스팅서버 조회중 ...");
						
						Intent processintent = getIntent();
					    String link = processintent.getStringExtra("includelink");
					    Log.i("LINK HANDLER", link);
					    link = link.substring(7, link.length());
					    if(link.indexOf("/") != -1) {
					    	link = link.substring(0, link.indexOf("/"));
					    }
					    Log.i("LINK HANDLER2", link);
						IpCheckerThread thread2 = new IpCheckerThread(link);
						thread2.start();
						
						while(true)
						{
							if(thread2.Getstate() == true) {
								break;
							}
						}
						
						if(thread2.iskr == true)
						{
							ipview.setText("Hosting Server : 한국서버");
						}
						else
						{
							ipview.setText("Hosting Server : 해외서버");
							vulnscore += 20;
						}
						
						ZipUtils.unzip(filedownload.getPath(), defaultdownload + "/", false);
						hazard.setText("hazard Score : " + String.valueOf(vulnscore));
						stateview.setText("검사완료!");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 10000);
        }    
	    Log.i("EXITEXIT", "END ......");
	}
}