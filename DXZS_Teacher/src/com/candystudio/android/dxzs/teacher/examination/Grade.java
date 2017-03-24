package com.candystudio.android.dxzs.teacher.examination;

public class Grade
{
	private String username;
	private int grade;
	public Grade()
	{
		
	}
	public Grade(String username,int grade)
	{
		this.username=username;
		this.grade=grade;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public int getGrade()
	{
		return grade;
	}
	public void setGrade(int grade)
	{
		this.grade = grade;
	}


}
