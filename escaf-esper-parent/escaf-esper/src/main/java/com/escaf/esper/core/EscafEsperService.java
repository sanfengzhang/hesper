package com.escaf.esper.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.escaf.esper.util.Constants;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.util.FileUtil;
import com.espertech.esperio.kafka.KafkaOutputDefault;

public class EscafEsperService
{
	private static final Log log = LogFactory.getLog(EscafEsperService.class);

	protected EPServiceProvider epServiceProvider;

	private Properties conf = null;

	private EscafEsperEventSupportService escafEsperEventSupportService = null;

	public EscafEsperService()
	{
		URL configURL = FileUtil.class.getClassLoader().getResource(Constants.BOOTSTRAP_CONFIG);

		if (null == configURL)
		{
			throw new NullPointerException(
					"please check you have configuration file with name=" + Constants.BOOTSTRAP_CONFIG);
		}

		conf = loadProperties(configURL);
		escafEsperEventSupportService = new EscafEsperEventSupportService();
	}

	public EPServiceProvider startEPServiceEngine()
	{

		Configuration engineConfig = new Configuration();
		engineConfig.addAnnotationImport(KafkaOutputDefault.class);

		String esperPlugins = conf.getProperty(Constants.ESCAF_ESPER_PLUGINS);
		String esperPluginArr[] = StringUtils.split(esperPlugins, ",");
		log.info("start pluginLoader with names=[" + ArrayUtils.toString(esperPluginArr) + "]");

		for (String esperPluginClassName : esperPluginArr)
		{
			URL url = FileUtil.class.getClassLoader().getResource(esperPluginClassName);
			engineConfig.addPluginLoader(esperPluginClassName, esperPluginClassName, loadProperties(url));

		}

		EPServiceProvider epServiceProvider = EPServiceProviderManager.getProvider(Constants.ESPER_PROVIDER_URI,
				engineConfig);

		// 初始化内置的一些Event事件
		escafEsperEventSupportService.initAddEventOperation(epServiceProvider);	

		return epServiceProvider;

	}

	public EPServiceProvider getEpServiceProvider()
	{
		return epServiceProvider;
	}

	public static Properties loadProperties(URL url)
	{
		InputStream is = null;

		Properties properties = new Properties();
		try
		{
			if (null == url)
			{
				return properties;
			}
			is = url.openStream();
			properties.load(is);
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(is);
		}

		return properties;
	}

}
