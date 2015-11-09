package com.huwcbjones.chat.core;

import com.huwcbjones.chat.core.exceptions.InvalidFrameException;
import com.huwcbjones.chat.core.exceptions.InvalidProtocolException;
import com.huwcbjones.chat.server.ChatServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ChatServer/ChatClient handshake protocol
 *
 * @author Huw Jones
 * @since 07/11/2015
 */
public class Protocol {

    private ObjectInputStream _input;
    private ObjectOutputStream _output;

    public Protocol(ObjectInputStream input, ObjectOutputStream output) {
        this._input = input;
        this._output = output;
    }

    public void connect() throws Exception {
        clientConnect();
    }

    public void connect(ChatServer server) throws Exception {
        serverConnect(server);
    }

    private void clientConnect() throws Exception {
        try {
            Frame hello = this.readFrame();
            if (hello.getType() != Frame.Type.HELLO) {
                throw new InvalidProtocolException("ChatServer protocol not understood.");
            }

        } catch (Exception ex) {
            throw new InvalidProtocolException("ChatServer protocol not understood.");
        }

        this._output.writeObject(new Frame(Frame.Type.HELLO, "Hello ChatServer."));

        try {
            Frame motd = this.readFrame();
            if (motd.getType() != Frame.Type.MOTD) {
                throw new InvalidProtocolException("ChatServer protocol not understood.");
            }else{
                System.out.println(motd.getObject().toString());
            }

        } catch (Exception ex) {
            throw new InvalidProtocolException("ChatServer protocol not understood.");
        }

        this._output.writeObject(new Frame(Frame.Type.OK, 1));
    }

    private void serverConnect(ChatServer server) throws Exception {
        this._output.writeObject(new Frame(Frame.Type.HELLO, "Hello ChatClient."));

        try {
            Frame hello = this.readFrame();
            if (!hello.isType(Frame.Type.HELLO)) {
                throw new InvalidProtocolException("ChatClient protocol not understood.");
            }
        } catch (Exception ex) {
            throw new InvalidProtocolException("ChatClient protocol not understood.");
        }

        this._output.writeObject(new Frame(Frame.Type.MOTD, server.getMOTD()));

        try {
            Frame motdOK = this.readFrame();
            if (!motdOK.isType(Frame.Type.OK)) {
                throw new InvalidProtocolException("ChatClient protocol not understood.");
            }
        } catch (Exception ex) {
            throw new InvalidProtocolException("ChatClient protocol not understood.");
        }

        this._output.writeObject(new Frame(Frame.Type.LOBBY_CHANGE, 1));
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
