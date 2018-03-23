package com.escaf.esper;

import org.junit.Test;

import com.escaf.esper.kafaserialize.ObjectDeserializer;
import com.escaf.esper.kafaserialize.ObjectSerializer;

public class SerializeTest
{

	@Test
	public void testSer()
	{
		OrderEvent orderEvent = new OrderEvent();
		orderEvent.setBuyerName("zhangsan");

		orderEvent.setPrice(10);

		@SuppressWarnings("resource")
		ObjectSerializer o = new ObjectSerializer();

		byte[] bt=o.serialize(null, orderEvent);
		
		@SuppressWarnings("resource")
		ObjectDeserializer objectDeserializer=new ObjectDeserializer();
		OrderEvent oo=(OrderEvent) objectDeserializer.deserialize(null, bt);
		
		System.out.println(oo.getBuyerName());
	}

}
