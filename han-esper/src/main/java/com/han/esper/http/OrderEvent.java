package com.han.esper.http;

import java.io.Serializable;
import java.util.Map;

public class OrderEvent implements Serializable{

	private String buyerName;

	private int price;

	private String mark;
	
	private Map<String,Object> infoMap;

	public OrderEvent() {

	}

	public OrderEvent(String buyerName, int price) {
		super();
		this.buyerName = buyerName;
		this.price = price;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Map<String, Object> getInfoMap()
	{
		return infoMap;
	}

	public void setInfoMap(Map<String, Object> infoMap)
	{
		this.infoMap = infoMap;
	}
	
	

}
