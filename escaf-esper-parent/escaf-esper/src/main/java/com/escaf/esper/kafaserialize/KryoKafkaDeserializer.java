package com.escaf.esper.kafaserialize;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class KryoKafkaDeserializer<T> implements Deserializer<T>
{

	public void configure(Map<String, ?> configs, boolean isKey)
	{

	}

	public T deserialize(String topic, byte[] data)
	{
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

		Input input = new Input(data);

		@SuppressWarnings("unchecked")
		T event = (T) kryo.readClassAndObject(input);
		input.close();
		return event;
	}

	public void close()
	{

	}

}
