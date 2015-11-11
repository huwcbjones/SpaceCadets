package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.*;

import java.io.EOFException;
import java.io.ObjectInputStream;

/**
 * Reads Frames from client
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
class ClientReadThread extends Thread {

    private boolean _shouldQuit = false;
    private ObjectInputStream _input;
    private ChatServer _server;
    private ClientThread _parent;

    public ClientReadThread(ClientThread parent, ChatServer chatServer, ObjectInputStream input) {
        this._parent = parent;
        this._server = chatServer;
        this._input = input;
        this.setName("Client_#" + parent.getClientID() + "_ClientRead");
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
                    case COMMAND:
                        this._server.processClientCommand(this._parent.getClientID(), (String)frame.getObject());
                        break;
                    case MESSAGE:
                        this._server.processMessage(this._parent.getClientID(), (Message) frame.getObject());
                        break;
                    case LOBBY_CHANGE:

                        break;
                    case LOBBY_GET:
                        Log.Console(Log.Level.INFO, "ChatClient #" + this._parent.getClientID() + " requested lobbies. Sending lobbies...");
                        this._parent.sendLobbies();
                        break;
                    case DISCONNECT:
                        this.quit();
                        this._parent.close(false);
                        return;
                    default:

                }
            } catch (EOFException ex){
                this.quit();
                this._parent.close(false);
            } catch (Exception ex) {
                Log.Console(Log.Level.WARN, "Exception on client #" + this._parent.getClientID() + ": " + ex.getMessage());
                if (ex.getMessage() != null && (ex.getMessage().contains("Connection reset") || ex.getMessage().contains("closed"))) {
                    this.quit();
                    this._parent.close(false);
                }
            }
        }
    }

    public void quit() {
        this._shouldQuit = true;
    }
}
