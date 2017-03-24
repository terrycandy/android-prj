package com.candystudio.android.dxzs.teacher.model;

import android.text.format.Time;

public class StudentInExam {

	// private String studentName;
	private String studentID;
	private double longitude;
	private double latitude;
	private int indexOfPoint;
	private Time reportTime;
	private String answer;
	private Boolean isAnswer;

	public StudentInExam(String Msg) {
		parseMsg(Msg);
	}

	public StudentInExam(String ID, double Lon, double Lat) {
		studentID = ID;
		longitude = Lon;
		latitude = Lat;
		isAnswer = false;
	}

	public StudentInExam(String ID, double Lon, double Lat, String Ans, int IoP) {
		// TODO Auto-generated constructor stub
		studentID = ID;
		longitude = Lon;
		latitude = Lat;
		answer = Ans;
		indexOfPoint = IoP;
		// reportTime=Time.;
		isAnswer = true;

	}

	public String ToMsg() {
		String msg = "";
		if (isAnswer) {
			msg += "answer:";
			msg += "id=";
			msg += studentID;
			msg += ";iop=";
			msg += indexOfPoint;
			msg += ";lon=";
			msg += longitude;
			msg += ";lat=";
			msg += latitude;
			msg += ";answer=";
			msg += answer;
			msg += ";time=";
			msg += reportTime.toString();
		} else {
			msg += "location:";
			msg += "id=";
			msg += studentID;
			msg += ";lon=";
			msg += longitude;
			msg += ";lat=";
			msg += latitude;
		}
		return msg;
	}

	private Boolean parseMsg(String Msg) {
		if (Msg.startsWith("location")) {
			isAnswer = false;
			int i = Msg.indexOf("id");
			int j = Msg.indexOf(";", i);
			if (i == -1 || j == -1)
				return false;
			studentID = Msg.substring(i+3, j);

			i = Msg.indexOf("lon");
			j = Msg.indexOf(";", i);
			if (i == -1 || j == -1)
				return false;
			longitude = Double.valueOf(Msg.substring(i+4, j));

			i = Msg.indexOf("lat");
			j = Msg.length();
			if (i == -1 || j == -1)
				return false;
			latitude = Double.valueOf(Msg.substring(i+4, j));

			return true;
		} else if (Msg.startsWith("answer")) {
			isAnswer = true;

			return true;
		} else {
			return false;
		}

	}

}
