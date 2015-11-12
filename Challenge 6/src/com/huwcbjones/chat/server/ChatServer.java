package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.*;
import com.huwcbjones.chat.server.commands.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
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

    private Client _client;
    private int _clientID = 1;
    private int _lobbyID = 1;

    private HashMap<Integer, Destination> _lobbies = new HashMap<>();
    private HashMap<Integer, ClientThread> _clients = new HashMap<>();
    private HashMap<String, Integer> _clientName = new HashMap<>();

    public ChatServer(int port) {
        this._port = port;
        this._client = new Client(0);
        this._client.setNickname("Server");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                _shouldQuit = true;
                shutdownServer();
            }
        });
    }

    public Client getClient(int clientID) throws IndexOutOfBoundsException {
        if (clientID == 0) {
            return this._client;
        } else {
            ClientThread client = this._clients.get(clientID);
            return client.getClient();
        }
    }

    public Client getClient(String nickname) throws IndexOutOfBoundsException {
        return getClient(this._clientName.get(nickname));
    }

    public void updateClientLinks(Client client) {
        Client serverClient = this.getClient(client.getClientID());
        this._clientName.remove(serverClient.getNickname());
        this._clientName.put(client.getNickname(), client.getClientID());
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

                this._lobbies.get(1).addClient(client);

                // Put it targets HashMap
                _clients.put(client.getClientID(), client);

                // Run client thread
                client.run();


                this._clientID++;

            }
        } catch (IOException ex) {
            Log.Console(Log.Level.FATAL, ex.getMessage());
        }
    }

    /**
     * Sends disconnect to all clients, then shuts server down
     */
    private void shutdownServer() {
        Log.Console(Log.Level.WARN, "ChatServer shutting down...");

        Log.Console(Log.Level.INFO, "Sending disconnect to clients...");

        for (ClientThread client : this._clients.values()) {
            client.close(true);
        }

        Log.Console(Log.Level.INFO, "ChatServer safely shut down!");
    }

    public void broadcastMessage(Frame message) {
        for (ClientThread client : this._clients.values()) {
            client.write(message);
        }
    }

    public void processMessage(int clientID, Message message) {
        ClientThread client = this._clients.get(clientID);
        if (!this._lobbies.containsKey(client.getClient().getLobby())) {
            Log.Console(Log.Level.WARN, "Client #" + client.getClientID() + " (" + client.getClient().getNickname() + ") tried to send a message to an unknown lobby.");
            message = new Message(0, 0, "Lobby not found.");
            message.setUser(this.getClient(0).getNickname());
            client.write(new Frame(Frame.Type.P_MESSAGE, message));
            return;
        }
        message.setUser(this._clients.get(message.getClientID()).getClient().getNickname());
        Log.Console(Log.Level.INFO, "Message({from:" + client.getClient().getNickname() + "}, {to:"
                + _lobbies.get(client.getClient().getLobby()).getName() + "}, {msg:"
                + message.getMessage() + "})");
        Destination lobby = this._lobbies.get(message.getLobbyID());
        lobby.message(message);
    }

    public void processClientCommand(int clientID, String commandStr) {
        if (commandStr.charAt(0) != '!') {
            this.processMessage(clientID, new Message(clientID, 0, commandStr));
        } else {
            commandStr = commandStr.substring(1);

            ArrayList<String> cmdStructure = new ArrayList<>(Arrays.asList(commandStr.split(" ")));

            // Copy the params into a new array
            ArrayList<String> cmdParams = new ArrayList<>(cmdStructure);

            // Remove index 0 (the actual command)
            cmdParams.remove(0);

            // Hammer time

            try {
                Command command = this.getCommand(cmdStructure.get(0));

                try {
                    command.addArguments(cmdParams);
                } catch (Exception ex) {
                    Message message = new Message(0, 0, ex.getMessage());
                    message.setUser(this.getClient(0).getNickname());
                    this._clients.get(clientID).write(new Frame(Frame.Type.P_MESSAGE, message));
                }

                try {
                    command.execute(this._clients.get(clientID), this);
                } catch (Exception ex) {
                    Message message = new Message(0, 0, ex.getMessage());
                    message.setUser(this.getClient(0).getNickname());
                    this._clients.get(clientID).write(new Frame(Frame.Type.P_MESSAGE, message));
                }
            } catch (NoClassDefFoundError ex) {
                Log.Console(Log.Level.FATAL, "Failed to load command. Is command available and classpath set?");
                Message message = new Message(0, 0, "Error loading command. Contact server administrator.");
                message.setUser(this.getClient(0).getNickname());
                this._clients.get(clientID).write(new Frame(Frame.Type.P_MESSAGE, message));
            } catch (Exception ex) {
                Message message = new Message(0, 0, "Command \"" + cmdStructure.get(0) + "\" not found.");
                message.setUser(this.getClient(0).getNickname());
                this._clients.get(clientID).write(new Frame(Frame.Type.P_MESSAGE, message));
            }
        }
    }

    public Command getCommand(String name) throws Exception, NoClassDefFoundError {
        ClassLoader classLoader = this.getClass().getClassLoader();

        // Creates the new class (will throw an exception if class is not found, but this is handled)
        Class<?> newCommandClass = classLoader.loadClass(ChatServer.class.getPackage().getName() + ".commands." + name);
        Command cmd = (Command) newCommandClass.newInstance();
        return cmd;
    }

    private void addDestination(String name) {
        Destination destination = new Destination(this, name, _lobbyID);
        this._lobbies.put(_lobbyID, destination);
        Log.Console(Log.Level.INFO, "Added Destination \"" + name + "\", #" + destination.getLobbyID());
        _lobbyID++;
    }

    public HashMap<Integer, Destination> getDestinations() {
        return this._lobbies;
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

    public void write(int TargetID, Frame frame) throws IndexOutOfBoundsException {
        this._clients.get(TargetID).write(frame);
    }

}
