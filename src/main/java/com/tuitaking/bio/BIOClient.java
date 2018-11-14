package com.tuitaking.bio;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by lijing on 2018/11/4.
 */
public class BIOClient {
    public static void main(String[] args) {
        int port=6666;
        try {
            Socket socket=new Socket("localhost",port);
            socket.getOutputStream().write("test".getBytes("UTF-8"));
            byte[] input = new byte[4];
            socket.getInputStream().read(input);
            System.out.println(new String(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
