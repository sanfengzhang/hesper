package com.han.esper;

import java.util.concurrent.CountDownLatch;

import com.han.esper.http.EsperHttpServer;

public class Bootstrap
{

	private static volatile Bootstrap INSTANCE;

	private final CountDownLatch keepAliveLatch = new CountDownLatch(1);

	private final Thread keepAliveThread;

	private AbstractEscEsperServer server;

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

		server = new EsperHttpServer();

	}

	public static void main(String[] args)
	{
		INSTANCE = new Bootstrap();
		INSTANCE.init();

	}

	public void init()
	{
		keepAliveThread.start();
		server.initEsper();

	}

}
