package com.tuitaking.singleReactor.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by lijing on 2018/12/2.
 */
public class Handler implements Runnable {
    private int status=SelectionKey.OP_READ;
    private SocketChannel socket;
    final SelectionKey sk;
    public Handler(Selector sel, SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        // Optionally try first read now
        sk = socket.register(sel, 0);
        sk.attach(this); // 将Handler作为callback对象
        sk.interestOps(SelectionKey.OP_READ); // 第二步,接收Read事件
        sel.wakeup();
    }
    @Override
    public void run() {
        try {
            if (status == SelectionKey.OP_READ)
                read();
            else if (status == SelectionKey.OP_WRITE)
                send();
        } catch (IOException e) { }
    }

    private void process() {

    }
    private void read() throws IOException{
        process();
    }
    private void  send() throws IOException{

    }

}
