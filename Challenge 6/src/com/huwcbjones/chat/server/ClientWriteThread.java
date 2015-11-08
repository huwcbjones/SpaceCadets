package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.base;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Writes Frames to the client
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class ClientWriteThread extends Thread {

    private boolean _shouldQuit = false;
    private Queue<Frame> _frameQueue = new ConcurrentLinkedQueue<>();
    private ObjectOutputStream _output;
    private base _parent;

    public ClientWriteThread(base parent, ObjectOutputStream output){
        this._parent = parent;
        this._output = output;
    }


    @Override
    public void run(){
        while(!_shouldQuit){
            for(Frame frame : _frameQueue){
                try {
                    _output.writeObject(frame);
                } catch (Exception ex) {
                    this._parent.LogMessage(base.ErrorLevel.WARN, ex.getMessage());
                }
            }
        }
    }

    public void quit(){
        this._shouldQuit = true;
    }

    public void write(Frame frame){
        this._frameQueue.add(frame);
    }
}
