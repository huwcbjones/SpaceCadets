package com.huwcbjones.chat.core;

import com.huwcbjones.chat.server.ChatServer;
import com.huwcbjones.chat.server.ClientThread;

import java.util.HashMap;

/**
 * Destination of Messages
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class Destination extends Lobby {

    private static final long serialVersionUID = 1;

    private ChatServer _server;
    private HashMap<Integer, ClientThread> _clients= new HashMap<>();

    public Destination(ChatServer chatServer, String name, int destinationID){
        super(destinationID, name);
        this._server = chatServer;
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
