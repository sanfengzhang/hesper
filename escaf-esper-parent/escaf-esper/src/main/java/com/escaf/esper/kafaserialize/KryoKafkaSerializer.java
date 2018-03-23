package com.escaf.esper.kafaserialize;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoKafkaSerializer<T> implements Serializer<T>
{

	public void configure(Map<String, ?> configs, boolean isKey)
	{

	}

	public byte[] serialize(String topic, T data)
	{
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		Output output = new Output(buffer);
		kryo.writeClassAndObject(output, data);

		output.flush();
		output.close();
		return buffer.toByteArray();
	}

	public void close()
	{

	}

}
