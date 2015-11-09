package com.huwcbjones.chat.core;

import java.io.Serializable;

/**
 * Class that represents the user
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Client implements Serializable {

    private String _name = null;
    private String _username = null;
    private int _clientID;
    private int _lobbyID;

    public Client(int clientID) {
        this._clientID = clientID;
    }

    public int getClientID() {
        return this._clientID;
    }

    public void setUsername(String username) {
        if (this._username == null) {
            this._username = username;
        }
    }

    public String getUsername() {
        return this._username;
    }

    public void setName(String name) {
        if (this._name == null) {
            this._name = name;
        }
    }

    public String getName() {
        return this._name;
    }

    public void setLobby(int lobbyID) {
        this._lobbyID = lobbyID;
    }

    public int getLobby() {
        return this._lobbyID;
    }
}
