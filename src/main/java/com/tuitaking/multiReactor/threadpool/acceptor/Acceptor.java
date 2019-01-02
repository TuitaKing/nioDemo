package com.tuitaking.multiReactor.threadpool.acceptor;

import com.tuitaking.singleReactor.handler.Handler;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lijing on 2018/12/2.
 */
public class Acceptor implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService threadPoolExecutor= Executors.newFixedThreadPool(10);

    public Acceptor(Selector selector, ServerSocketChannel serverChannel){
        this.selector=selector;
        this.serverSocketChannel=serverChannel;
    }
    @Override
    public void run() {
        SocketChannel sc= null;
        try {
            sc = serverSocketChannel.accept();
            while(sc!=null){
                threadPoolExecutor.execute(new Handler(selector,sc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(sc!=null){
        }
    }
}
