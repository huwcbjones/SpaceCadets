package com.huwcbjones.chat.core;

import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Writes Frames to the client
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class WriteThread extends Thread {

    private boolean _shouldQuit = false;
    private Queue<Frame> _frameQueue = new ConcurrentLinkedQueue<>();
    private ObjectOutputStream _output;

    public WriteThread(ObjectOutputStream output, String threadID) {
        this._output = output;
        this.setName("Client_#" + threadID);
    }


    @Override
    public void run() {
        while (!_shouldQuit) {
            for (Frame frame : _frameQueue) {
                if(_shouldQuit){
                    break;
                }
                try {
                    _output.writeObject(frame);
                    _frameQueue.remove(frame);
                } catch (Exception ex) {
                    if (ex.getMessage() != null && (ex.getMessage().contains("connection abort")|| ex.getMessage().contains("connection closed"))) {
                        this.quit();
                        return;
                    }
                    Log.Console(Log.Level.WARN, "Failed to send Frame: " + ex.getMessage());
                }
            }
        }
    }

    public int getQueueSize(){
        return this._frameQueue.size();
    }

    public void quit() {
        this._shouldQuit = true;
    }

    public void write(Frame frame) {
        this._frameQueue.add(frame);
    }
}
