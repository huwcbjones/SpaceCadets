package com.huwcbjones.chat.client;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.Message;
import com.huwcbjones.chat.core.Protocol;

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

    private boolean _shouldQuit = false;

    public Client(int port, URI server) {
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
        try {
            this.connectToServer();
            this.LogMessage(ErrorLevel.INFO, "Connected to server \"" + this._server.getHost() + "\"");
        } catch (Exception ex) {
            this.LogMessage(ErrorLevel.ERROR, ex.getMessage());
        }
        if(_isConnected){
            this.LogMessage(ErrorLevel.INFO, "Closing connection to server...");
            try {
                this._out.writeObject(new Frame(Frame.Type.DISCONNECT, true));
            } catch (Exception ex){

            } finally {
                try {
                    this._socket.close();
                } catch (Exception ex) {

                }
                this.LogMessage(ErrorLevel.INFO, "Connection to server closed.");
            }
        }
    }

    private void connectToServer() throws IOException {
        this._socket = new Socket(this._server.getHost(), this._port);

        // Output stream needs to be first as inputStream is blocking
        // Get output stream
        this._out = new ObjectOutputStream(this._socket.getOutputStream());

        // Get input stream
        this._in = new ObjectInputStream(this._socket.getInputStream());



        try {
            Protocol protocol = new Protocol(this, this._socket, false);
            protocol.connect();
            this._isConnected = true;
            this.LogMessage(ErrorLevel.INFO, "Connected to server!");


        } catch (Exception ex) {

        } finally {
            try {
                this._socket.close();
            } catch (Exception ex) {
                this.LogMessage(ErrorLevel.INFO, "Connection to server closed.");
            }

        }
    }

    public void sendMessage(Message message) throws Exception {
        this._out.writeObject(new Frame(Frame.Type.MESSAGE, message));
    }
}
