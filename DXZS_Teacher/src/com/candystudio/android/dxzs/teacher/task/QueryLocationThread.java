package com.candystudio.android.dxzs.teacher.task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

import com.candystudio.android.dxzs.teacher.app.MyApplication;
import com.candystudio.android.dxzs.teacher.model.LocationClass;

public class QueryLocationThread extends Thread {

	public Socket clientSocket;
	public DataInputStream inputStream;
	public DataOutputStream outputStream;
	public ArrayList<LocationClass> locations;
	private String ipString;
	private int port;
	private String sender, receiver;
	public int res;

	public QueryLocationThread(String Sender, String Receiver) {
		Context context = MyApplication.getInstance();
		SharedPreferences sp = context.getSharedPreferences("server",
				Context.MODE_PRIVATE);
		ipString = sp.getString("ip", "none");
		port = Integer.valueOf(sp.getString("port", "none"));
		sender = Sender;
		receiver = Receiver;
		locations = new ArrayList<LocationClass>();

	}

	public void run() {
		try {
			clientSocket = new Socket(ipString, port);
			inputStream = new DataInputStream(clientSocket.getInputStream());
			outputStream = new DataOutputStream(clientSocket.getOutputStream());
			// chengguo;123456
			outputStream.writeUTF("qry-" + receiver + ";" + sender);
			String messageString;
			res = inputStream.readInt();
			for (int i = 0; i < res; i++) {
				messageString = inputStream.readUTF();
				LocationClass loc = new LocationClass(messageString);
				locations.add(loc);
			}
			clientSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			res = -1;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			res = -2;
			e.printStackTrace();

		}
	}
}