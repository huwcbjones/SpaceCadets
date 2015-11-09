package com.huwcbjones.chat.client;

import com.huwcbjones.chat.core.*;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * Reads Frames from the server
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class ServerReadThread extends Thread {

    private boolean _shouldQuit = false;
    private ObjectInputStream _input;
    private ChatClient _parent;

    public ServerReadThread(ChatClient parent, ObjectInputStream input) {
        this._parent = parent;
        this._input = input;
        this.setName("Client_ServerRead");
    }

    @Override
    public void run() {
        Frame frame;
        while (!_shouldQuit) {
            try {
                frame = Protocol.readFrame(_input);
                switch (frame.getType()) {
                    case CLIENT_SEND:
                        this._parent.setClient((Client)frame.getObject());
                        break;
                    case P_MESSAGE:
                        if(frame.getObject() instanceof Message){

                        }
                        break;
                    case MESSAGE:
                        //this._parent.processMessage((Message) frame.getObject());
                        break;
                    case MOTD:
                        if(frame.getObject() instanceof  String){
                            System.out.print(frame.getObject());
                        }
                        break;
                    case LOBBY_CHANGE:
                        if(frame.getObject() instanceof  Integer){
                            this._parent.setLobby((int)frame.getObject());
                        }
                        break;
                    case LOBBY_GET:
                        if (frame.getObject() instanceof Map) {
                            this._parent.displayLobbies((Map<Integer, Destination>) frame.getObject());
                        } else {
                            Log.Console(Log.Level.WARN, "Invalid response received for LOBBY_GET.");
                        }

                        break;
                    case DISCONNECT:
                        this._parent.close();
                        return;

                    default:

                }
            } catch (EOFException ex) {
                this.quit();
                this._parent.close();
            } catch (Exception ex) {
                Log.Console(Log.Level.WARN, "ChatServer exception: " + ex.getMessage());
                if (ex.getMessage() != null && (ex.getMessage().contains("Connection reset") || ex.getMessage().contains("closed"))) {
                    this.quit();
                    System.exit(0);
                }
            }
        }
    }

    public void quit() {
        this._shouldQuit = true;
    }
}
