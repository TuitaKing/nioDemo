package com.tuitaking.singleReactor.acceptor;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by lijing on 2018/12/2.
 */
public class Acceptor implements Runnable {
    ServerSocketChannel serverSocket;
    public Acceptor(ServerSocketChannel serverSocket){
        this.serverSocket=serverSocket;
    }
    @Override
    public void run() {
        try {
            SocketChannel sk=serverSocket.accept();
            if(sk!=null){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
