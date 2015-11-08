package com.huwcbjones.chat.core;

import com.huwcbjones.chat.core.exceptions.InvalidFrameException;
import com.huwcbjones.chat.core.exceptions.InvalidProtocolException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Server/Client handshake protocol
 *
 * @author Huw Jones
 * @since 07/11/2015
 */
public class Protocol {

    private boolean _isServer;
    private Socket _socket;
    private base _parent;

    private ObjectInputStream _input;
    private ObjectOutputStream _output;

    public Protocol(base parent, Socket socket, boolean isServer) {
        this._socket = socket;
        this._isServer = isServer;
        this._parent = parent;
    }

    public void connect() throws Exception {
        this._output = new ObjectOutputStream(this._socket.getOutputStream());
        this._input = new ObjectInputStream(this._socket.getInputStream());

        if (this._isServer) {
            serverConnect();
        } else {
            clientConnect();
        }
        this._parent.LogMessage(base.ErrorLevel.INFO, "Server/Client successfully connected!");
    }

    private void clientConnect() throws Exception {
        try {
            Frame hello = this.readFrame();
            if (hello.getType() != Frame.Type.HELLO) {
                throw new InvalidProtocolException("Server protocol not understood.");
            }

        } catch (Exception ex) {
            this._parent.LogMessage(base.ErrorLevel.ERROR, "Server protocol not understood.");
        }

        this._output.writeObject(new Frame(Frame.Type.HELLO, "Hello Server."));
    }

    private void serverConnect() throws Exception {
        this._output.writeObject(new Frame(Frame.Type.HELLO, "Hello Client."));

        try {
            Frame hello = this.readFrame();
            if (!hello.isType(Frame.Type.HELLO)) {
                throw new InvalidProtocolException("Client protocol not understood.");
            }
        } catch (Exception ex) {
            this._parent.LogMessage(base.ErrorLevel.ERROR, "Client protocol not understood.");
        }
    }

    public Frame readFrame() throws Exception {
        Object frame = this._input.readObject();
        if (!(frame instanceof Frame)) {
            throw new InvalidFrameException();
        }
        return (Frame) frame;
    }

}
