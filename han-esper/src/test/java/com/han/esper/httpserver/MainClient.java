package com.han.esper.httpserver;

import java.io.IOException;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esperio.http.EsperIOHTTPAdapterPlugin;

public class MainClient {

	public static void main(String[] args) throws IOException {
		String esperIOHTTPConfig = "<esperio-http-configuration>\n" + "<service name=\"service1\" port=\"" + 8087
				+ "\" nio=\"" + true + "\"/>" + "<get service=\"service1\" pattern=\"*\"/>"
				+ "</esperio-http-configuration>";

		Configuration engineConfig = new Configuration();
		engineConfig.addPluginLoader("EsperIOHTTPAdapter", EsperIOHTTPAdapterPlugin.class.getName(), new Properties(),
				esperIOHTTPConfig);

		engineConfig.addEventType("SupportBean", SupportBean.class);

		EPServiceProvider provider = EPServiceProviderManager.getProvider("test", engineConfig);

		EPStatement stmt = provider.getEPAdministrator().createEPL("select * from SupportBean");
		stmt.addListener(new UpdateListener() {

			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				System.out.println(JSON.toJSONString(newEvents));

			}
		});

		System.in.read();

	}

}
