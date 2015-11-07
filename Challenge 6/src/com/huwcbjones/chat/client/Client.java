package com.huwcbjones.chat.client;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.Message;

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
public class Client {

    private URI _server;
    private int _port;
    private Socket _socket;

    private ObjectOutputStream _out;
    private ObjectInputStream _in;

    private boolean _shouldQuit = false;

    public enum ErrorLevel {
        INFO,
        WARN,
        ERROR
    }

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
        try{
            this.connectToServer();
            this.LogMessage(ErrorLevel.INFO, "Connected to server \"" + this._server.getHost() + "\"");
        } catch(Exception ex){
            this.LogMessage(ErrorLevel.ERROR, ex.getMessage());
        }
    }

    private void connectToServer() throws IOException {
        this._socket = new Socket(this._server.getHost(), this._port);
        this._in = new ObjectInputStream(this._socket.getInputStream());
        this._out = new ObjectOutputStream(this._socket.getOutputStream());

        this._out.writeObject(new Frame(Frame.Type.HELLO, "Hello Server!"));
        this._out.flush();
        try {
            Object hello = this._in.readObject();
            if(!(hello instanceof Frame)){
                this.LogMessage(ErrorLevel.ERROR, "Invalid Frame received.");
            }
            Frame frame = (Frame)hello;
            if(frame.getType() == Frame.Type.HELLO){
                System.out.println(frame.getObject());
            }
        } catch (Exception ex){
            this.LogMessage(ErrorLevel.ERROR, "Failed to read object.");
        }
    }

    public void sendMessage(Message message) throws Exception {
        this._out.writeObject(new Frame(Frame.Type.MESSAGE, message));
    }

    public void LogMessage(ErrorLevel level, String message){
        switch(level){
            case ERROR:
                System.out.print("[ ERR  ] ");
                break;
            case WARN:
                System.out.print("[ WARN ] ");
                break;
            case INFO:
                System.out.print("[ INFO ] ");
                break;
        }
        System.out.println(message);
    }
}
