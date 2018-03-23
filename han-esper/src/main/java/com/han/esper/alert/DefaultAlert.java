package com.han.esper.alert;

public class DefaultAlert extends Alert
{

	@Override
	public void hanle()
	{
		System.out.println(this.getAlertName());

	}

}
