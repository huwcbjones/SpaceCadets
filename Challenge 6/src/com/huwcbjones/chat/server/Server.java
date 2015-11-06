package com.huwcbjones.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.rmi.server.ExportException;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Server {

    private ServerSocket _serverSocket;
    private int _port = 60666;

    public Server(int port){
        this._port = port;
    }

    public void run(){
        try{
            _serverSocket = new ServerSocket(this._port);
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
