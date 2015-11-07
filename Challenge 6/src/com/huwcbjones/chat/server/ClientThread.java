package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.Message;
import com.huwcbjones.chat.core.exceptions.ClientNotConnectedException;

import java.io.*;
import java.net.Socket;

/**
 * Handles Clients on the Server
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class ClientThread extends Thread {

    private Socket _socket;
    private ObjectInputStream _in;
    private ObjectOutputStream _out;
    private Server _server;
    private int _clientID = this.hashCode();

    private boolean _isConnected = false;

    public ClientThread(Server server, Socket socket) {
        this._server = server;
        this._socket = socket;
    }

    /**
     * Gets ClientID
     * @return ClientID
     */
    public int getClientID() {
        return this._clientID;
    }

    /**
     * Runs client thread
     */
    @Override
    public void run() {
        try {
            this._server.LogMessage(Server.ErrorLevel.INFO, "New connection from " + this._socket.getInetAddress() + ":" + this._socket.getPort());

            // Get client input
            this._in = new ObjectInputStream(this._socket.getInputStream());

            // Get client output
            this._out = new ObjectOutputStream(this._socket.getOutputStream());

            this._isConnected = true;
            this._out.writeObject(new Frame(Frame.Type.HELLO, "Welcome to the  Chat Server."));
            this._out.flush();
            this._server.LogMessage(Server.ErrorLevel.INFO, "Client connected! Client ID #" + _clientID);
        } catch (IOException ex) {
            this._server.LogMessage(Server.ErrorLevel.ERROR, "Connection to client ID #" + _clientID + " failed.");
        } finally {
            try {
                this._socket.close();
            } catch (IOException ex) {
                this._server.LogMessage(Server.ErrorLevel.INFO, "Connection to client ID #" + _clientID + " closed.");
            }
        }
    }

    /**
     * Sends a message to client
     *
     * @param message Message to send
     * @throws ClientNotConnectedException Thrown if client is not connected
     */
    public void message(Message message) throws ClientNotConnectedException {
        if(!this._isConnected){
            throw new ClientNotConnectedException();
        }
        try {
            this._out.writeObject(new Frame(Frame.Type.MESSAGE, message));
        } catch (Exception ex){
            this._server.LogMessage(Server.ErrorLevel.WARN, "Failed to send message to client ID #" + _clientID);
        }
    }
}
