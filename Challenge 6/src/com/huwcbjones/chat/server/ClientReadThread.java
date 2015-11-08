package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.Message;
import com.huwcbjones.chat.core.Protocol;
import com.huwcbjones.chat.core.base;

import java.io.ObjectInputStream;

/**
 * Reads Frames from client
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class ClientReadThread extends Thread {

    private boolean _shouldQuit = false;
    private ObjectInputStream _input;
    private Server _server;

    public ClientReadThread(Server server, ObjectInputStream input){
        this._server = server;
        this._input = input;
    }
    @Override
    public void run(){
        Frame frame;
        while(!_shouldQuit){
            try {
                frame = Protocol.readFrame(_input);
                switch(frame.getType()){
                    case MESSAGE:
                        this._server.processMessage((Message)frame.getObject());
                        break;
                    default:

                }
            } catch (Exception ex){
                this._server.LogMessage(base.ErrorLevel.WARN, ex.getMessage());
            }
        }
    }
}
