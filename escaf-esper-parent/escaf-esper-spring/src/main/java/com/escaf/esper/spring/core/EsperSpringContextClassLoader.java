package com.escaf.esper.spring.core;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EsperSpringContextClassLoader
{

	private static final String ESPER_SPRING_CLASSLOADER_HOME = "esper-app/";

	private static final String ESPER_ROOT_BIN_DIR = "bin/";

	private static final String BIZ_EVENT_TYPE_JAR = "eventtype";

	private List<String> eventTypeJars = new ArrayList<String>();

	private static final EsperSpringContextClassLoader INSTANCE = new EsperSpringContextClassLoader();

	private static final Log log = LogFactory.getLog(EsperSpringContextClassLoader.class);

	private EsperSpringContextClassLoader()
	{

	}

	public static EsperSpringContextClassLoader getInstance()
	{
		return INSTANCE;
	}

	public URLClassLoader getEsperSpringContextClassLoader(String confAppPath, ClassLoader parentClassLoader)
	{

		URL rootURL = EsperSpringContextClassLoader.class.getResource("/");
		String esperAppPath = rootURL.getPath() + ESPER_SPRING_CLASSLOADER_HOME;

		List<URL> jarURL = new ArrayList<URL>();
		if (null == confAppPath || "".equals(confAppPath))
		{
			if (esperAppPath.contains(ESPER_ROOT_BIN_DIR))
			{
				esperAppPath = esperAppPath.replace(ESPER_ROOT_BIN_DIR, "");
				addJarURL(esperAppPath, jarURL, parentClassLoader);

			}

		} else
		{

			addJarURL(confAppPath, jarURL, parentClassLoader);
		}

		URL urls[] = new URL[jarURL.size()];
		URLClassLoader urlClassLoader = new URLClassLoader(jarURL.toArray(urls), parentClassLoader);
		log.info("Spring AppplicationContext Load App Jars" + ArrayUtils.toString(jarURL.toArray()));

		return urlClassLoader;

	}

	private void addJarURL(String esperAppPath, List<URL> jarURL, ClassLoader parentClassLoader)
	{
		File file = new File(esperAppPath);
		File fs[] = file.listFiles();

		for (File f : fs)
		{
			String path = "jar:file:/" + f.getPath() + "!/";
			URL url = null;
			try
			{
				url = new URL(path);
			} catch (MalformedURLException e)
			{

				e.printStackTrace();
			}
			if (path.contains(BIZ_EVENT_TYPE_JAR))
			{
				URLClassLoader urlClassLoader = (URLClassLoader) parentClassLoader;
				try
				{
					Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class<?>[] { URL.class });
					method.setAccessible(true);
					method.invoke(urlClassLoader, new Object[] { url });

				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e)
				{

					e.printStackTrace();
				}

				eventTypeJars.add(path);
			}

			jarURL.add(url);

		}

	}

	public List<String> getEventTypeJars()
	{
		return eventTypeJars;
	}
	
	

}
