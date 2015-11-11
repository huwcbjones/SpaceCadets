package com.huwcbjones.chat.core;

import com.huwcbjones.util.AutoResetEvent;

import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Exchanger;

/**
 * Writes Frames to the client
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class WriteThread extends Thread {

    private AutoResetEvent _runQueue = new AutoResetEvent(false);
    private boolean _shouldQuit = false;
    private Queue<Frame> _frameQueue = new ConcurrentLinkedQueue<>();
    private ObjectOutputStream _output;

    public WriteThread(ObjectOutputStream output, String threadID) {
        this._output = output;
        this.setName(threadID);
    }


    @Override
    public void run() {
        /*while (!_shouldQuit) {
             for (Frame frame : _frameQueue) {
                if(_shouldQuit){
                    break;
                }
                try {
                    _output.writeObject(frame);
                    _frameQueue.remove(frame);
                } catch (Exception ex) {
                    if (ex.getMessage() != null && (ex.getMessage().contains("connection abort")|| ex.getMessage().contains("closed"))) {
                        this.quit();
                        return;
                    }
                    Log.Console(Log.Level.WARN, "Failed to send Frame: " + ex.getMessage());
                    _frameQueue.remove(frame);
                }
            }
        }*/
    }

    public int getQueueSize(){
        return this._frameQueue.size();
    }

    public void quit() {
        this._shouldQuit = true;
        this._runQueue.set();
        try {
            this._output.flush();
        } catch (Exception ex){

        }
    }

    public void write(Frame frame) {
        this._frameQueue.add(frame);
        try {
            _output.writeObject(frame);
            //this._runQueue.set();
        } catch (Exception ex){
            Log.Console(Log.Level.WARN, ex.toString());
        }
    }
}
