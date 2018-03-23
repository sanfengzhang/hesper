package com.escaf.esper.spring;

import java.io.IOException;

import org.springframework.context.support.AbstractApplicationContext;

import com.escaf.esper.spring.core.EsperSpringApplicationContextUtils;
import com.escaf.esper.spring.core.SpringContextPluginService;
import com.espertech.esper.client.EPServiceProvider;

public class SpringContextPluginAdapter
{
	private AbstractApplicationContext applicationContext;

	private final EPServiceProvider epServiceProvider;

	public SpringContextPluginAdapter(EPServiceProvider epServiceProvider)
	{
		if (null == epServiceProvider)
		{
			throw new NullPointerException("epServiceProvider must not be null.");
		}
		this.epServiceProvider = epServiceProvider;

	}

	public void start() throws IOException
	{

		applicationContext = EsperSpringApplicationContextUtils.getDefaultApplicationContext();

		SpringContextPluginService springContextPluginService = new SpringContextPluginService(epServiceProvider,
				applicationContext);
		springContextPluginService.start();

	}

	public void destory()
	{
		if (null != applicationContext)
		{
			applicationContext.close();
		}
		applicationContext = null;
	}

	public void setApplicationContext(AbstractApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

}
