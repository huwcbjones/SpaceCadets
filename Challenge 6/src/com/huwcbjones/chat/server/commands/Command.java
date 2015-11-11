package com.huwcbjones.chat.server.commands;

import com.huwcbjones.chat.core.Message;
import com.huwcbjones.chat.server.ChatServer;
import com.huwcbjones.chat.server.ClientThread;

import java.util.ArrayList;

/**
 * Command Interface
 *
 * @author Huw Jones
 * @since 10/11/2015
 */
public interface Command {

    String getHelp();

    void addArguments(ArrayList<String> arguments) throws Exception;

    void execute(ClientThread client, ChatServer server) throws Exception;
}
