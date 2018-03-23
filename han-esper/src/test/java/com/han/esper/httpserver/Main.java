package com.han.esper.httpserver;

import com.alibaba.fastjson.JSON;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esperio.http.EsperIOHTTPAdapter;
import com.espertech.esperio.http.config.ConfigurationHTTPAdapter;
import com.espertech.esperio.http.config.Request;

public class Main {

	public static void main(String[] args) throws Exception {
		ConfigurationHTTPAdapter adapterConfig = new ConfigurationHTTPAdapter();

		Request requestOne = new Request();
		requestOne.setStream("SupportBean");
		requestOne.setUri("http://127.0.0.1:8078/root");
		adapterConfig.getRequests().add(requestOne);

		
		EsperIOHTTPAdapter adapter = new EsperIOHTTPAdapter(adapterConfig, "test");

		Configuration engineConfig = new Configuration();
		engineConfig.addEventType("SupportBean", SupportBean.class);
	
		EPServiceProvider provider = EPServiceProviderManager.getProvider("test", engineConfig);

		adapter.start();

		EPStatement stmt = provider.getEPAdministrator()
				.createEPL("select * from SupportBean");
		stmt.addListener(new UpdateListener() {

			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				System.out.println(JSON.toJSONString(newEvents));

			}
		});

		SupportHTTPServer server8078 = new SupportHTTPServer(8078);
		server8078.start();

		System.in.read();

	}

}
