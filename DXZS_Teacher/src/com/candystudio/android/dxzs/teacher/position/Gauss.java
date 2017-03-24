package com.candystudio.android.dxzs.teacher.position;


public class Gauss {
	double a; // 长半轴
	double b; // 短半轴
	double f; // 扁率
	double e1; // 第一偏心率
	double e2; // 第二偏心率
	double e2e2; // 第二偏心率平方
	private int platx;
	private int platy;

	public int getPlatX() {
		return platx;
	}

	public int getPlatY() {
		return platy;
	}

	public Gauss(double a1, double f1) {
		a = a1;
		f = f1;
		b = cal_b(a, f);
		e1 = cal_e1(a, b);
		e2 = cal_e2(a, b);
		e2e2 = cal_e2e2(a, b);

	}

	private double cal_e1(double a, double b) {// 第一偏心率
		return (Math.sqrt(a * a - b * b)) / a;
	}

	private double cal_e2(double a, double b) {// 第二偏心率
		return (Math.sqrt(a * a - b * b)) / b;
	}

	private double cal_b(double a, double f) {// 短半轴

		return (1 - f) * a;
	}

	private double cal_e2e2(double a, double b) {
		return (a * a - b * b) / (b * b);
	}

	private double Cal_Y0(double B1) {// 根据维度计算Y0
		double X0 = 0;
		double B = B1 * Math.PI / 180;
		double a1, a2, a3, a4, a5;
		double n;
		n = (a - b) / (a + b);
		a1 = (a + b) / 2.0 * (1 + Math.pow(n, 2) / 4.0 + Math.pow(n, 4) / 64.0);
		a2 = -3.0 / 2 * n + 9 / 16.0 * Math.pow(n, 3) - 3 / 32.0
				* Math.pow(n, 5);
		a3 = 15 / 16.0 * Math.pow(n, 2) - 15 / 32.0 * Math.pow(n, 4);
		a4 = -35 / 48.0 * Math.pow(n, 3) + 105 / 256.0 * Math.pow(n, 5);
		a5 = 315 / 512.0 * Math.pow(n, 4);
		X0 = a1
				* (B + a2 * Math.sin(2 * B) + a3 * Math.sin(4 * B) + a4
						* Math.sin(6 * B) + a5 * Math.sin(8 * B));
		return X0;
	}

	public void Cal_xy(double B1, double L1) {// 高斯正算，未加50000

		double Y0;
		double B;
		double L;
		double L0; //中央经线经度
		double l;
		double t;
		double n2;
		double N;
		B = B1 * Math.PI / 180;
		L = L1 * Math.PI / 180;
		Band b = new Band();
		int bandnum = b.GetSixBand(L1);
		L0=b.Get6L0(bandnum);
		Y0 = Cal_Y0(B1);// 有问题,should be Y

		L0 = L0 * Math.PI / 180;// 转为弧度
		l = L - L0;
		t = Math.tan(B);
		n2 = e2e2 * Math.cos(B) * Math.cos(B);
		N = N(B1);
		double cosB = Math.cos(B);
		double cos2B = Math.pow(Math.cos(B), 2);
		double cos3B = Math.pow(Math.cos(B), 3);
		double cos4B = Math.pow(Math.cos(B), 4);
		double cos5B = Math.pow(Math.cos(B), 5);
		double cos6B = Math.pow(Math.cos(B), 6);
		double cos7B = Math.pow(Math.cos(B), 7);
		double cos8B = Math.pow(Math.cos(B), 8);
		double l2 = Math.pow(l, 2);
		double l3 = Math.pow(l, 3);
		double l4 = Math.pow(l, 4);
		double l5 = Math.pow(l, 5);
		double l6 = Math.pow(l, 6);
		double l7 = Math.pow(l, 7);
		double l8 = Math.pow(l, 8);
		double t2 = Math.pow(t, 2);
		double t4 = Math.pow(t, 4);
		double t6 = Math.pow(t, 6);
		double n4 = Math.pow(n2, 2);


		double x, y;

		y = Y0 + t / 2.0 * N * cos2B * l2 + t / 24.0 * N * cos4B
				* (5 - t2 + 9 * n2 + 4 * n4) * l4 + t / 720.0 * N * cos6B
				* (61 - 58 * t2 + t4 + 270 * n2 - 330 * t2 * n2) * l6 + t
				/ 40320.0 * N * cos8B * (1385 - 3111 * t2 + 543 * t4 - t6) * l8;
		x = bandnum * 1000000 + 500000 + N * cosB * l + N / 6.0 * cos3B
				* (1 - t2 + n2) * l3 + N / 120.0 * cos5B
				* (5 - 18 * t2 + t4 + 14 * n2 - 58 * t2 * n2) * l5 + N / 5040.0
				* cos7B * (61 - 479 * t2 + 179 * t4 - t6) * l7;
		// Log.i("x", String.valueOf(x));
		// Log.i("y", String.valueOf(y));
		platx = (int) x;
		platy = (int) y;
	}

	private double N(double B1) {// 椭圆体卯酉圈曲率半径
		double B = B1 * Math.PI / 180;
		return a / (Math.sqrt(1 - e1 * Math.sin(B) * e1 * Math.sin(B)));
	}

	private double Bf(double x) {// 底点纬度
		double Bf = 0;

		double a1, a2, a3, a4, a5;
		double n = (a - b) / (a + b);
		double n2 = Math.pow(n, 2);
		double n3 = Math.pow(n, 3);
		double n4 = Math.pow(n, 4);
		double n5 = Math.pow(n, 5);
		a1 = (a + b) / 2.0 * (1 + n2 / 4.0 + n4 / 64.0);
		a2 = 3.0 / 2 * n - 27.0 / 32 * n3 + 269.0 / 512 * n5;
		a3 = 21.0 / 16 * n2 - 55.0 / 32 * n4;
		a4 = 151.0 / 96 * n3 - 417.0 / 128 * n5;
		a5 = 1097.0 / 512 * n4;
		double x1 = x / a1;
		Bf = x1 + a2 * Math.sin(2 * x1) + a3 * Math.sin(4 * x1) + a4
				* Math.sin(6 * x1) + a5 * Math.sin(8 * x1);
		/*
		 * double e2 = Math.pow(e, 2); double e4 = Math.pow(e, 4); double e6 =
		 * Math.pow(e, 6); double e8 = Math.pow(e, 8); double A0 =
		 * 1+3.0/4*e2+45.0/64*e4+350.0/512*e6+11025.0/16384*e8
		 */
		return Bf;
	}

	public double Cal_B(double x, double y) {// 高斯反算，求维度B。
		double B = 0;
		double Bf = Bf(x);
		double y2 = Math.pow(y, 2);
		double y4 = Math.pow(y, 4);
		double y6 = Math.pow(y, 6);
		double y8 = Math.pow(y, 8);
		double tf = Math.tan(Bf);
		double tf2 = Math.pow(tf, 2);
		double tf4 = Math.pow(tf, 4);
		double tf6 = Math.pow(tf, 6);
		double n2 = e2e2 * Math.cos(Bf) * Math.cos(Bf);
		double n4 = Math.pow(n2, 2);
		double Nf = a / (Math.sqrt(1 - e1 * Math.sin(Bf) * e1 * Math.sin(Bf)));
		double Nf2 = Math.pow(Nf, 2);
		// double Nf3 = Math.pow(Nf, 3);
		double Nf4 = Math.pow(Nf, 4);
		// double Nf5 = Math.pow(Nf, 5);
		double Nf6 = Math.pow(Nf, 6);
		// double Nf7 = Math.pow(Nf, 7);
		double Nf8 = Math.pow(Nf, 8);
		B = Bf
				+ tf
				* (-1 - n2)
				* y2
				/ (2.0 * Nf2)
				+ tf
				* (5 + 3 * tf2 + 6 * n2 - 6 * tf2 * n2 - 3 * n4 - 9 * tf2 * n4)
				* y4
				/ (24.0 * Nf4)
				+ tf
				* (-61 - 90 * tf2 - 45 * tf4 - 107 * n2 + 162 * tf2 * n2 + 45
						* tf4 * n2) * y6 / (720.0 * Nf6) + tf
				* (1385 + 3663 * tf2 + 4095 * tf4 + 1575 * tf6) * y8
				/ (40320.0 * Nf8);
		B = B * 180 / Math.PI;
		if (B < 0) {
			B = B + 180.0;
		}
		return B;
	}

	public double Cal_L(double x, double y, double L0) {// 高斯反算，求经度L
		double L = 0;
		double Bf = Bf(x);
		double y3 = Math.pow(y, 3);
		double y5 = Math.pow(y, 5);
		double y7 = Math.pow(y, 7);
		double tf = Math.tan(Bf);
		double tf2 = Math.pow(tf, 2);
		double tf4 = Math.pow(tf, 4);
		double tf6 = Math.pow(tf, 6);
		double n2 = e2e2 * Math.cos(Bf) * Math.cos(Bf);
		// double n4 = Math.pow(n2, 2);
		double Nf = a / (Math.sqrt(1 - e1 * Math.sin(Bf) * e1 * Math.sin(Bf)));
		// double Nf2 = Math.pow(Nf, 2);
		double Nf3 = Math.pow(Nf, 3);
		// double Nf4 = Math.pow(Nf, 4);
		double Nf5 = Math.pow(Nf, 5);
		// double Nf6 = Math.pow(Nf, 6);
		double Nf7 = Math.pow(Nf, 7);
		// double Nf8 = Math.pow(Nf, 8);
		double cosBf = Math.cos(Bf);
		L0 = L0 * Math.PI / 180;// 转为弧度
		L = L0 + y / (Nf * cosBf) + y3 * (-1 - 2 * tf2 - n2)
				/ (6.0 * Nf3 * cosBf) + y5
				* (5 + 28 * tf2 + 24 * tf4 + 6 * n2 + 8 * tf2 * n2)
				/ (120.0 * Nf5 * cosBf) + y7
				* (-61 - 622 * tf2 - 1320 * tf4 - 720 * tf6)
				/ (5040.0 * Nf7 * cosBf);
		L = L * 180 / Math.PI;
		if (L < 0) {
			L = L + 180.0;
		}
		return L;
	}
}
