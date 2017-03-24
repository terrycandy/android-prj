package com.candystudio.android.dxzs.teacher.position;

public class Band {
	public Band(){
	}
	
	public int GetSixBand(double L){//六度带带号
		return (int)L/6 + 1 ;
	}
	
	public int GetThreeBand(double L){//三度带带号
		return (int)((L+1.5)/3);
	}
    
	public int Get3L0(int n){//三度带中央经线
		return n*3;
	}
	public int Get6L0(int n){//六度带中央经线
		return n*6-3;
		
	}
	
	public int Get3L0(double L){//三度带中央经线
		return (GetThreeBand(L))*3;
	}
	public int Get6L0(double L){//六度带中央经线
		return GetSixBand(L)*6-3;
		
	}
}