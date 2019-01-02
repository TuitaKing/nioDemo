package com.tuitaking.singleReactor.acceptor;

import com.tuitaking.singleReactor.handler.Handler;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by lijing on 2018/12/2.
 */
public class Acceptor implements Runnable {
    ServerSocketChannel serverSocket;
    Selector selector;
    public Acceptor(Selector sl,ServerSocketChannel serverSocket){
        this.serverSocket=serverSocket;
        this.selector    =sl;
    }
    @Override
    public void run() {
        try {
            SocketChannel sc=serverSocket.accept();
            if(sc!=null){
                new Handler(selector,sc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
