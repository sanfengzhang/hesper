package com.han.esper.example;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class SelectNamedWindowListener implements UpdateListener
{

	public void update(EventBean[] newEvents, EventBean[] oldEvents)
	{
		if (newEvents != null)
		{
			System.out.println("There is " + newEvents.length + " events to be return!");
			for (int i = 0; i < newEvents.length; i++)
			{
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}