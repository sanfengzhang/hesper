package com.han.esper.example;

public class SelectEvent
{
	private int price;
	private String name;

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "name=" + name + ", price=" + price;
	}

}
