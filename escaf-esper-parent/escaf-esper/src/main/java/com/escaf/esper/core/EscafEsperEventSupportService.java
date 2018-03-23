package com.escaf.esper.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.Base64;

import com.escaf.esper.util.ClazzUtil;
import com.escaf.esper.util.Constants;
import com.escaf.esper.util.StringUtils;
import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class EscafEsperEventSupportService
{

	private static final Log log = LogFactory.getLog(EscafEsperEventSupportService.class);

	public void initAddEventOperation(EPServiceProvider epServiceProvider)
	{
		final ConfigurationOperations configurationOperations = epServiceProvider.getEPAdministrator()
				.getConfiguration();

		Map<String, Object> eventMap = new HashMap<String, Object>();

		eventMap.put(Constants.ADD_EVENT_NAME, String.class);
		eventMap.put(Constants.EPL_CONTENT, String.class);
		eventMap.put(Constants.ADD_EVENT_DEFINE, String.class);
		eventMap.put(Constants.ADD_EVENT_STMT_LISTENER, String.class);
		eventMap.put(Constants.ADD_EVENT_TYPE, String.class);

		configurationOperations.addEventType(Constants.ADD_EVENT_OPERATION, eventMap);

		final EPAdministrator epAdministrator = epServiceProvider.getEPAdministrator();

		final EPStatement stmt = epAdministrator.createEPL("select * from " + Constants.ADD_EVENT_OPERATION);
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

				String name = (String) eventBean.get(Constants.ADD_EVENT_NAME);
				if (null != name)
				{

					String newSql = (String) eventBean.get(Constants.EPL_CONTENT);
					String eventDefine = (String) eventBean.get(Constants.ADD_EVENT_DEFINE);
					String eventType = (String) eventBean.get(Constants.ADD_EVENT_TYPE);
					if (StringUtils.isEmpty(newSql) || StringUtils.isEmpty(eventDefine)
							|| StringUtils.isEmpty(eventType))
					{
						throw new NullPointerException("sqlContent and eventDefine must not be null,then sqlContent=["
								+ newSql + "],eventDefine=[" + eventDefine + "],eventType=[" + eventType + "].");

					}

					@SuppressWarnings("unchecked")
					Map<String, String> eventDefMap = JSON.parseObject(eventDefine, Map.class);

					if (eventType.equals("map"))//支持Map类型的事件实体类型
					{
						Iterator<Entry<String, String>> it = eventDefMap.entrySet().iterator();
						Map<String, Object> eventMap = new HashMap<String, Object>();
						while (it.hasNext())
						{
							Entry<String, String> en = it.next();

							eventMap.put(en.getKey(), ClazzUtil.getClazzType(en.getValue()));

						}

						log.info("add event success,then eventMap=" + eventMap.toString());
						configurationOperations.addEventType(name, eventMap);

					} else//以java实体对象为事件类型，支持动态加载。目前不支持多个自定义的类的组合。
					{
						String className = eventDefMap.get(Constants.ADD_EVENT_CLASS_NAME);
						String classContent = eventDefMap.get(Constants.ADD_EVENT_CLASS_CONTENT);

						Class<?> clazz = getClazz(className, classContent);
						configurationOperations.addEventType(name, clazz);

					}

					EPStatement stmt = epAdministrator.createEPL(newSql);
					String stmtListener = (String) eventBean.get(Constants.ADD_EVENT_STMT_LISTENER);					
					UpdateListener updateListener = loadListenerObject(stmtListener);
					if (null != updateListener)
					{
						stmt.addListener(updateListener);
					}

				}

			}

		}
	}

	private UpdateListener loadListenerObject(String stmtListener)
	{
		@SuppressWarnings("unchecked")
		Map<String, String> stmts = JSON.parseObject(stmtListener, Map.class);

		if (Constants.ADD_EVENT_STMT_LISTENER_CLASS.equals(stmts.get("type")))
		{

			String classContent = stmts.get("classContent");
			String className = stmts.get("className");

			Class<?> clazz = getClazz(className, classContent);
			try
			{
				return (UpdateListener) clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e)
			{

				e.printStackTrace();
			}

		}
		return null;

	}

	private Class<?> getClazz(String className, String classContent)
	{
		ClassLoader currentTccl = null;
		currentTccl = Thread.currentThread().getContextClassLoader();
		Class<?> clazz = null;
		if (currentTccl != null)
		{

			log.info("start init UpdateListener Object,name=" + className);
			Method defineClass = null;
			try
			{
				defineClass = ClassLoader.class.getDeclaredMethod("defineClass",
						new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
				defineClass.setAccessible(true);

				byte[] data = Base64.decodeFast(classContent);
				clazz = (Class<?>) defineClass.invoke(currentTccl,
						new Object[] { className, data, 0, data.length, this.getClass().getProtectionDomain() });
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e)
			{

				e.printStackTrace();
			}

		}

		return clazz;
	}

}
