package com.candystudio.android.dxzs.teacher.examination;

public class Standard
{/*	+"id integer primary key,"
	+"latitude real,"
	+"longitude real,"
	+"msg text,"
	+"map integer ,"
	+"route integer ,"
	+"number integer )";*/
	
	private int  x;
	private int  y;
	private String msg;
	private int map;
	private int route;
	private int number;
	public Standard(int x,int y,String msg,int map,int route,int number)
	{
		this.x=x;
		this.y=y;
		this.msg=msg;
		this.map=map;
		this.route=route;
		this.number=number;
		
	}
	public Standard()
	{
		// TODO Auto-generated constructor stub
	}
	
	
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public int getMap()
	{
		return map;
	}
	public void setMap(int map)
	{
		this.map = map;
	}
	public int getRoute()
	{
		return route;
	}
	public void setRoute(int route)
	{
		this.route = route;
	}
	public int getNumber()
	{
		return number;
	}
	public void setNumber(int number)
	{
		this.number = number;
	}
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}

}
