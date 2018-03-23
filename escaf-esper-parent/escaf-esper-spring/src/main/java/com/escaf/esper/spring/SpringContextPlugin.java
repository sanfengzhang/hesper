package com.escaf.esper.spring;

import java.io.IOException;

import com.espertech.esper.plugin.PluginLoader;
import com.espertech.esper.plugin.PluginLoaderInitContext;

public class SpringContextPlugin implements PluginLoader
{

	private PluginLoaderInitContext context;

	private SpringContextPluginAdapter springContextPluginAdapter;

	public static final String PLUGIN_NAME = SpringContextPlugin.class.getName();

	public void init(PluginLoaderInitContext context)
	{
		this.context = context;

	}

	public void postInitialize()
	{

		try
		{
			springContextPluginAdapter = new SpringContextPluginAdapter(context.getEpServiceProvider());
			springContextPluginAdapter.start();

		} catch (IOException e)
		{

			e.printStackTrace();
		}
	}

	public void destroy()
	{

		if (null != springContextPluginAdapter)
		{
			springContextPluginAdapter.destory();
		}

	}

	public SpringContextPluginAdapter getSpringContextPluginAdapter()
	{
		return springContextPluginAdapter;
	}

}
