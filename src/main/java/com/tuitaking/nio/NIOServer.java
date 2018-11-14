package com.tuitaking.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by lijing on 2018/11/4.
 */
public class NIOServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc= ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress("localhost",6666));

            Selector selector=Selector.open();
            ssc.configureBlocking(false);
            ssc.register(selector,SelectionKey.OP_ACCEPT);

            for(;;){
                int key=selector.select(6000);
               if( key == 0){
                  System.out.println("for a long time, there is no accept");
                   continue;
               }
            Iterator<SelectionKey> keyIterabletor=selector.selectedKeys().iterator();
                while(keyIterabletor.hasNext()){
                    SelectionKey selectionKey=keyIterabletor.next();
                    if(selectionKey.isAcceptable()){
                        handleAccept(selectionKey);
                    }
                    if(selectionKey.isValid()&&selectionKey.isReadable()){
                        handleRead(selectionKey);
                    }
                    if(selectionKey.isValid()&&selectionKey.isWritable()){
                        handleWrite(selectionKey);
                    }
                    keyIterabletor.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        //接受到请求后，将当前的sc注册为read，ssChannel只有一个accept的关心事件
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024));

    }

    public static void handleRead(SelectionKey key) throws IOException {
        SocketChannel ssChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer= (ByteBuffer) key.attachment();
        int readByte=ssChannel.read(byteBuffer);
        while(readByte > 0){
            //byteBuffer 有position和limit 两个指针
            //切换写和读，是将position的位置重置为0，也就是说读取当前已经写的数据，如果没有读取完，下次在读就会丢失。
            byteBuffer.flip();
            //判断是否超过界限（position>limit)
            while (byteBuffer.hasRemaining()) {
                System.out.print((char) byteBuffer.get());
            }
            System.out.println();
            byteBuffer.clear();
            readByte = ssChannel.read(byteBuffer);
            if(readByte==0){
                ssChannel.register(key.selector(), SelectionKey.OP_WRITE, ByteBuffer.allocateDirect(1024));
            }
        }



    }



    public static void handleWrite(SelectionKey key) throws IOException {
        SocketChannel ssChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer= (ByteBuffer) key.attachment();
        byteBuffer.put("it's from NIOServer".getBytes());
        //同读操作
        byteBuffer.flip();
        while(byteBuffer.hasRemaining()){
            ssChannel.write(byteBuffer);
        }
        ssChannel.close();

    }
}
