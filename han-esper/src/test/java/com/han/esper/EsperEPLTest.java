package com.han.esper;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.util.DateTime;
import com.espertech.esper.epl.annotation.AnnotationUtil;
import com.espertech.esperio.kafka.KafkaOutputDefault;

public class EsperEPLTest
{

	private EPServiceProvider ePServiceProvider;

	private EPAdministrator admin;

	@Before
	public void before()
	{

		Configuration engineConfig = new Configuration();
		engineConfig.addAnnotationImport(KafkaOutputDefault.class);
		
		ePServiceProvider = EPServiceProviderManager.getDefaultProvider(engineConfig);
	
		admin = ePServiceProvider.getEPAdministrator();
		admin.getConfiguration().addEventType("OrderEventTest", OrderEventTest.class);

	}

	@Test
	public void esperAnnotationTest()
	{
		String epl = "@Name('event') @KafkaOutputDefault select * from  OrderEventTest";

		EPStatement statement = admin.createEPL(epl);
	
		Annotation annotation = AnnotationUtil.findAnnotation(statement.getAnnotations(), KafkaOutputDefault.class);
		System.out.println(annotation.toString());
	}

	/**
	 * NOTE: 1.按条件实时更新数据，是建立在事件流之上，需要使用istream关键字 2.对应的实例对象强制要求实现{@
	 * Serializable}接口，否则会报错
	 */
	@Test
	public void orderEventUpdateByWhereTest()
	{
		String updateEpl = "update istream OrderEventTest set mark='0000'";
		admin.createEPL(updateEpl);

		EPStatement state = admin.createEPL("select * from OrderEventTest");
		state.addListener(new SupportUpdateListenerSub());

		EPRuntime runtime = ePServiceProvider.getEPRuntime();
		OrderEventTest b1 = new OrderEventTest();
		b1.setPrice(10);
		b1.setBuyerName("zhangsan");
		runtime.sendEvent(b1);
	}

	/**
	 * win:time_bacth:时间批量窗口就是按第一条事件到来之后每隔n秒计算一次 win:time:是滑动时间窗口
	 */
	@Test
	public void orderEventWindowTimeBatchTest()
	{

		// win:time_bacth、win:time的区别，是前面是事件批量窗口，每5秒统计一次，后面的是基于现在的时间向前滑动，统计最近五秒的值
		// 统计每5秒的时间间隔内price的平均价
		String epl = "select *, avg(price) as avgPrice from OrderEvent.win:time_batch(5 sec)";
		EPStatement state = admin.createEPL(epl);

		state.addListener(new SupportUpdateListenerSub());

		EPRuntime runtime = ePServiceProvider.getEPRuntime();
		OrderEventTest b1 = new OrderEventTest();
		b1.setPrice(10);
		b1.setBuyerName("zhangsan");

		runtime.sendEvent(b1);

		OrderEventTest b2 = new OrderEventTest();
		b2.setPrice(100);
		b2.setBuyerName("lisi");
		runtime.sendEvent(b2);

		try
		{
			Thread.sleep(6000);
		} catch (InterruptedException e)
		{

			e.printStackTrace();
		}

		OrderEventTest b3 = new OrderEventTest();
		b3.setPrice(1000);
		b3.setBuyerName("wangwu");
		runtime.sendEvent(b3);

		OrderEventTest b4 = new OrderEventTest();
		b4.setPrice(200);
		b4.setBuyerName("wangwu");
		runtime.sendEvent(b4);

	}

	@Test
	public void orderEventWindowTimeTest() throws IOException
	{
		String epl = "select count(*) as orderCount   from  OrderEventTest.win:time(3 sec)";

		EPStatement state = admin.createEPL(epl);
		state.addListener(new SupportUpdateListenerSub());

		EPRuntime runtime = ePServiceProvider.getEPRuntime();
		OrderEventTest b1 = new OrderEventTest();
		b1.setPrice(10);
		b1.setBuyerName("zhangsan");

		runtime.sendEvent(b1);

		OrderEventTest b2 = new OrderEventTest();
		b2.setPrice(100);
		b2.setBuyerName("zhangsan");
		runtime.sendEvent(b2);

		try
		{
			Thread.sleep(4000);
		} catch (InterruptedException e)
		{

			e.printStackTrace();
		}

		OrderEventTest b3 = new OrderEventTest();
		b3.setPrice(1000);
		b3.setBuyerName("zhangsan");
		runtime.sendEvent(b3);

		System.in.read();

	}

	@Test
	public void orderEventInTimeWindowBySameIdTest()
	{

		String epl = "select count(*) as orderCount from pattern[every OrderEventTest(buyerName='zhangsan') where timer:within(5 seconds)]";

		// String epl="select *,count(*) as userCount from pattern[a=OrderEventTest ->
		// (every b=OrderEventTest(buyerName=a.buyerName,buyerName='zhangsan')) where
		// timer:within(5 seconds)] ";
		EPStatement state = admin.createEPL(epl);
		this.addListener(epl, state);

		EPRuntime runtime = ePServiceProvider.getEPRuntime();
		OrderEventTest b1 = new OrderEventTest();
		b1.setPrice(10);
		b1.setBuyerName("zhangsan");

		runtime.sendEvent(b1);

		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{

			e.printStackTrace();
		}

		OrderEventTest b2 = new OrderEventTest();
		b2.setPrice(100);
		b2.setBuyerName("zhangsan");
		runtime.sendEvent(b2);

		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException e)
		{

			e.printStackTrace();
		}

		OrderEventTest b3 = new OrderEventTest();
		b3.setPrice(1000);
		b3.setBuyerName("zhangsan");
		runtime.sendEvent(b3);
	}

	@SuppressWarnings("unused")
	@Test
	public void timerWithinTest()
	{

		ePServiceProvider.getEPAdministrator().getConfiguration().addEventType("SupportBean", SupportBean.class);

		sendCurrentTime(ePServiceProvider, "2017-02-01T09:00:00.000");
		SupportUpdateListener listener = new SupportUpdateListener();
		ePServiceProvider.getEPAdministrator()
				.createEPL("select * from pattern [(every SupportBean) where "
						+ (false ? "timer:withinmax(1 month, 10)" : "timer:within(1 month)") + "]")
				.addListener(listener);

		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E1", 0));
		System.out.println(listener.getAndClearIsInvoked());
		System.out.println(listener.getLastNewData().length);

		sendCurrentTimeWithMinus(ePServiceProvider, "2017-02-02T09:00:00.000", 1);
		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E2", 0));

		System.out.println(listener.getAndClearIsInvoked());

		sendCurrentTime(ePServiceProvider, "2017-11-01T09:00:00.000");
		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E3", 0));
		System.out.println(listener.getAndClearIsInvoked());
		System.out.println(listener.getLastNewData().length);
	}

	@Test
	public void runAssertionPatternNotFollowedBy()
	{
		ePServiceProvider.getEPAdministrator().getConfiguration().addEventType("SB", SupportBean.class);
		ePServiceProvider.getEPAdministrator().getConfiguration().addEventType("MD", SupportMarketDataBean.class);
		sendTimer(0, ePServiceProvider);

		String stmtText = "select a.* ,count(b) as bCount from pattern [ every(a=SB -> b=MD )where timer:within(5 sec) ]";
		EPStatement statement = ePServiceProvider.getEPAdministrator().createEPL(stmtText);
		SupportUpdateListener listener = new SupportUpdateListenerSub();
		statement.addListener(listener);

		sendTimer(10000, ePServiceProvider);

		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E1", 1));
		sendTimer(7000, ePServiceProvider);

		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E2", 2));
		sendTimer(8000, ePServiceProvider);

		ePServiceProvider.getEPRuntime().sendEvent(new SupportBean("E4", 1));

		sendTimer(9000, ePServiceProvider);

		ePServiceProvider.getEPRuntime().sendEvent(new SupportMarketDataBean("E5", "M1", 1d));
		ePServiceProvider.getEPRuntime().sendEvent(new SupportMarketDataBean("E5", "M1", 1d));

		ePServiceProvider.getEPRuntime().sendEvent(new SupportMarketDataBean("E5", "M1", 1d));
		System.out.println(listener.isInvoked());

	}

	private void sendTimer(long timeInMSec, EPServiceProvider epService)
	{
		CurrentTimeEvent theEvent = new CurrentTimeEvent(timeInMSec);
		EPRuntime runtime = epService.getEPRuntime();
		runtime.sendEvent(theEvent);
	}

	private void sendCurrentTime(EPServiceProvider epService, String time)
	{
		epService.getEPRuntime().sendEvent(new CurrentTimeEvent(DateTime.parseDefaultMSec(time)));
	}

	private void sendCurrentTimeWithMinus(EPServiceProvider epService, String time, long minus)
	{
		epService.getEPRuntime().sendEvent(new CurrentTimeEvent(DateTime.parseDefaultMSec(time) - minus));
	}

	private void addListener(String epl, EPStatement statement)
	{

		statement.addListener(new UpdateListener()
		{

			public void update(EventBean[] newEvents, EventBean[] oldEvents)
			{
				for (EventBean event : newEvents)
				{

					System.out.println(JSON.toJSONString(event));
				}

			}
		});

	}

}
