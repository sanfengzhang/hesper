package com.escaf.esper.kafaserialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

@SuppressWarnings("rawtypes")
public class ObjectSerializer implements Serializer
{

	@Override
	public void close()
	{

	}

	@Override
	public void configure(Map arg0, boolean arg1)
	{

	}

	@Override
	public byte[] serialize(String arg0, Object object)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try
		{
			oos = new ObjectOutputStream(os);
			oos.writeObject(object);

			return os.toByteArray();

		} catch (IOException e)
		{

			e.printStackTrace();
		}

		return null;
	}

}
