package com.tuitaking.singleReactor.handler;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by lijing on 2018/12/2.
 */
public class Handler implements Runnable {
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;
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
            if (state == SelectionKey.OP_READ)
                read();
            else if (state == SelectionKey.OP_WRITE)
                send();
        } catch (IOException e) { }
    }

    private void process() {
        byte[] bytes = new byte[input.position()];
        input.flip();
        input.get(bytes);
        input.clear();

        System.out.println("input:"+DatatypeConverter.printHexBinary(bytes));
    }
    private void read() throws IOException{
        socket.read(input);
        if (inputIsComplete()) {
            process();
            state = SENDING;
            // Normally also do first write now
            sk.interestOps(SelectionKey.OP_WRITE); // 第三步,接收write事件
        }


    }
    private void  send() throws IOException{
        output.clear();
        output.put("hello,nio".getBytes());
        output.flip();
        socket.write(output);
        if (outputIsComplete()){
            sk.interestOps(SelectionKey.OP_READ);
            state = READING;
            sk.cancel(); // write完就结束了, 关闭select key
        }

    }

    boolean inputIsComplete() {
        return true;
    }

    boolean outputIsComplete() {
        return true;
    }

}
