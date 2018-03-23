package com.han.esper;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.janino.ByteArrayClassLoader;

import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.util.ObjectInputStreamWithTCCL;

public class ClassLoaderTest
{

	public static void main(String[] args) throws Exception
	{
		

		FileInputStream fis = new FileInputStream(
				new File("C:\\\\Users\\\\owner\\\\Desktop\\\\DefaultEsperUpdateListener.class"));
		
		Map<String,byte[]> map=new HashMap<String,byte[]>();
		
		map.put("DefaultEsperUpdateListener", IOUtils.toByteArray(fis));
		ByteArrayClassLoader byteArrayClassLoader=new ByteArrayClassLoader(map);
		
		Class<?> clazz=byteArrayClassLoader.loadClass("DefaultEsperUpdateListener");
		

//		ObjectInputStreamWithTCCL objectInputStreamWithTCCL = new ObjectInputStreamWithTCCL(IOUtils.buffer(fis));
//		ObjectStreamClass streamClass = ObjectStreamClass.lookup(UpdateListener.class);
//		Class<?> clazz = objectInputStreamWithTCCL.resolveClass(streamClass);
		
		System.out.println(clazz.newInstance());
	}

}
