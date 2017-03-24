package com.candystudio.android.dxzs.teacher.model;

public class LocationClass {

	public String Sender;
	public String Receiver;
	public int PointID;
	public double Longitude;
	public double Latitude;
	public String MessageText;
	public String ReportTimeString;
	public int ReportType;// 0-->help 1-->wrong 2-->right

	public String ReportString;

	public LocationClass(String CommString) {
		ReportString = CommString;
		parse();
	}

	private void parse() {
		// loc-chengguo;tangtang;1;113.12345;28.56789;KDDXABC
		String[] params = ReportString.split(";");
		Receiver = params[0];
		Sender = params[1];
		PointID = Integer.valueOf(params[2]);
		Longitude = Double.valueOf(params[3]);
		Latitude = Double.valueOf(params[4]);
		MessageText = params[5];
		if (params.length == 7)
			ReportTimeString = params[6];
		if (MessageText .equals("sos")) {
			ReportType = 0;
		} else if (MessageText.equals("mis")) {
			ReportType = 1;
		} else {
			ReportType = 2;
		}
	}

	public String generatePstmtString(String TableName) {
		String sqlString = "insert into ";
		sqlString += TableName;
		sqlString += " values(?,?,?,?,?,?,?)";

		return sqlString;
	}

}
