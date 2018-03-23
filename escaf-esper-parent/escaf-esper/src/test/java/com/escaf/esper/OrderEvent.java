package com.escaf.esper;

import java.io.Serializable;

public class OrderEvent implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String buyerName;

	private int price;

	public String getBuyerName()
	{
		return buyerName;
	}

	public void setBuyerName(String buyerName)
	{
		this.buyerName = buyerName;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	@Override
	public String toString()
	{
		return "OrderEvent [buyerName=" + buyerName + ", price=" + price + "]";
	}

}
