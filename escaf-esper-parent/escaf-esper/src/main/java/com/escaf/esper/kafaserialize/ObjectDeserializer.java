package com.escaf.esper.kafaserialize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

@SuppressWarnings("rawtypes")
public class ObjectDeserializer implements Deserializer
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
	public Object deserialize(String arg0, byte[] data)
	{
		if (data == null)
		{
			return null;
		} else
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = null;
			try
			{
				ois = new ObjectInputStream(bais);
				return ois.readObject();

			} catch (IOException e)
			{

				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{

				e.printStackTrace();
			} finally
			{
				if (null != ois)
				{
					try
					{
						ois.close();
					} catch (IOException e)
					{

						e.printStackTrace();
					}
				}
				if (bais != null)
				{
					try
					{
						bais.close();
					} catch (IOException e)
					{

						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}

}
