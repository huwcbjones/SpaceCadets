package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.*;
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

    private int _clientID;
    private Socket _socket;
    private ObjectInputStream _in;
    private ObjectOutputStream _out;
    private Server _server;

    private WriteThread _write;
    private ClientReadThread _read;

    private boolean _isConnected = false;

    public ClientThread(Server server, Socket socket, int clientID) {
        this._server = server;
        this._socket = socket;
        this._clientID = clientID;
    }

    /**
     * Gets ClientID
     *
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

            // Output stream needs to be first as inputStream is blocking
            // Get output stream
            this._out = new ObjectOutputStream(this._socket.getOutputStream());
            this._out.flush();

            // Get input stream
            this._in = new ObjectInputStream(this._socket.getInputStream());

            Protocol protocol = new Protocol(this._server, this._socket, true);
            protocol.connect();


            this._isConnected = true;
            this._server.LogMessage(Server.ErrorLevel.INFO, "Client ID #" + _clientID + " connected!");

            this._write = new WriteThread(this._server, _out);
            this._read = new ClientReadThread(this, this._server, _in);

            this._read.start();
            this._write.start();

        } catch (Exception ex) {
            this._server.LogMessage(Server.ErrorLevel.ERROR, "Connection to client ID #" + _clientID + " failed: " + ex.getMessage());
        }
    }

    public void close(boolean serverTriggered) {
        this._server.LogMessage(Server.ErrorLevel.INFO, "Connection to client ID #" + _clientID + " closing...");
        if(serverTriggered) {
            this.disconnect();
            while(this._write.getQueueSize() != 0){
                try {
                    Thread.sleep(50);
                } catch (Exception ex){

                }
            }
        }
        try {
            this._in.close();
            this._out.close();
        } catch (Exception ex) {

        }

        try {
            this._read.quit();
            this._write.quit();
            this._socket.close();
        } catch (IOException ex) {

        }
        this._server.LogMessage(Server.ErrorLevel.INFO, "Connection to client ID #" + _clientID + " closed.");

    }

    /**
     * Sends a message to client
     *
     * @param message Message to send
     * @throws ClientNotConnectedException Thrown if client is not connected
     */
    public void message(Message message) throws ClientNotConnectedException {
        if (!this._isConnected) {
            throw new ClientNotConnectedException();
        }
        try {
            this._write.write(new Frame(Frame.Type.MESSAGE, message));
        } catch (Exception ex) {
            this._server.LogMessage(Server.ErrorLevel.WARN, "Failed to send message to client ID #" + _clientID);
        }
    }

    public void disconnect() {
        this._write.write(new Frame(Frame.Type.DISCONNECT, 0));
    }
}
