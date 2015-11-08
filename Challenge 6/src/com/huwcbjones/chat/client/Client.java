package com.huwcbjones.chat.client;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.Message;
import com.huwcbjones.chat.core.Protocol;
import com.huwcbjones.chat.core.WriteThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.rmi.server.ExportException;

/**
 * Contains Client functionality
 *
 * @author Huw Jones
 * @since 06/11/2015
 */
public class Client extends com.huwcbjones.chat.core.base {

    private URI _server;
    private int _port;
    private Socket _socket;

    private boolean _isConnected = false;

    private ObjectOutputStream _out;
    private ObjectInputStream _in;

    private WriteThread _write;
    private ServerReadThread _read;

    private boolean _shouldQuit = false;

    public Client(int port, URI server) {
        this._port = port;
        this._server = server;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });
    }

    /**
     * Runs Client
     */
    public void run() {
        this.connectToServer();

        if (!this._isConnected) {
            return;
        }

        this._write = new WriteThread(this, _out);
        this._read = new ServerReadThread(this, _in);

        this._read.start();
        this._write.start();
    }

    private void connectToServer() {
        try {
            this._socket = new Socket(this._server.getHost(), this._port);

            // Output stream needs to be first as inputStream is blocking
            // Get output stream
            this._out = new ObjectOutputStream(this._socket.getOutputStream());

            this._out.flush();

            // Get input stream
            this._in = new ObjectInputStream(this._socket.getInputStream());


            Protocol protocol = new Protocol(this, this._socket, false);
            protocol.connect();
            this._isConnected = true;

            this.LogMessage(ErrorLevel.INFO, "Connected to server \"" + this._server.getHost() + "\"");

        } catch (Exception ex) {
            this.LogMessage(ErrorLevel.ERROR, "Connection to server failed: " + ex.getMessage());
        }
    }

    public void close(){
        try{
            this._in.close();
            this._out.close();
        } catch (Exception ex){

        }
        try {
            this._write.quit();
            this._read.quit();
            this._socket.close();
        } catch (Exception ex) {

        }
        this.LogMessage(ErrorLevel.INFO, "Connection to server closed.");
    }
}
