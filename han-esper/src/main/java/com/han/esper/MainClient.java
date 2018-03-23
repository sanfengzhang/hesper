package com.han.esper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.espertech.esperio.http.core.URIUtil;

public class MainClient
{

	private final int port;
	private HttpClient httpclient;

	public MainClient(int port)
	{
		this.port = port;
		httpclient = new DefaultHttpClient();

	}

	public void request(String document, String... parameters) throws UnsupportedEncodingException
	{
		String uri = "http://127.0.0.1:" + port + "/" + document;
		// String uri = "https://www.baidu.com";
		URI requestURI = null;
		try
		{
			requestURI = URIUtil.withQuery(new URI(uri), parameters);
		} catch (URISyntaxException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpPost httpget = new HttpPost(requestURI);

		httpget.addHeader("Connection", "Close");
	/**
		httpget.addHeader("Connection", "Close");

		Map<String, String> map = new HashMap<String, String>();
		map.put("root", "stream");
		map.put("_def_addEvent", "eventName");

		map.put("eplContent", "select * from orderEvent");
		map.put("eventDefine", "{\"buyerName\":\"String\",\"price\": \"int\"}");

		String classContent = loadStmtListener();

		map.put("eventListener", "{\"type\":\"class\",\"className\":\"DefaultEsperUpdateListener\",\"classContent\":\""
				+ classContent + "\"}");

		HttpEntity httpEntity = new StringEntity(JSON.toJSONString(map));
		httpget.setEntity(httpEntity);**/
		
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = null;
		try
		{
			responseBody = httpclient.execute(httpget, responseHandler);

			System.out.println(responseBody);
		} catch (IOException e)
		{
			throw new RuntimeException("Error executing request:" + e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception
	{
		MainClient supportHTTPClient = new MainClient(8080);
		
	
//		supportHTTPClient.request("root", "stream", "_def_addEvent", "eventName", "orderEvent", "eplContent",
//				"select * from orderEvent", "eventListener","{\"type\":\"class\",\"className\":\"DefaultEsperUpdateListener\",\"classContent\":\""
//						+ URLEncoder.encode(loadStmtListener(), "utf-8") + "\"}" , "eventDefine",
//				"{\"buyerName\":\"String\",\"price\": \"int\"}");

		 supportHTTPClient.request("root", "stream", "orderEvent", "buyerName",
		 "zhangsan", "price", "10");

	}

	public static String loadStmtListener()
	{
		try
		{
			FileInputStream fileInputStream = new FileInputStream(
					new File("C:\\Users\\owner\\Desktop\\DefaultEsperUpdateListener.class"));
			try
			{
				String str = org.apache.commons.io.IOUtils.toString(fileInputStream, "UTF-8");

				return str;
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
}
