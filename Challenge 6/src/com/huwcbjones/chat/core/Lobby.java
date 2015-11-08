package com.huwcbjones.chat.core;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Lobby Base Class
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public abstract class Lobby implements Serializable {

    private static final long serialVersionUID = 1;

    private int _lobbyID;
    private String _name;
    private HashMap<Integer, Client> _clients = new HashMap<Integer, Client>();

    public String getName() {
        return this._name;
    }

    public int getNumberOfClients() {
        return _clients.size();
    }

    public int getLobbyID() {
        return this._lobbyID;
    }

    public void addClient(Client client) {
        if (!_clients.containsKey(client.getClientID())) {
            _clients.put(client.getClientID(), client);
        }
    }

    public void removeClient(int clientID) {
        if (_clients.containsKey(clientID)) {
            _clients.remove(clientID);
        }
    }
}
