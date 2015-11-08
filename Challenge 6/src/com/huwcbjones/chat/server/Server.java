package com.huwcbjones.chat.server;

import com.huwcbjones.chat.core.Destination;
import com.huwcbjones.chat.core.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 * Contains Server functionality
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Server extends com.huwcbjones.chat.core.base {

    private boolean _shouldQuit = false;
    private ServerSocket _serverSocket;
    private int _port = 60666;

    private int _clientID = 1;

    private HashMap<Integer, Destination> _targets = new HashMap<>();
    private HashMap<Integer, ClientThread> _clients = new HashMap<>();

    public Server(int port) {
        this._port = port;

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                _shouldQuit = true;
            }
        });
    }

    /**
     * Runs the server
     */
    public void run() {
        this.LogMessage(ErrorLevel.INFO, "Server starting...");
        try {
            // Open server socket for listening
            _serverSocket = new ServerSocket(this._port);
            this.LogMessage(ErrorLevel.INFO, "Listening on " + this._port);
            ClientThread client;

            this.addDestination("Lobby");

            this.LogMessage(ErrorLevel.INFO, "Server successfully started!");
            // Run server
            while(!this._shouldQuit) {

                // On accept, create a new client thread
                client = new ClientThread(this, _serverSocket.accept(), this._clientID);
                this._clientID++;

                // Put it targets HashMap
                _clients.put(client.getClientID(), client);

                // Run client thread
                client.run();
            }
            this.LogMessage(ErrorLevel.WARN, "Server shutting down...");

            this.LogMessage(ErrorLevel.INFO, "Server safely shut down!");
        } catch (IOException ex) {
            this.LogMessage(ErrorLevel.ERROR, ex.getMessage());
        }
    }

    public void processMessage(Message message) throws Exception {
        /*if (!_targets.containsKey(message.getTarget().getTarget())) {
            throw new TargetNotFoundException();
        }*/
    }



    public void addDestination(String name){
        Destination destination = new Destination(this, name);
        this._targets.put(destination.hashCode(), destination);
        this.LogMessage(ErrorLevel.INFO, "Added Destination \"" + name + "\", #" + destination.getDestinationID());
    }
}
