package com.han.esper;

import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public abstract class AbstractEscEsperServer
{
	protected final EPServiceProvider epServiceProvider;

	private static final String ADD_EVENT_CONFIG = "_def_addEvent";

	protected AbstractEscEsperServer()
	{

		epServiceProvider = initEsper();
		initAddEventOperation();

	}

	public EPServiceProvider getEpServiceProvider()
	{
		return epServiceProvider;
	}

	public abstract EPServiceProvider initEsper();

	public void addEvent(String eventTypeName, Class<?> clazz)
	{
		if (null != epServiceProvider)
		{
			epServiceProvider.getEPAdministrator().getConfiguration().addEventType(eventTypeName, clazz);

		}

	}

	public void initAddEventOperation()
	{
		final ConfigurationOperations configurationOperations = this.epServiceProvider.getEPAdministrator()
				.getConfiguration();
		configurationOperations.addEventType(ADD_EVENT_CONFIG, Event.class);
		final EPAdministrator epAdministrator = this.epServiceProvider.getEPAdministrator();
		final EPStatement stmt = epAdministrator.createEPL("select * from " + ADD_EVENT_CONFIG);
		stmt.addListener(new UpdateListener()
		{

			public void update(EventBean[] newEvents, EventBean[] oldEvents)
			{
				handleAddEventOperation(newEvents, configurationOperations, epAdministrator);

			}
		});

	}

	private void handleAddEventOperation(EventBean[] newEvents, ConfigurationOperations configurationOperations,
			EPAdministrator epAdministrator)
	{
		if (null != newEvents)
		{
			for (EventBean eventBean : newEvents)
			{

				String name = (String) eventBean.get("eventName");
				if (null != name)
				{

					String newSql = (String) eventBean.get("sqlContent");
					configurationOperations.addEventType(name, Event.class);
					EPStatement stmt = epAdministrator.createEPL(newSql);
					stmt.addListener(new UpdateListener()
					{

						public void update(EventBean[] newEvents, EventBean[] oldEvents)
						{
							for (EventBean eventBean : newEvents)
							{

							}

						}
					});
				}

			}

		}
	}

}
