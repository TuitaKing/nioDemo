package com.tuitaking.multiReactor.multiReactors.reactors;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

public class ServerReactor implements Runnable {
	private SelectorProvider selectorProvider = SelectorProvider.provider();
	private ServerSocketChannel serverSocketChannel;

	public ServerReactor(int port) throws IOException {
		serverSocketChannel = selectorProvider.openServerSocketChannel();
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress("localhost", port), 1024);
		serverSocketChannel.configureBlocking(false);
	}

	public void run() {
		try {
			new ServerDispatcher(serverSocketChannel, SelectorProvider.provider()).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}