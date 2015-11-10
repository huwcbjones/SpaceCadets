package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.*;
import com.huwcbjones.chat.core.exceptions.ClientNotConnectedException;

import java.io.*;
import java.net.Socket;

/**
 * Handles Clients on the ChatServer
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class ClientThread extends Thread {

    private int _clientID;
    private Socket _socket;
    private ObjectInputStream _in;
    private ObjectOutputStream _out;
    private ChatServer _server;

    private Client _client = null;

    private WriteThread _write;
    private ClientReadThread _read;

    private boolean _isConnected = false;

    public ClientThread(ChatServer chatServer, Socket socket, int clientID) {
        this._server = chatServer;
        this._socket = socket;
        this._clientID = clientID;
        this._client = new Client(clientID);
    }

    /**
     * Gets ClientID
     *
     * @return ClientID
     */
    public int getClientID() {
        if (this._client != null) {
            return this._client.getClientID();
        } else {
            return this._clientID;
        }
    }

    public void setClient(Client client) {
        if (this._client.getClientID() == client.getClientID()) {
            this._client = client;
            Log.Console(Log.Level.INFO, "ChatClient ID #" + _clientID + " is known as " + client.getName() + " (" + client.getUsername() + ").");
        }
    }

    public Client getClient() {
        return this._client;
    }

    /**
     * Runs client thread
     */
    @Override
    public void run() {
        try {
            Log.Console(Log.Level.INFO, "New connection from " + this._socket.getInetAddress() + ":" + this._socket.getPort());

            // Output stream needs to be first as inputStream is blocking
            // Get output stream
            this._out = new ObjectOutputStream(this._socket.getOutputStream());

            // Get input stream
            this._in = new ObjectInputStream(this._socket.getInputStream());

            Protocol protocol = new Protocol(this._in, this._out);
            protocol.connect(this._server);

            this._isConnected = true;
            Log.Console(Log.Level.INFO, "ChatClient ID #" + _clientID + " connected!");

            this._write = new WriteThread(_out, "Client_#" + this._clientID + "_ClientWrite");
            this._read = new ClientReadThread(this, this._server, _in);

            this._read.start();
            this._write.start();

            // Send Client object to client
            this._write.write(new Frame(Frame.Type.CLIENT_SEND, this._client));

        } catch (Exception ex) {
            Log.Console(Log.Level.FATAL, "Connection to client ID #" + _clientID + " failed: " + ex.getMessage());
        }
    }

    /**
     * Closes connection to client
     *
     * @param serverTriggered If server triggered this, then send a disconnect frame to client
     */
    public void close(boolean serverTriggered) {
        Log.Console(Log.Level.INFO, "Connection to client ID #" + _clientID + " closing...");
        if (this._isConnected) {
            if (serverTriggered) {
                this.disconnect();
                this._write.quit();
            }
            try {
                this._in.close();
                this._out.close();
            } catch (Exception ex) {

            }
        }

        try {
            this._read.quit();
            this._write.quit();
            this._socket.close();
        } catch (IOException ex) {

        }
        Log.Console(Log.Level.INFO, "Connection to client ID #" + _clientID + " closed.");

    }

    public void write(Frame frame){
        this._write.write(frame);
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
            Log.Console(Log.Level.WARN, "Failed to send message to client ID #" + _clientID);
        }
    }

    public void sendLobbies() {
        this._write.write(new Frame(Frame.Type.LOBBY_GET, this._server.getDestinations()));
    }

    public void disconnect() {
        this._write.write(new Frame(Frame.Type.DISCONNECT, 0));
    }
}
