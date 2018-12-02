package com.tuitaking.singleReactor.reactor;

import com.tuitaking.singleReactor.acceptor.Acceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by lijing on 2018/12/2.
 */
public class Reactor implements Runnable{
    final Selector selector;
    final ServerSocketChannel serverSocket;
    public Reactor(int port) throws IOException {
        selector=Selector.open();
        serverSocket=ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false); //设置为非阻塞，据说底层会不断轮询。
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT); // 分治法,将accept事件和处理分离
        sk.attach(new Acceptor(serverSocket));

    }
    @Override
    public void run() {

    }
}
