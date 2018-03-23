package com.escaf.esper;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.escaf.esper.kafaserialize.KryoKafkaDeserializer;



public class KafkaConsumerTest
{

	public static void main(String[] args)
	{

		Properties p = new Properties();
		p.put("bootstrap.servers", "192.168.1.100:9092");
	//	p.put("value.deserializer", "com.escaf.esper.util.ObjectDeserializer");
		p.put("value.deserializer", KryoKafkaDeserializer.class);
		p.put("key.deserializer", StringDeserializer.class);
		p.put("group.id", "com1232111");
		p.put("enable.auto.commit", "true");

		p.put("auto.commit.interval.ms", "1000");
		p.put("session.timeout.ms", "30000");
		p.put("auto.offset.reset", "earliest");
		System.out.println(Thread.currentThread().getContextClassLoader());
		@SuppressWarnings("resource")
		KafkaConsumer<String, Event> kafkaConsumer = new KafkaConsumer<String, Event>(p);

		Set<String> set = new HashSet<String>();
		//set.add("esper-kafka-event");
		//set.add("eventTopic");
		set.add("Save-Eeven-Kafka4");
		kafkaConsumer.subscribe(set);
		while (true)
		{
			ConsumerRecords<String, Event> records = kafkaConsumer.poll(Long.MAX_VALUE);
			if (!records.isEmpty())
			{
				for (ConsumerRecord<String, Event> record : records)
				{

					System.out.println(record.value().toString());
				}
			}

		}

	}
}
