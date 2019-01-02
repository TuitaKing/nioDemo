package com.tuitaking.multiReactor.threadpool.reactor;

import com.tuitaking.singleReactor.acceptor.Acceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by lijing on 2018/12/2.
 */
public class Reactor  implements Runnable{
    protected final int port;
    protected final ServerSocketChannel serverChannel;
    protected final long timeout;
    protected Selector selector;

    public Reactor(int port, ServerSocketChannel serverChannel, long timeout){
        this.port = port;
        this.serverChannel = serverChannel;
        this.timeout = timeout;
    }
    private void init() throws IOException {
        selector = Selector.open();
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        SelectionKey key = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        key.attach(newAcceptor(selector,serverChannel));
    }
    public Acceptor newAcceptor(Selector selector,ServerSocketChannel serverChannel){
          return new Acceptor(selector,serverChannel);
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
