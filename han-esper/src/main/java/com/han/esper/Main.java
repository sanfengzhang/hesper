package com.han.esper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.han.esper.http.OrderEvent;

public class Main
{

	public static void main(String[] args)
	{
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();
		
		admin.getConfiguration().addEventType("order1", OrderEvent.class);
		

		admin.getConfiguration().addEventType("order0", OrderEvent.class);
		
		System.out.println(String.class);
		

		String epl1 = "select * from  order0 where infoMap('addr')='test'";
		
		String epl = "select * from pattern[every order1(buyerName='zhangsan') -> timer:interval(5 seconds)]";

		EPStatement state1 = admin.createEPL(epl);
		state1.addListener(new UpdateListener()
		{

			public void update(EventBean[] newEvents, EventBean[] oldEvents)
			{
				if (newEvents != null)
				{
					EventBean event = newEvents[0];
					// cast(avg(price), int)�м�Ŀո���EPL�п��Բ�д������event.get��ʱ�������ϣ�������asһ������������ת�����ֵ
					System.out.println(JSON.toJSONString(newEvents));
					
					
				}

			}
		});

		EPRuntime runtime = epService.getEPRuntime();

		OrderEvent b1 = new OrderEvent();
		b1.setPrice(10);
		b1.setBuyerName("zhangsan");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("addr", "test");
		b1.setInfoMap(map);
		runtime.sendEvent(b1);
		
		try
		{
			System.in.read();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
