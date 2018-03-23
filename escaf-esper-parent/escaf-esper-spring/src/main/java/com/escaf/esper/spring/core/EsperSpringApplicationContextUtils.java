package com.escaf.esper.spring.core;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class EsperSpringApplicationContextUtils
{

	public static EsperSpringApplicationContext getDefaultApplicationContext()
	{
		final ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();

		ClassPathResource classPathResource = new ClassPathResource("esper.spring.properties");
		Properties properties = null;
		try
		{
			properties = PropertiesLoaderUtils.loadProperties(classPathResource);
		} catch (IOException e)
		{

			e.printStackTrace();
		}
		String basePackage = properties.getProperty("esper.spring.scan.packages");
		final String[] basePackages = StringUtils.split(basePackage, ",");

		final URLClassLoader urlClassLoader = EsperSpringContextClassLoader.getInstance()
				.getEsperSpringContextClassLoader(properties.getProperty("esper.sprin.app.dir"), parentClassLoader);

		EsperSpringApplicationContext applicationContextref = new EsperSpringApplicationContext(urlClassLoader,
				SpringConfiguration.class);

		return applicationContextref;
	}

}
