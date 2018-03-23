package com.han.esper;

public class Event
{

	private String eventName;

	private String sqlContent;

	private String type;

	public String getEventName()
	{
		return eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	public String getSqlContent()
	{
		return sqlContent;
	}

	public void setSqlContent(String sqlContent)
	{
		this.sqlContent = sqlContent;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return "Event [eventName=" + eventName + ", sqlContent=" + sqlContent + ", type=" + type + "]";
	}

}
