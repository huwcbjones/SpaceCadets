package com.huwcbjones.chat.core;

import com.huwcbjones.chat.server.ClientThread;

import java.util.HashMap;

/**
 * Destination of Messages
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class Destination {

    private String _name;
    private HashMap<Integer, ClientThread> _clients= new HashMap<>();

    public Destination(String name){
        this._name = name;
    }

    public void addClient(ClientThread client){
        _clients.put(client.getClientID(), client);
    }

    public void removeClient(int clientID){}

    public void message(Message message){
        for(ClientThread client: this._clients.values()){
        }
    }
}
