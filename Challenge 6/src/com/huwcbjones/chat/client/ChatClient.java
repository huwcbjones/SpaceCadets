package com.huwcbjones.chat.client;

import com.huwcbjones.chat.core.*;

import java.io.Console;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.Map;

/**
 * Contains ChatClient functionality
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class ChatClient {

    private URI _server;
    private int _port;
    private Socket _socket;

    private boolean _isConnected = false;

    private ObjectOutputStream _out;
    private ObjectInputStream _in;

    protected  WriteThread write;
    protected ServerReadThread read;

    private boolean _shouldQuit = false;

    private String _username;
    private String _name;
    protected Client client = null;

    public ChatClient(int port, URI server) {
        this._port = port;
        this._server = server;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (_isConnected) {
                    close();
                }
                quit();
            }
        });
    }

    public void setClient(Client client) {
        if (this.client == null) {
            this.client = client;
            this.client.setName(this._name);
            this.client.setUsername(this._username);

            this.write.write(new Frame(Frame.Type.CLIENT_SEND, this.client));
        }
    }

    /**
     * Runs ChatClient
     */
    public void run() {
        this.getClientDetails();
        this.connectToServer();

        if (!this._isConnected) {
            return;
        }

        this.write = new WriteThread(_out);
        this.read = new ServerReadThread(this, _in);

        this.read.start();
        this.write.start();

        interact();
    }

    private void getClientDetails() {
        Console c = System.console();
        if (c == null) {
            Log.Console(Log.Level.ERROR, "Console not found. Console is required to run in CLI mode.");
            this.close();
            return;
        }
        boolean isValid = false;
        while (!isValid) {
            String input = c.readLine("Username> ");
            if(!input.matches("^[a-zA-Z_\\d]*$")){
                Log.Console(Log.Level.WARN, "Usernames can only contain alphanumeric characters and underscores.");
            }else{
                this._username = input;
                isValid = true;
            }
        }
        isValid = false;
        while (!isValid) {
            String input = c.readLine("Name    > ");
            if(!input.matches("^([A-Za-z ])*$")){
                Log.Console(Log.Level.WARN, "Names can only contain alphanumeric characters and spaces.");
            }else{
                this._name = input;
                isValid = true;
            }
        }
    }

    private void connectToServer() {
        try {
            this._socket = new Socket(this._server.getHost(), this._port);

            // Output stream needs to be first as inputStream is blocking
            // Get output stream
            this._out = new ObjectOutputStream(this._socket.getOutputStream());

            this._out.flush();

            // Get input stream
            this._in = new ObjectInputStream(this._socket.getInputStream());


            Protocol protocol = new Protocol(this._socket, false);
            protocol.connect();
            this._isConnected = true;

            Log.Console(Log.Level.INFO, "Connected to server \"" + this._server.getHost() + "\"");

        } catch (Exception ex) {
            Log.Console(Log.Level.ERROR, "Connection to server failed: " + ex.getMessage());
        }
    }

    public void displayLobbies(Map<Integer, Destination> lobbies) {
        System.out.println("========================================");
        System.out.println("=          AVAILABLE LOBBIES           =");
        System.out.println("========================================");
        System.out.println("=  ID  = Name                   = Size =");
        for (Destination lobby : lobbies.values()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("= ");
            stringBuilder.append(String.format("%4s", lobby.getLobbyID()));
            stringBuilder.append(" = ");
            stringBuilder.append(String.format("%22s", lobby.getName()));
            stringBuilder.append(" = ");
            stringBuilder.append(String.format("%4s", lobby.getNumberOfClients()));
            stringBuilder.append(" =");
            System.out.println(stringBuilder.toString());
        }
        System.out.println("========================================");
    }

    public void close() {
        this.write.write(new Frame(Frame.Type.DISCONNECT, 0));
    }

    public void quit() {
        try {
            this._in.close();
            this._out.close();
        } catch (Exception ex) {

        }
        try {
            this.write.quit();
            this.read.quit();
            this._socket.close();
        } catch (Exception ex) {

        }
        Log.Console(Log.Level.INFO, "Connection to server closed.");
    }

    public void interact() {
        Console c = System.console();
        if (c == null) {
            Log.Console(Log.Level.ERROR, "Console not found. Console is required to run in CLI mode.");
            this.close();
            return;
        }
        while (!this._shouldQuit) {
            String input = c.readLine("> ");
            if (input.charAt(0) == '!') {
                this.write.write(new Frame(Frame.Type.COMMAND, input));
            } else {
                Message message = new Message(this.client, input);
                this.write.write(new Frame(Frame.Type.MESSAGE, message));
            }
        }
    }
}
