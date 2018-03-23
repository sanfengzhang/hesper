package com.escaf.esper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class ClassLoaderTest
{

	public static void main(String[] args) throws Exception
	{
		
		Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] {
				String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
		defineClass.setAccessible(true);
		byte[] data=loadStmtListener();
		Class<?> clazz = (Class<?>) defineClass.invoke(Thread.currentThread().getContextClassLoader(), new Object[] { "com.escaf.event.storage.Event", data, 0,
				data.length, ClassLoaderTest.class.getProtectionDomain()});
		
		System.out.println(clazz.newInstance());
		
	}
	
	public static byte[] loadStmtListener()
	{
		try
		{
			FileInputStream fileInputStream = new FileInputStream(
					new File("C:\\Users\\owner\\Desktop\\Event.class"));
			try
			{
				byte[] str = org.apache.commons.io.IOUtils.toByteArray(fileInputStream);

				return str;
			} catch (IOException e)
			{

				e.printStackTrace();
			}
		} catch (FileNotFoundException e)
		{

			e.printStackTrace();
		}

		return null;

	}


}
