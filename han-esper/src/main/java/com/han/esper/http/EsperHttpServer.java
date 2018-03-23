package com.han.esper.http;

import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esperio.http.EsperIOHTTPAdapterPlugin;
import com.han.esper.AbstractEscEsperServer;

public class EsperHttpServer extends AbstractEscEsperServer
{

	@Override
	public EPServiceProvider initEsper()
	{
		String esperIOHTTPConfig = "<esperio-http-configuration>\n" + "<service name=\"service1\" port=\"" + 8087
				+ "\" nio=\"" + true + "\"/>" + "<get service=\"service1\" pattern=\"*\"/>"
				+ "</esperio-http-configuration>";

		Configuration engineConfig = new Configuration();
		engineConfig.addPluginLoader("EsperIOHTTPAdapter", EsperIOHTTPAdapterPlugin.class.getName(), new Properties(),
				esperIOHTTPConfig);

		engineConfig.addEventType("OrderEvent", OrderEvent.class);

		EPServiceProvider epServiceProvider = EPServiceProviderManager.getProvider("test", engineConfig);

		EPStatement stmt = epServiceProvider.getEPAdministrator()
				.createEPL("select * from OrderEvent where buyerName='zhangsan'");
		stmt.addListener(new UpdateListener()
		{

			public void update(EventBean[] newEvents, EventBean[] oldEvents)
			{
				System.out.println(JSON.toJSONString(newEvents));

			}
		});

		return epServiceProvider;

	}
}
