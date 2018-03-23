package com.han.esper;

import java.io.IOException;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.han.esper.http.OrderEvent;

public class SocketMain {

	public static void main(String[] args) throws IOException {

		Properties p = new Properties();
		p.setProperty("esperio.http.configuration.file", "esperio-http-sample-config.xml");

		Configuration configuration = new Configuration();
		
		//configuration.getEngineDefaults().getLogging().setEnableExecutionDebug(true);;

		configuration.addPluginLoader("httpPlugin", "com.espertech.esperio.http.EsperIOHTTPAdapterPlugin", p);
		configuration.addEventType(OrderEvent.class);

		EPServiceProvider esEpServiceProvider = EPServiceProviderManager.getDefaultProvider(configuration);

		EPAdministrator epAdministrator = esEpServiceProvider.getEPAdministrator();
		String eventSql = "select * from "+OrderEvent.class.getName();
		EPStatement statement = epAdministrator.createEPL(eventSql);

		statement.addListener(new UpdateListener() {

			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				System.out.println(JSON.toJSONString(newEvents));

			}
		});
		
		OrderEvent orderEvent=new OrderEvent();
		orderEvent.setBuyerName("zhangsan");
		orderEvent.setPrice(10);
		esEpServiceProvider.getEPRuntime().sendEvent(orderEvent);
		
		
		System.in.read();

	}

}
