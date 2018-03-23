package com.han.esper;

import org.junit.Test;

public class EsperHttpServerTest {

	@Test
	public void testRequestHttpServer() {
		
		SupportHTTPClient supportHTTPClient = new SupportHTTPClient(8087);
		supportHTTPClient.request("root", "stream", "OrderEvent", "buyerName", "zhangsan","price","20");
	}

}
