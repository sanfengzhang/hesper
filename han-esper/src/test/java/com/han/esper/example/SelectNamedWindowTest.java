package com.han.esper.example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

public class SelectNamedWindowTest
{

	public static void main(String[] args)
	{
		 EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();  
	        EPAdministrator admin = epService.getEPAdministrator();  
	        EPRuntime runtime = epService.getEPRuntime();  
	  
	        String selectEvent = SelectEvent.class.getName();  
	  
	        String epl1 = "create window SelectNamedWindow.win:length_batch(3) as " + selectEvent;  
	        admin.createEPL(epl1);  
	        System.out.println("Create named window: create window SelectNamedWindow.win:length_batch(3) as "+selectEvent);  
	        String epl2 = "insert into SelectNamedWindow select * from " + selectEvent;  
	        admin.createEPL(epl2);  
	        
	        

	        // Can't create "select * from SelectamedWindow.win:time(3 sec)"  
	        String epl3 = "select * from SelectNamedWindow(price>=2)";  
	        EPStatement state3 = admin.createEPL(epl3);  
	        state3.addListener(new SelectNamedWindowListener());  
	        System.out.println("Register select sentence: select * from SelectNamedWindow(price>=2)");  
	  
	        SelectEvent se1 = new SelectEvent();  
	        se1.setName("se1");  
	        se1.setPrice(1);  
	        System.out.println("Send SelecEvent1 " + se1);  
	        runtime.sendEvent(se1);  
	  
	        SelectEvent se2 = new SelectEvent();  
	        se2.setName("se2");  
	        se2.setPrice(2);  
	        System.out.println("Send SelecEvent2 " + se2);  
	        runtime.sendEvent(se2);  
	  
	  
	        SelectEvent se3 = new SelectEvent();  
	        se3.setName("se3");  
	        se3.setPrice(3);  
	        System.out.println("Send SelecEvent3 " + se3 + "\n");  
	        runtime.sendEvent(se3);  

	}

}
