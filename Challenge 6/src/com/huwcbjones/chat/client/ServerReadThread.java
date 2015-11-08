package com.huwcbjones.chat.client;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.Message;
import com.huwcbjones.chat.core.Protocol;
import com.huwcbjones.chat.core.base;

import java.io.EOFException;
import java.io.ObjectInputStream;

/**
 * Reads Frames from the server
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class ServerReadThread extends Thread {

    private boolean _shouldQuit = false;
    private ObjectInputStream _input;
    private Client _parent;

    public ServerReadThread(Client parent, ObjectInputStream input){
        this._parent = parent;
        this._input = input;
    }

    @Override
    public void run() {
        Frame frame;
        while (!_shouldQuit) {
            try {
                frame = Protocol.readFrame(_input);
                switch (frame.getType()) {
                    case MESSAGE:
                        //this._parent.processMessage((Message) frame.getObject());
                        break;
                    case DISCONNECT:
                        this._parent.close();
                        return;
                    default:

                }
            } catch (EOFException ex){
                this.quit();
                this._parent.close();
            } catch (Exception ex) {
                this._parent.LogMessage(base.ErrorLevel.WARN, "Server exception: " + ex.getMessage());
                if (ex.getMessage() != null && ex.getMessage().contains("Connection reset")) {
                    this.quit();
                    this._parent.close();
                }
            }
        }
    }

    public void quit() {
        this._shouldQuit = true;
    }
}
