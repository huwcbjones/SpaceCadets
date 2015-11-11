package com.huwcbjones.chat.server.commands;

import com.huwcbjones.chat.core.Message;

import java.util.ArrayList;

/**
 * Command Interface
 *
 * @author Huw Jones
 * @since 10/11/2015
 */
public interface Command {
    void getCommand();

    String getHelp();

    void addArguments(ArrayList<String> arguments);

    void execute();

    Message getResult();
}
