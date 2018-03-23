package com.escaf.esper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Before;
import org.junit.Test;

import com.escaf.esper.kafaserialize.KryoKafkaSerializer;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("rawtypes")
public class ProduceEvent
{

	private Properties props = null;

	@Before
	public void before()
	{
		props = new Properties();
		props.put("bootstrap.servers", "192.168.1.100:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", KryoKafkaSerializer.class);
	}
	
	@Test
	public void sendEventTest()
	{

		KafkaProducer<String, Event> producer = new KafkaProducer<String, Event>(props);
		
		Event event=new Event();
		event.setAlertIndex("告警Esper0");
		event.setEventApplicationName("ESPER APP");
		event.setEventHostIp("192.168.1.100");
		event.setEventApplicationName("Jonyh");
		event.setEventLevel(2);
		event.setEventHappendDateTime(new Date());
		event.setEventId("test99");
		

		ProducerRecord<String, Event> record = new ProducerRecord<String, Event>("Save-Eeven-Kafka2", event);
		for (int i = 0; i < 1; i++)
		{
			producer.send(record, new Callback()
			{

				public void onCompletion(RecordMetadata metadata, Exception exception)
				{
					
					
				}

			});
		}

		producer.close();

	}

	
	
	@Test
	public void sendCloseSpringPluginEventTest()
	{

		KafkaProducer<String, Map> producer = new KafkaProducer<String, Map>(props);

		Map<String, String> map = new HashMap<String, String>();
		map.put("mapEventTypeName", "destorySpringContextPlugin");

		map.put("destorySpringContextPlugin", "destorySpringContextPlugin");

		ProducerRecord<String, Map> record = new ProducerRecord<String, Map>("eventTopic", map);
		producer.send(record, new Callback()
		{

			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception arg1)
			{
				System.out.println(recordMetadata.offset());

			}
		});
		producer.close();
	}

	@Test
	public void sendSatrtSpringPluginEventTest()
	{
		KafkaProducer<String, Map> producer = new KafkaProducer<String, Map>(props);

		Map<String, String> map = new HashMap<String, String>();
		map.put("mapEventTypeName", "startSpringContextEvent");

		map.put("startSpringContextEvent", "startSpringContextEvent");

		ProducerRecord<String, Map> record = new ProducerRecord<String, Map>("eventTopic", map);
		producer.send(record, new Callback()
		{

			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception arg1)
			{
				System.out.println(recordMetadata.offset());

			}
		});
		producer.close();
	}

	@Test
	public void sendSpringContextEventTest()
	{

		KafkaProducer<String, Map> producer = new KafkaProducer<String, Map>(props);

		Map<String, String> map = new HashMap<String, String>();
		map.put("mapEventTypeName", "myEvent");

		map.put("name", "zhangsan0000001");
		map.put("age", "12");

		map.put("remark", "test00001");

		ProducerRecord<String, Map> record = new ProducerRecord<String, Map>("eventTopic", map);
		producer.send(record, new Callback()
		{

			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception arg1)
			{
				System.out.println(recordMetadata.offset());

			}
		});
		producer.close();

	}

	@Test
	public void sendAddEventTest() throws UnsupportedEncodingException
	{

		KafkaProducer<String, Map> producer = new KafkaProducer<String, Map>(props);

		Map<String, String> map = new HashMap<String, String>();
		map.put("mapEventTypeName", "_def_addEvent");

		map.put("eventName", "orderEvent");

		map.put("eplContent", "@name('first') @KafkaOutputDefault select * from orderEvent");
		map.put("eventDefine", "{\"buyerName\":\"String\",\"price\": \"int\"}");

		String classContent = Base64.encode(loadStmtListener());

		map.put("eventListener", "{\"type\":\"class\",\"className\":\"DefaultEsperUpdateListener\",\"classContent\":\""
				+ classContent + "\"}");

		ProducerRecord<String, Map> record = new ProducerRecord<String, Map>("eventTopic", map);
		producer.send(record, new Callback()
		{

			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception arg1)
			{
				System.out.println(recordMetadata.offset());

			}
		});
		producer.close();

	}

	@Test
	public void sendAddEventClassEvent()
	{
		
		KafkaProducer<String, Map> producer = new KafkaProducer<String, Map>(props);

		Map<String, String> map = new HashMap<String, String>();
		map.put("mapEventTypeName", "_def_addEvent");

		map.put("eventName", "Event");

		map.put("eplContent", "@name('first') @KafkaOutputDefault select * from Event");
		
		String classContent = Base64.encode(loadEventClass());
		map.put("eventDefine", "{\"eventClassName\":\"com.escaf.event.storage.Event\",\"eventClassContent\":\""+ classContent + "\"}");

	

		map.put("eventListener", "{}");
		map.put("eventClassType", "class");

		ProducerRecord<String, Map> record = new ProducerRecord<String, Map>("sysEventTopic", map);
		producer.send(record, new Callback()
		{

			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception arg1)
			{
				System.out.println(recordMetadata.offset());

			}
		});
		producer.close();

	}
	
	

	@Test
	public void sendMapObjectTest()
	{

		KafkaProducer<String, Map> producer = new KafkaProducer<String, Map>(props);

		for (int i = 0; i < 5; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("mapEventTypeName", "orderEvent");
			map.put("buyerName", "zahngsa");

			map.put("price", 1000);

			ProducerRecord<String, Map> record = new ProducerRecord<String, Map>("eventTopic", map);
			producer.send(record, new Callback()
			{

				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception arg1)
				{
					System.out.println(recordMetadata.offset());

				}
			});
		}

		producer.close();

	}

	@Test
	public void sendOrderEventTest()
	{

		KafkaProducer<String, OrderEvent> producer = new KafkaProducer<String, OrderEvent>(props);

		for (int i = 0; i < 5; i++)
		{
			OrderEvent orderEvent = new OrderEvent();

			orderEvent.setBuyerName("zhangsan");

			orderEvent.setPrice(10 * i);

			ProducerRecord<String, OrderEvent> record = new ProducerRecord<String, OrderEvent>("eventTopic",
					orderEvent);
			producer.send(record, new Callback()
			{

				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception arg1)
				{
					System.out.println(recordMetadata.offset());

				}
			});
		}

		producer.close();

	}

	public static byte[] loadStmtListener()
	{
		try
		{
			FileInputStream fileInputStream = new FileInputStream(
					new File("C:\\Users\\owner\\Desktop\\DefaultEsperUpdateListener.class"));
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
	
	public static byte[] loadEventClass()
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
