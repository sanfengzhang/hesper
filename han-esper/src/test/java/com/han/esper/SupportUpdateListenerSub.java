package com.han.esper;

import com.alibaba.fastjson.JSON;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.util.JSONEventRenderer;
import com.sun.xml.internal.ws.spi.db.OldBridge;

public class SupportUpdateListenerSub extends SupportUpdateListener
{
	private JSONEventRenderer jsonEventRenderer;

	public SupportUpdateListenerSub()
	{

	}

	public SupportUpdateListenerSub(JSONEventRenderer jsonEventRenderer)
	{
		this.jsonEventRenderer = jsonEventRenderer;
	}

	@Override
	public synchronized void update(EventBean[] newEvents, EventBean[] oldData)
	{
		super.update(newEvents, oldData);

		System.out.println("There is " + newEvents.length + " events to be return!");
		
		for (int i = 0; i < newEvents.length; i++)
		{
			 System.out.println(JSON.toJSONString(newEvents[i].getUnderlying()));  
		}

	}

}
