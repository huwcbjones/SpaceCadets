package com.huwcbjones.chat.core;

import com.huwcbjones.chat.server.ClientThread;
import com.huwcbjones.chat.server.Server;

import java.util.HashMap;

/**
 * Destination of Messages
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class Destination {

    private Server _server;
    private int _destinationID = this.hashCode();
    private String _name;
    private HashMap<Integer, ClientThread> _clients= new HashMap<>();

    public Destination(Server server, String name){
        this._server = server;
        this._name = name;
    }

    public String getName(){
        return this._name;
    }
    public int getNumberOfClients(){
        return _clients.size();
    }

    public int getDestinationID(){
        return this._destinationID;
    }

    public void addClient(ClientThread client){
        _clients.put(client.getClientID(), client);
    }

    public void removeClient(int clientID){
        this._server.LogMessage(Server.ErrorLevel.WARN, "Connection to Client ID#" + clientID + " was lost. Client removed.");
        _clients.remove(clientID);
    }

    public void message(Message message){
        for(ClientThread client: this._clients.values()){
            try {
                client.message(message);
            }catch (Exception ex){
                this.removeClient(client.getClientID());
            }
        }
    }
}
