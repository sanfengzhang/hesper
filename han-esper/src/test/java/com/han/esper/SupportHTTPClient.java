package com.han.esper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esperio.http.core.URIUtil;

public class SupportHTTPClient {
	private static Logger log = LoggerFactory.getLogger(SupportHTTPClient.class);

	private final int port;
	private HttpClient httpclient;

	public SupportHTTPClient(int port) {
		this.port = port;
		httpclient = new DefaultHttpClient();

	}

	public void request(String document, String... parameters) {
		String uri = "http://127.0.0.1:" + port + "/" + document;
		// String uri = "https://www.baidu.com";
		URI requestURI = null;
		try {
			requestURI = URIUtil.withQuery(new URI(uri), parameters);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("Requesting from URI " + requestURI);
		HttpGet httpget = new HttpGet(requestURI);
		httpget.addHeader("Connection", "Close");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = null;
		try {
			responseBody = httpclient.execute(httpget, responseHandler);

			System.out.println(responseBody);
		} catch (IOException e) {
			throw new RuntimeException("Error executing request:" + e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		SupportHTTPClient supportHTTPClient = new SupportHTTPClient(8087);
		supportHTTPClient.request("root", "stream", "_def_addEvent", "eventName", "orderEvent","sqlContent","select * from orderEvent");

	}

}
