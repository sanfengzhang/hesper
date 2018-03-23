package com.han.esper;

import com.han.esper.httpserver.SupportHTTPServer;

public class Server {

	public static void main(String[] args) throws Exception {
		SupportHTTPServer server = new SupportHTTPServer(8080);
		server.start();

		System.in.read();

	}

}
