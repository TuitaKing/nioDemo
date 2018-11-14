package com.tuitaking.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by lijing on 2018/11/4.
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel=SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",6666));
        socketChannel.configureBlocking(false);

        Selector selector=Selector.open();

        socketChannel.register(selector,SelectionKey.OP_WRITE);

        for(;;){
            selector.select(6000);
            Iterator<SelectionKey> selectionKeys=selector.selectedKeys().iterator();
            while(selectionKeys.hasNext()){
                SelectionKey sk=selectionKeys.next();
                if(sk.isConnectable()){
                    handleWrite(sk,selector);
                    socketChannel.register(selector,SelectionKey.OP_WRITE);
                }
                if(sk.isValid()&&sk.isReadable()){
                    handleRead(sk);
                }
                if(sk.isValid()&&sk.isWritable()){
                    handleWrite(sk,selector);
                }
            }
        }

    }
    public static void handleWrite(SelectionKey sk,Selector selector) throws IOException {
        SocketChannel writeChannel= (SocketChannel) sk.channel();
        ByteBuffer writeBuff=ByteBuffer.allocate(1024);
        writeBuff.put("this is from client".getBytes());
        writeBuff.flip();
        while(writeBuff.hasRemaining()){
            writeChannel.write(writeBuff);
        }


        writeChannel.register(selector, SelectionKey.OP_READ,writeChannel);
    }
    public static void handleRead(SelectionKey sk) throws IOException {
        SocketChannel read= (SocketChannel) sk.channel();
        ByteBuffer readBuff=ByteBuffer.allocate(1024);
        int readPos=read.read(readBuff);
        while(readPos > 0){
            readBuff.flip();
            while(readBuff.hasRemaining()){
                System.out.print((char)readBuff.get());
            }
            readPos = read.read(readBuff);
            if(readPos==0){
                    return;
            }
        }

    }
}
