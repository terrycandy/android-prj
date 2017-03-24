package com.candystudio.android.dxzs.teacher.position;

import android.location.Location;

public class Converter {
	// WGS84����ϵ�µľ��ȡ�γ�ȡ��߶�
	private double lon84;
	private double lat84;
	private double ele84;
	// BJ54����ϵ�µľ��ȡ�γ�ȡ��߶�
	private double lon54;
	private double lat54;
	private double ele54;
	// BJ54����ϵ�µľ��ȡ�γ�ȡ��߶�
	private double lon2000;
	private double lat2000;
	private double ele2000;
	// WGS84����ϵ�µ�ֱ������
	private double x84;
	private double y84;
	private double z84;
	// BJ54����ϵ�µ�ֱ������
	private double x54;
	private double y54;
	private double z54;
	// BJ54����ϵ�µ�ƽ������
	private int px54;
	private int py54;

	private double lon0;// ���뾭��
	// WGS84����ϵ����
	private double n84;// ���߳���
	private double ee84;// ��һƫ����ƽ��
	private double a84;// ���򳤰뾶
	private double f84;// ����
	private double gm84;
	private double omega84;
	
	// BJ54����ϵ����
	private double n54;// ���߳���
	private double ee54;// ��һƫ����ƽ��
	private double a54;// ���򳤰뾶
	private double b54;
	private double f54;// ����
	
	//CGCS2000����ϵ����
	private double n2000;// ���߳���
	private double ee2000;// ��һƫ����ƽ��
	private double a2000;// ���򳤰뾶
	private double b2000;
	private double f2000;// ����
	private double gm2000;
	private double omega2000;
	
	public double getLon54() {
		double temp = lon54 * 180.0 / Math.PI;
		if (temp < 0) {
			temp = 180 + temp;
		}
		return temp;
	}

	public double getLat54() {
		double temp = lat54 * 180.0 / Math.PI;
		if (temp < 0) {
			temp = 180 + temp;
		}
		return temp;
	}

	public double getEle54() {
		return ele54;
	}

	public int getPx2000() {
		return py54;
	}

	public int getPy2000() {
		return px54;
	}

	public Converter() {

		a84 = 6378137;
		ee84 = 0.0066943799013;
		f84 = 1 / 298.257223629;
		gm84=3.986004418*1014;
		
		a54 = 6378245;
		b54 = 6356863.0188;
		ee54 = 0.006693421622965949;
		f54 = 1 / 298.3;
		
		
		a2000=6378137;
		f2000=1/298.257222101;
		gm2000=3.986005*1014;

	}
	
	public void Convert_84GEOto2000PLAT(Double Longitude, Double Latitude, Double Altitude) {
		lon84 = Longitude;
		lat84 = Latitude;
		ele84 = Altitude;
				
		lon2000=lon84;
		lat2000=lat84;
		ele2000=ele84;
		
		convert_2000GEOto2000PLAT();
	}

	public void Covert_84GEOto54PLAT(Location LC) {
		lon84 = LC.getLongitude() * Math.PI / 180;
		lat84 = LC.getLatitude() * Math.PI / 180;
		ele84 = LC.getAltitude();
		convert_84GEOto84XYZ();
		convert_84XYZto54XYZ(0, 0, 0, 0, 0, 0, 0);
		convert_54XYZto54GEO();
		convert_54GEOto54PLAT();
	}

	// 84��������ת����84�ռ�ֱ������
	private void convert_84GEOto84XYZ() {
		n84 = a84 / Math.sqrt(1 - (ee84 * Math.sin(lat84) * Math.sin(lat84)));
		x84 = (n84 + ele84) * Math.cos(lat84) * Math.cos(lon84);
		y84 = (n84 + ele84) * Math.cos(lat84) * Math.sin(lon84);
		z84 = (n84 * (1 - ee84) + ele84) * Math.sin(lat84);
	}

	// �߲���,84�ռ�ֱ������ת����54�ռ�ֱ������
	private void convert_84XYZto54XYZ(double DX, double DY, double DZ, double Arf,
			double Beta, double Gama, double k) {

		x54 = DX + (1 + k) * x84 + Gama * y84 - Beta * z84;
		y54 = DY + (1 + k) * y84 - Gama * x84 + Arf * z84;
		z54 = DZ + (1 + k) * z84 + Beta * x84 - Arf * y84;
	}

	// 54�ռ�ֱ������ת����54��������
	private void convert_54XYZto54GEO() {
		// �㾭��
		lon54 = Math.atan(y54 / x54) * 180 / Math.PI;
		if (lon54 < 0)
			lon54 = lon54 + 180;

		double N0 = a54;
		double H0 = Math.sqrt(x54 * x54 + y54 * y54 + z54 * z54)
				- Math.sqrt(a54 * b54);
		double B0 = Math.atan((z54 / Math.sqrt(x54 * x54 + y54 * y54))
				* (N0 + H0) / (N0 + H0 - ee54 * N0));
		double N1 = a54 / Math.sqrt(1 - ee54 * Math.sin(B0) * Math.sin(B0));
		double H1 = Math.sqrt(x54 * x54 + y54 * y54) / Math.cos(B0) - N1;
		double B1 = Math.atan((z54 / Math.sqrt(x54 * x54 + y54 * y54))
				* (N1 + H1) / (N1 + H1 - ee54 * N1));
		while ((B1 - B0) > 0.000001 || (H1 - H0) > 0.000001) {
			B0 = B1;
			H0 = H1;
			N1 = a54 / Math.sqrt(1 - ee54 * Math.sin(B0) * Math.sin(B0));
			H1 = Math.sqrt(x54 * x54 + y54 * y54) / Math.cos(B0) - N1;
			B1 = Math.atan((z54 / Math.sqrt(x54 * x54 + y54 * y54)) * (N1 + H1)
					/ (N1 + H1 - ee54 * N1));
		}
		lat54 = B1 * 180 / Math.PI;
		ele54 = H1;
	}

	// 54��������ת����54ƽ������
	private void convert_54GEOto54PLAT() {
		// ��˹ͶӰ
		Gauss g = new Gauss(a54, f54);
		g.Cal_xy(lat54, lon54);
		px54 = g.getPlatX();
		py54 = g.getPlatY();

	}

	// 2000��������ת����2000ƽ������
	private void convert_2000GEOto2000PLAT() {
		// ��˹ͶӰ
		Gauss g = new Gauss(a2000, f2000);
		g.Cal_xy(lat2000, lon2000);
		px54 = g.getPlatX();
		py54 = g.getPlatY();

	}

}
