package com.pjcstudio.pjcstudio.checksms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import android.util.Log;

public class IpCheckerThread extends Thread {
	private String domain;
	public Boolean iskr;
	public Boolean state;
	
	public IpCheckerThread(String domainarg)
	{
		domain = domainarg;
		state = false;
	}
	
	public void run()
	{
		String resp = GetPosition();
		
		Log.i("respdata", resp.substring(0,100));
		if(resp.indexOf("KR") == -1)
		{
			iskr = false;
			Log.i("WHERE", "ELSE KR");
		}
		else
		{
			iskr = true;
			Log.i("WHERE", "KR");
		}
		state = true;
	}
	
	public Boolean Getstate()
	{
		return state;
	}
	
	public String GetPosition()
	{
		StringBuilder output = new StringBuilder();
		InetAddress inetaddr[] = null;
		try {
			inetaddr = InetAddress.getAllByName(domain); // IP ���ؿ���
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String ipaddr = inetaddr[0].getHostAddress();
		
		try {
			URL url = new URL("http://whois.kisa.or.kr/openapi/whois.jsp?query=" + ipaddr + "&key=2014110918344555186521");
			Log.i("URL", "http://whois.kisa.or.kr/openapi/whois.jsp?query=" + ipaddr + "&key=2014110918344555186521");
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if(conn != null) {
				conn.setConnectTimeout(10000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				
				int rescode = conn.getResponseCode(); // ��û
				if(rescode == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line = null;
					while(true) {
						line = reader.readLine();
						if(line == null) {
							break;
						}
						output.append(line + "\n");
					}
					reader.close();
					conn.disconnect();
					}
				}
		} catch (IOException e) {
			Log.i("HTTP", "request domain fail");
			e.printStackTrace();
		}
		return output.toString();
	}
}
