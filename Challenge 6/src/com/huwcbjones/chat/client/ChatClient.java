package com.huwcbjones.chat.client;

import com.huwcbjones.chat.core.*;

import java.io.Console;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.text.SimpleDateFormat;
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

    private WriteThread write;
    private ServerReadThread read;

    private boolean _shouldQuit = false;

    private int _currentLobby = 0;

    private String _nickname = null;
    private Client client = null;

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
            this.client.setNickname(this._nickname);

            if (this._isConnected) {
                this.write.write(new Frame(Frame.Type.CLIENT_SEND, this.client));
            }
        } else {
            this.client.setNickname(this._nickname);

            if (this._isConnected) {
                this.write.write(new Frame(Frame.Type.CLIENT_SEND, this.client));
            }
        }
    }

    /**
     * Runs ChatClient
     */
    public void run() {
        if (System.console() == null) {
            Log.Console(Log.Level.FATAL, "Console not found. Console is required to run in CLI mode.");
            System.exit(0);
            return;
        }
        if (this._nickname == null) {
            this.getClientDetails();
        }
        try {
            this.connectToServer();
        } catch (Exception ex) {
            Log.Console(Log.Level.FATAL, ex.toString());
        }
        if (!this._isConnected) {
            return;
        }
        try {
            this.write = new WriteThread(_out, "Client_ServerWrite");
            this.read = new ServerReadThread(this, _in);
        } catch (Exception ex) {
            Log.Console(Log.Level.FATAL, ex.toString());
        }

        try {
            this.read.start();
            this.write.start();
        } catch (Exception ex) {
            Log.Console(Log.Level.FATAL, ex.toString());
        }
        interact();
    }

    public boolean runGUI() throws Exception {
        this.connectToServer();

        if (!this._isConnected) {
            return false;
        }
        try {
            this.write = new WriteThread(_out, "Client_ServerWrite");
            this.read = new ServerReadThread(this, _in);
        } catch (Exception ex) {
            Log.Console(Log.Level.FATAL, ex.toString());
            throw ex;
        }

        try {
            this.read.start();
            this.write.start();
        } catch (Exception ex) {
            Log.Console(Log.Level.FATAL, ex.toString());
            throw ex;
        }
        return true;
    }

    public boolean setNickname(String nickname) {
        if (!nickname.matches("^([A-Za-z ])*$")) {
            return false;
        }

        this._nickname = nickname;
        if (this._isConnected) {
            this.write.write(new Frame(Frame.Type.CLIENT_SEND, this.client));
        }
        return true;
    }


    private void getClientDetails() {
        Console c = System.console();
        if (c == null) {
            Log.Console(Log.Level.FATAL, "Console not found. Console is required to run in CLI mode.");
            System.exit(0);
            return;

        }
        boolean isValid = false;
        while (!isValid) {
            String username = c.readLine("Nickname> ");
            if (!this.setNickname(username)) {
                Log.Console(Log.Level.WARN, "Nicknames can only contain alphanumeric characters and underscores.");
            } else {
                isValid = true;
            }
        }
    }

    public String getNickname(){
        if(this.client != null) {
            return this.client.getNickname();
        }else{
            return this._nickname;
        }
    }

    private void connectToServer() throws Exception {
        try {
            this._socket = new Socket(this._server.getHost(), this._port);

            // Output stream needs to be first as inputStream is blocking
            // Get output stream
            this._out = new ObjectOutputStream(this._socket.getOutputStream());

            // Get input stream
            this._in = new ObjectInputStream(this._socket.getInputStream());


            Protocol protocol = new Protocol(this._in, this._out);
            protocol.connect();
            this._isConnected = true;

            Log.Console(Log.Level.INFO, "Connected to server \"" + this._server.getHost() + "\"");

        } catch (Exception ex) {
            Log.Console(Log.Level.FATAL, "Connection to server failed: " + ex.getMessage());
            throw ex;
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

    public void setLobby(int lobby) {
        this._currentLobby = lobby;
        if (this.client != null) {
            this.client.setLobby(lobby);
        }
    }

    public void displayMessage(Message message) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        Log.Console(Log.Level.MSG, "<" + format.format(message.getTimestamp()) + "> [" + message.getUser() + "]: " + message.getMessage());
    }

    public void close() {
        this.write.write(new Frame(Frame.Type.DISCONNECT, 0));
    }

    public void quit() {
        this._shouldQuit = true;
        try {
            this.write.quit();
            this.read.quit();
            this._socket.close();
        } catch (Exception ex) {

        }
        Log.Console(Log.Level.INFO, "Connection to server closed.");
    }

    private void interact() {
        Console c = System.console();
        if (c == null) {
            Log.Console(Log.Level.FATAL, "Console not found. Console is required to run in CLI mode.");
            this.close();
            return;
        }
        while (!this._shouldQuit) {
            String input = c.readLine("> ");
            if (this._shouldQuit) {
                return;
            }
            this.message(input);
        }
    }

    public void message(String input) {
        if (input.charAt(0) == '!') {
            this.write.write(new Frame(Frame.Type.COMMAND, input));
        } else {
            Message message = new Message(this.client.getClientID(), _currentLobby, input);
            this.write.write(new Frame(Frame.Type.MESSAGE, message));
        }
    }
}
