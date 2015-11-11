package com.huwcbjones.chat.core;

import java.io.Serializable;

/**
 * Class that represents the user
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Client implements Serializable {

    private String _nickname = null;
    private int _clientID;
    private int _lobbyID;

    public Client(int clientID) {
        this._clientID = clientID;
    }

    public int getClientID() {
        return this._clientID;
    }

    public void setNickname(String nickname) {
        if (this._nickname == null) {
            this._nickname = nickname;
        }
    }

    public String getNickname() {
        return this._nickname;
    }

    public void setLobby(int lobbyID) {
        this._lobbyID = lobbyID;
    }

    public int getLobby() {
        return this._lobbyID;
    }
}
