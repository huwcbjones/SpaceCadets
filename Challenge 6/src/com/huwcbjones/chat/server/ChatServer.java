package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.Destination;
import com.huwcbjones.chat.core.Log;
import com.huwcbjones.chat.core.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 * Contains ChatServer functionality
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class ChatServer {

    private boolean _shouldQuit = false;
    private ServerSocket _serverSocket;
    private int _port = 60666;

    private String _name = "Java Chat ChatServer";

    private int _clientID = 1;
    private int _lobbyID = 1;

    private HashMap<Integer, Destination> _targets = new HashMap<>();
    private HashMap<Integer, ClientThread> _clients = new HashMap<>();

    public ChatServer(int port) {
        this._port = port;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                _shouldQuit = true;
                shutdownServer();
            }
        });
    }

    /**
     * Runs the server
     */
    public void run() {
        Log.Console(Log.Level.INFO, "ChatServer starting...");
        try {
            // Open server socket for listening
            _serverSocket = new ServerSocket(this._port);
            Log.Console(Log.Level.INFO, "Listening on " + this._port);


            this.addDestination("Lobby");

            Log.Console(Log.Level.INFO, "ChatServer successfully started!");
            // Run server
            while (!this._shouldQuit) {

                // On accept, create a new client thread
                ClientThread client = new ClientThread(this, _serverSocket.accept(), this._clientID);
                this._clientID++;

                // Put it targets HashMap
                _clients.put(client.getClientID(), client);

                // Run client thread
                client.run();
            }
        } catch (IOException ex) {
            Log.Console(Log.Level.ERROR, ex.getMessage());
        }
    }

    /**
     * Sends disconnect to all clients, then shuts server down
     */
    public void shutdownServer() {
        Log.Console(Log.Level.WARN, "ChatServer shutting down...");

        Log.Console(Log.Level.INFO, "Sending disconnect to clients...");

        for (ClientThread client : this._clients.values()) {
            client.close(true);
        }

        Log.Console(Log.Level.INFO, "ChatServer safely shut down!");
    }

    public void processMessage(Message message) throws Exception {
        /*if (!_targets.containsKey(message.getTarget().getTarget())) {
            throw new TargetNotFoundException();
        }*/
    }

    public void addDestination(String name) {
        Destination destination = new Destination(this, name, _lobbyID);
        this._targets.put(_lobbyID, destination);
        Log.Console(Log.Level.INFO, "Added Destination \"" + name + "\", #" + destination.getLobbyID());
        _lobbyID++;
    }

    public HashMap<Integer, Destination> getDestinations() {
        return this._targets;
    }

    public String getMOTD() {
        StringBuilder motd = new StringBuilder();
        motd.append("***\n");

        motd.append("* Welcome to ");
        motd.append(this._name);
        motd.append("!\n");

        motd.append("* \n");

        motd.append("* Messaging:\n");
        motd.append("* To send a message, type the message and press return.\n");

        motd.append("* \n");

        motd.append("* Commands:\n");
        motd.append("* All commands being with ! (exclamation mark).\n");
        motd.append("* For help, type !help. This will give you a list of commands.\n");
        motd.append("* For help with a particular command, type !help [command].\n");

        motd.append("* \n");

        motd.append("***\n");

        return motd.toString();
    }

    public void processClientInput(int clientID, String command) {
        if (command.charAt(0) == '!') {
            this.processClientCommand(clientID, command);
        } else {

        }
    }

    private void processClientCommand(int clientID, String command) {

    }
}