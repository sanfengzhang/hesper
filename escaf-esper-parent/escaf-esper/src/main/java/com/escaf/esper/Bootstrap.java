package com.escaf.esper;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.escaf.esper.core.EscafEsperService;

public class Bootstrap
{

	private static volatile Bootstrap INSTANCE;

	private final CountDownLatch keepAliveLatch = new CountDownLatch(1);

	private final Thread keepAliveThread;

	private EscafEsperService server = new EscafEsperService();

	private static final Log log = LogFactory.getLog(Bootstrap.class);

	public Bootstrap()
	{

		keepAliveThread = new Thread(new Runnable()
		{

			public void run()
			{
				try
				{
					keepAliveLatch.await();
				} catch (InterruptedException e)
				{

				}

			}
		}, "esperEngine[keepAlive]");

		keepAliveThread.setDaemon(false);

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{

				keepAliveLatch.countDown();
			}
		});
	}

	public static void main(String[] args)
	{
		log.info("start escaf.esper params=" + ArrayUtils.toString(args));
		INSTANCE = new Bootstrap();
		INSTANCE.init();

	}

	public void init()
	{
		keepAliveThread.start();
		server.startEPServiceEngine();

	}

}
