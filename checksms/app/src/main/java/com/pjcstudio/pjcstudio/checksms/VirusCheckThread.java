package com.pjcstudio.pjcstudio.checksms;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class VirusCheckThread extends Thread {
	String resStr;
	String apikeyStr;
	Boolean isvirus;
	Boolean state;
	
	public VirusCheckThread(String inStr, String inStr2) {
		resStr = inStr;
		apikeyStr = inStr2;
		isvirus = false;
		state = false;
	}
	
	public Boolean Getisvirse()
	{
		return isvirus;
	}
	
	public Boolean Getstate()
	{
		return state;
	}
	
	public void run() {
		try {
			final String output = request(resStr, apikeyStr);
			int idx = output.indexOf("true");
			Log.i("HTTP", String.valueOf(idx));
			
			state = true;
			
			if(idx != -1) // ���̷��� �Ǻ�
			{
				isvirus = true;
				state = true;
			}
			else
			{
				Log.i("HTTP", "not virus");
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String request(String res, String apikey) {
		StringBuilder output = new StringBuilder();
		try {
			URL url = new URL("https://www.virustotal.com/vtapi/v2/file/report");
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); // ���ͳ� ��ü ����
			if(conn != null)
			{
				conn.setConnectTimeout(1000000); // Ÿ�Ӿƿ���
				conn.setRequestMethod("POST"); // POST �޼ҵ�� ����
				conn.setDoInput(true);
				conn.setDoOutput(true);
				
				String param = "resource=" + res + "&" + "apikey=" + apikey; // POST ���ڱ��� 
				
				OutputStream out_stream = conn.getOutputStream();
				out_stream.write( param.getBytes("UTF-8") );
				out_stream.flush();
				out_stream.close();
				
				InputStream is = null;
			    BufferedReader in = null;
			    String data = "";

			    is = conn.getInputStream();
			    in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

			    String line = null;
			    StringBuffer buff   = new StringBuffer();

			    while ( ( line = in.readLine() ) != null )
			    {
			        buff.append(line + "\n");
			    }
			    data = buff.toString().trim();
			    return data;
			}
		} catch(Exception ex) {
			Log.i("HTTP", "ERROR");
		}
		
		return output.toString();
	}
}