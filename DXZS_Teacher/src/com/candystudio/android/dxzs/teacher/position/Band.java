package com.candystudio.android.dxzs.teacher.position;

public class Band {
	public Band(){
	}
	
	public int GetSixBand(double L){//���ȴ�����
		return (int)L/6 + 1 ;
	}
	
	public int GetThreeBand(double L){//���ȴ�����
		return (int)((L+1.5)/3);
	}
    
	public int Get3L0(int n){//���ȴ����뾭��
		return n*3;
	}
	public int Get6L0(int n){//���ȴ����뾭��
		return n*6-3;
		
	}
	
	public int Get3L0(double L){//���ȴ����뾭��
		return (GetThreeBand(L))*3;
	}
	public int Get6L0(double L){//���ȴ����뾭��
		return GetSixBand(L)*6-3;
		
	}
}