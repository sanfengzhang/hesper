package com.han.esper.alert;

public abstract class Alert
{
	private String alertName;

	public abstract void hanle();

	public String getAlertName()
	{
		return alertName;
	}

	public void setAlertName(String alertName)
	{
		this.alertName = alertName;
	}

}
