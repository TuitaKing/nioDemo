package com.tuitaking.bio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lijing on 2018/11/4.
 */
public class BIOServer {
    public static ServerSocket ssocket;

    public static void main(String[] args) {
        int port = 6666;
        try {
            ssocket = new ServerSocket(port);
            while (true) {
                Socket socket = ssocket.accept();// 不断接收请求
                new Thread(new ServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerHandler implements Runnable {
    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            byte[] input = new byte[8];
            socket.getInputStream().read(input);//获取数据
            byte[] output = process(input);//处理数据
            socket.getOutputStream().write(output);//返回数据
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] process(byte[] b) {
        byte[] returnMessage = null;
        try {
            returnMessage = "2018".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }
}
