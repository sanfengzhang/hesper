package com.escaf.esper.spring.core;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import com.escaf.esper.spring.annotation.EsperEventEntity;
import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EventType;

public class EsperSpringAnnotationScanSupport
{
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

	private String[] baseScanPackages;

	private static final Log logger = LogFactory.getLog(EsperSpringAnnotationScanSupport.class);

	private EPServiceProvider epServiceProvider;

	public EsperSpringAnnotationScanSupport()
	{

	}

	public EsperSpringAnnotationScanSupport(String[] baseScanPackages, EPServiceProvider epServiceProvider)
	{
		this.baseScanPackages = baseScanPackages;
		this.epServiceProvider = epServiceProvider;
	}

	public void bindSpringEventTypeNameAuto()
	{
		for (String basePackage : baseScanPackages)
		{
			doScan(basePackage);

		}

	}

	private void doScan(String basePackage)
	{

		try
		{
			logger.info("satrt scan esper event entity" + basePackage);
			Resource[] resources = resourcePatternResolver.getResources(basePackage+DEFAULT_RESOURCE_PATTERN);

			boolean traceEnabled = logger.isTraceEnabled();
			for (Resource resource : resources)
			{

				if (traceEnabled)
				{
					logger.trace("Scanning " + resource);
				}
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
				boolean has = annotationMetadata.hasAnnotation(EsperEventEntity.class.getName());

				if (has)
				{
					Map<String, Object> annotationMaps = annotationMetadata
							.getAnnotationAttributes(EsperEventEntity.class.getName());

					String eventTypeName = annotationMaps.get("name").toString();
					ConfigurationOperations configurationOperations = epServiceProvider.getEPAdministrator()
							.getConfiguration();
					EventType eventType = configurationOperations.getEventType(eventTypeName);
					if (null == eventType)
					{
						Class<?> clazz = Class.forName(metadataReader.getClassMetadata().getClassName(), false,
								Thread.currentThread().getContextClassLoader());
						configurationOperations.addEventType(eventTypeName, clazz);

					}

				}

			}

		} catch (IOException | LinkageError | ClassNotFoundException e)
		{
			e.printStackTrace();

		}

	}

}
