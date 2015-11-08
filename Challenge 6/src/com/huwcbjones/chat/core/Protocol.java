package com.huwcbjones.chat.core;

import com.huwcbjones.chat.core.exceptions.InvalidFrameException;
import com.huwcbjones.chat.core.exceptions.InvalidProtocolException;

import java.io.IOException;
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
    }

    private void clientConnect() throws Exception {
        try {
            Frame hello = this.readFrame();
            if (hello.getType() != Frame.Type.HELLO) {
                throw new InvalidProtocolException("Server protocol not understood.");
            }

        } catch (Exception ex) {
            throw new InvalidProtocolException("Server protocol not understood.");
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
            throw new InvalidProtocolException("Client protocol not understood.");
        }
    }

    public Frame readFrame() throws Exception {
        return Protocol.readFrame(this._input);
    }

    /**
     * Reads a frame from the socket
     * @param inputStream Stream to read from
     * @return Frame received from socket
     * @throws IOException Thrown if there was a connection error.
     * @throws ClassNotFoundException Thrown if the class was not found.
     * @throws InvalidFrameException Thrown if Frame was invalid.
     */
    public static Frame readFrame(ObjectInputStream inputStream) throws IOException, ClassNotFoundException, InvalidFrameException {
        Object frame = inputStream.readObject();
        if (!(frame instanceof Frame)) {
            throw new InvalidFrameException();
        }
        return (Frame) frame;
    }

}
