package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.base;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Reads Frames from client
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class ClientReadThread extends Thread {

    private boolean _shouldQuit = false;
    private ObjectInputStream _input;
    private base _parent;

    public ClientReadThread(base parent, ObjectInputStream input){
        this._parent = parent;
        this._input = input;
    }
    @Override
    public void run(){

    }
}
