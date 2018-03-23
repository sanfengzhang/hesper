package com.han.esper;

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
import com.han.esper.http.OrderEvent;
import com.han.esper.httpserver.SupportHTTPServer;

public class SupportHttpServerMain {

	public static void main(String[] args) throws Exception {

		ConfigurationHTTPAdapter adapterConfig = new ConfigurationHTTPAdapter();

		Request requestOne = new Request();
		requestOne.setStream("OrderEvent");
		requestOne.setUri("http://127.0.0.1:8078/root");
		adapterConfig.getRequests().add(requestOne);

		EsperIOHTTPAdapter adapter = new EsperIOHTTPAdapter(adapterConfig, "TestHTTPAdapterOutput");

		Configuration engineConfig = new Configuration();
		engineConfig.addEventType("OrderEvent", OrderEvent.class);

		EPServiceProvider provider = EPServiceProviderManager.getProvider("TestHTTPAdapterOutput", engineConfig);

		// provider.getEPRuntime().sendEvent(new OrderEvent("zhangsan", 10));

		adapter.start();

		EPStatement stmt = provider.getEPAdministrator().createEPL("select buyerName  from OrderEvent");
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
