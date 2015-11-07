package com.huwcbjones.chat.client;

import java.net.Socket;
import java.net.URL;

/**
 * Contains Client functionality
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class Client {

    private URL _server;
    private int _port;
    private Socket _socket;

    private boolean _shouldQuit = false;

    public Client(int port, URL server) {
        this._port = port;
        this._server = server;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                _shouldQuit = true;
            }
        });
    }

    /**
     * Runs Client
     */
    public void run() {

    }

    public void sendMessage() {

    }

}
