package com.escaf.esper.spring.core;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.escaf.esper.spring.SpringContextPlugin;
import com.escaf.esper.spring.annotation.InjectEsper;
import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class SpringContextPluginService
{
	public static final String DESTORY_SPRINGCONTEXT_EVENT = "destorySpringContextPlugin";

	public static final String START_SPRINGCONTEXT_EVENT = "startSpringContextEvent";

	private EPServiceProvider epServiceProvider;

	private AbstractApplicationContext applicationContext;

	private static final Log log = LogFactory.getLog(SpringContextPluginService.class);

	public SpringContextPluginService(EPServiceProvider epServiceProvider,
			AbstractApplicationContext applicationContext)
	{
		this.epServiceProvider = epServiceProvider;
		this.applicationContext = applicationContext;
	}

	public void start()
	{
		String packageScan[] = new String[EsperSpringContextClassLoader.getInstance().getEventTypeJars().size()];
		EsperSpringContextClassLoader.getInstance().getEventTypeJars().toArray(packageScan);
		EsperSpringAnnotationScanSupport esperSpringAnnotationScanSupport = new EsperSpringAnnotationScanSupport(
				packageScan, epServiceProvider);
		esperSpringAnnotationScanSupport.bindSpringEventTypeNameAuto();
		initSpringContextPluginEvent();
		destorySpringContextPluginEvent();

	}

	public void initSpringContextPluginEvent()
	{
		initAddEvent();
		initAddEPL();

	}

	private void initAddEPL()
	{
		ClassPathResource classPathResource = new ClassPathResource("epl-init.properties");

		try
		{
			Iterator<String> it = IOUtils.readLines(classPathResource.getInputStream(), "utf-8").iterator();
			while (it.hasNext())
			{

				EPStatement epStatement = epServiceProvider.getEPAdministrator().createEPL(it.next());
				List<UpdateListener> list = getUpdateListenerFromApplicationContext(epStatement.getName());
				for (UpdateListener updateListener : list)
				{
					epStatement.addListener(updateListener);
				}

			}

		} catch (IOException e)
		{

			e.printStackTrace();
		}
	}

	private List<UpdateListener> getUpdateListenerFromApplicationContext(String statementName)
	{

		List<UpdateListener> result = new ArrayList<UpdateListener>();
		if (null != applicationContext)
		{

			Map<String, Object> beansMap = applicationContext.getBeansWithAnnotation(InjectEsper.class);
			Iterator<Entry<String, Object>> it = beansMap.entrySet().iterator();

			while (it.hasNext())
			{
				Entry<String, Object> en = it.next();

				SpringEsperUpadateListener object = (SpringEsperUpadateListener) en.getValue();

				InjectEsper injectEsper = AnnotationUtils.findAnnotation(object.getClass(), InjectEsper.class);

				String names[] = injectEsper.statmentName();

				String prefixName = injectEsper.prefixStatmentName();

				if (ArrayUtils.contains(names, statementName))
				{
					result.add(object);
				} else if (statementName.startsWith(prefixName))
				{
					result.add(object);
				}

			}

		}

		return result;

	}

	/** eg:orderEvent=id:int,name:string */
	private void initAddEvent()
	{
		ClassPathResource classPathResource = new ClassPathResource("add_event_init.properties");

		try
		{
			Properties properties = PropertiesLoaderUtils.loadProperties(classPathResource);
			Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();

			final EPAdministrator epAdministrator = this.epServiceProvider.getEPAdministrator();
			final ConfigurationOperations configurationOperations = epAdministrator.getConfiguration();
			while (it.hasNext())
			{
				Entry<Object, Object> en = it.next();
				String key = en.getKey().toString();
				String value = en.getValue().toString();

				String values[] = StringUtils.split(value, ",");

				Map<String, Object> eventTypeMap = new HashMap<String, Object>();
				for (String prop : values)
				{

					String[] props = StringUtils.split(prop, ":");
					eventTypeMap.put(props[0], props[1]);

				}

				configurationOperations.addEventType(key, eventTypeMap);

			}

		} catch (IOException e)
		{

			e.printStackTrace();
		}

	}

	public void destorySpringContextPluginEvent()
	{
		String destoryEPL = "@name('destorySpringContextPluginEPL') select * from " + DESTORY_SPRINGCONTEXT_EVENT;

		final EPAdministrator epAdministrator = this.epServiceProvider.getEPAdministrator();
		final ConfigurationOperations configurationOperations = epAdministrator.getConfiguration();

		Map<String, Object> typeMap = new HashMap<String, Object>();
		typeMap.put("destorySpringContextPlugin", String.class);
		// typeMap.put("enableDestoryStmts", boolean.class);

		configurationOperations.addEventType(DESTORY_SPRINGCONTEXT_EVENT, typeMap);
		final EPStatement epStatement = epServiceProvider.getEPAdministrator().createEPL(destoryEPL);
		epStatement.addListener(new DestorySpringContextEventUpadateListener());

		String startEPL = "@name('startApplicationContext') select * from " + START_SPRINGCONTEXT_EVENT;
		Map<String, Object> typeMapStart = new HashMap<String, Object>();
		typeMapStart.put(START_SPRINGCONTEXT_EVENT, String.class);
		configurationOperations.addEventType(START_SPRINGCONTEXT_EVENT, typeMapStart);
		final EPStatement epStatementStart = epServiceProvider.getEPAdministrator().createEPL(startEPL);
		epStatementStart.addListener(new StartSpringContextEventUpadateListener());

	}

	private class DestorySpringContextEventUpadateListener implements UpdateListener
	{
		public synchronized void update(EventBean[] newEvents, EventBean[] oldEvents)
		{
			if (null == applicationContext)
			{
				log.info("applicationContext have already closed.");
				return;
			}

			final EPAdministrator epAdministrator = epServiceProvider.getEPAdministrator();
			String stmtnames[] = epAdministrator.getStatementNames();
			for (String stmtname : stmtnames)
			{

				EPStatement statement = epAdministrator.getStatement(stmtname);
				Iterator<UpdateListener> iterator = statement.getUpdateListeners();
				while (iterator.hasNext())
				{
					UpdateListener updateListener = iterator.next();
					if (updateListener instanceof SpringEsperUpadateListener)
					{
						log.info("remove listener with stmt_name=[" + stmtname + "].");
						statement.removeListener(updateListener);
					}

				}

			}

			if (null != applicationContext)
			{
				URLClassLoader urlClassLoader = (URLClassLoader) applicationContext.getClassLoader();
				try
				{
					urlClassLoader.close();
				} catch (IOException e)
				{

					e.printStackTrace();
				}
				applicationContext.close();
				applicationContext = null;
			}

		}

	}

	private final class StartSpringContextEventUpadateListener implements UpdateListener
	{

		@Override
		public synchronized void update(EventBean[] newEvents, EventBean[] oldEvents)
		{
			if (null != applicationContext)
			{
				log.info("the applicationContext not null,please stop it first then you can start it.");
				return;
			}

			applicationContext = EsperSpringApplicationContextUtils.getDefaultApplicationContext();

			Context context = epServiceProvider.getContext();
			SpringContextPlugin springContextPlugin = null;
			try
			{
				springContextPlugin = (SpringContextPlugin) context
						.lookup("plugin-loader/" + SpringContextPlugin.class.getName());
			} catch (NamingException e)
			{

				e.printStackTrace();
			}
			if (null == springContextPlugin)
			{
				throw new NullPointerException("SpringContextPlugin Object must not be null,pluginObjectName="
						+ SpringContextPlugin.class.getName());
			}
			springContextPlugin.getSpringContextPluginAdapter().setApplicationContext(applicationContext);

			EPAdministrator epAdministrator = epServiceProvider.getEPAdministrator();

			Map<String, Object> beansMap = applicationContext.getBeansWithAnnotation(InjectEsper.class);
			Iterator<Entry<String, Object>> it = beansMap.entrySet().iterator();

			while (it.hasNext())
			{
				Entry<String, Object> en = it.next();

				SpringEsperUpadateListener object = (SpringEsperUpadateListener) en.getValue();

				InjectEsper injectEsper = AnnotationUtils.findAnnotation(object.getClass(), InjectEsper.class);

				String names[] = injectEsper.statmentName();

				String prefixName = injectEsper.prefixStatmentName();

				for (String name : names)
				{

					final EPStatement epStatement = epAdministrator.getStatement(name);
					log.info("add updateListener success,listener-class-name" + object.getClass().getName()
							+ ",statementName=" + name);
					epStatement.addListener(object);

				}

				if (null != prefixName && !"".equals(prefixName))
				{
					String allNames[] = epAdministrator.getStatementNames();
					for (String stmtName : allNames)
					{
						if (stmtName.startsWith(prefixName))
						{
							final EPStatement epStatement = epAdministrator.getStatement(stmtName);
							epStatement.addListener(object);
							log.info("add updateListener success,listener-class-name" + object.getClass().getName()
									+ ",statementName=" + stmtName);

						}
					}
				}

			}
		}

	}

}
