package com.tuitaking.singleReactor;

import com.tuitaking.singleReactor.reactor.*;

import java.io.IOException;

/**
 * Created by lijing on 2018/12/2.
 */
public class Main {
    public static void main(String[] args) {
        Reactor reactor = null;
        try {
            reactor = new Reactor(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(reactor).start();
    }
}
