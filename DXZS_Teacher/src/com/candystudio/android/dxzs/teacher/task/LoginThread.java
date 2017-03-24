package com.candystudio.android.dxzs.teacher.task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.candystudio.android.dxzs.teacher.app.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

public class LoginThread extends Thread {

	public Socket clientSocket;
	public DataInputStream inputStream;
	public DataOutputStream outputStream;
	public int inflag;
	private String ipString;
	private int port;
	private String userNameString, passwordString;


	public LoginThread(String UserName, String Password) {
		Context context = MyApplication.getInstance();

		SharedPreferences sp = context.getSharedPreferences("server",
				Context.MODE_PRIVATE);

		ipString = sp.getString("ip", "none");
		port = Integer.valueOf(sp.getString("port", "none"));

		userNameString = UserName;
		passwordString = Password;
	}

	public void run() {
		try {
			clientSocket = new Socket(ipString, port);
			inputStream = new DataInputStream(clientSocket.getInputStream());
			outputStream = new DataOutputStream(clientSocket.getOutputStream());
			// chengguo;123456
			outputStream.writeUTF("gin-" + userNameString+";"+passwordString);
					
			while(true)
			{
				String messageString = inputStream.readUTF();
				if (messageString == null)
					continue;
				else if(messageString.equals("1"))
				{
					// 通知主线程，登录成功。
					inflag=1;
					break;
				}
				else if(messageString.equals("2"))
				{
					// 通知主线程，登录失败。
					inflag=2;
					break;
				}
			}
			clientSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			inflag=3;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			inflag=4;
			e.printStackTrace();
			
		}

			


	}

}
