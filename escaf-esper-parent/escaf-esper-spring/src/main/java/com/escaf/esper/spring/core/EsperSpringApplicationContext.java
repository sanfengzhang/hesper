package com.escaf.esper.spring.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EsperSpringApplicationContext extends AnnotationConfigApplicationContext
{

	public EsperSpringApplicationContext(ClassLoader classLoader, Class<?>... annotatedClasses)
	{
		super();
		register(annotatedClasses);
		this.setClassLoader(classLoader);

		refresh();

	}

	public EsperSpringApplicationContext(ClassLoader classLoader, String... basePackages)
	{
		super();
		scan(basePackages);
		this.setClassLoader(classLoader);
		refresh();
	}

}
