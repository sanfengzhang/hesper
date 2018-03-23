package com.han.esper;

import java.io.IOException;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

public class MainTest
{
	
	private static final  EPServiceProvider ePServiceProvider= EPServiceProviderManager.getDefaultProvider(); 

	public static void main(String[] args) throws IOException
	{
		ePServiceProvider.getEPAdministrator().getConfiguration().addEventType("SB", SupportBean.class);
		ePServiceProvider.getEPAdministrator().getConfiguration().addEventType("MD", SupportMarketDataBean.class);
		sendTimer(0, ePServiceProvider);

		String stmtText = "select * from pattern [ every(SB -> (MD where timer:within(5 sec))) ]";
		EPStatement statement = ePServiceProvider.getEPAdministrator().createEPL(stmtText);
		SupportUpdateListener listener = new SupportUpdateListener();
		statement.addListener(listener);

		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E1", 1));
		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E2", 2));
		sendTimer(12000, ePServiceProvider);

		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E4", 1));
		
		
		sendTimer(12000, ePServiceProvider);
		ePServiceProvider.getEPRuntime().sendEvent(new SupportMarketDataBean("E5", "M1", 1d));
		System.out.println(listener.isInvoked());
		
		System.in.read();

	}
	
	private  static void sendTimer(long timeInMSec, EPServiceProvider epService)
	{
		CurrentTimeEvent theEvent = new CurrentTimeEvent(timeInMSec);
		EPRuntime runtime = epService.getEPRuntime();
		runtime.sendEvent(theEvent);
	}

}
