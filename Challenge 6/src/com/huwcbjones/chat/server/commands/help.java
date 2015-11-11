package com.huwcbjones.chat.server.commands;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.server.ChatServer;
import com.huwcbjones.chat.server.ClientThread;

import java.util.ArrayList;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 10/11/2015
 */
public class help implements Command {

    private ArrayList<String> _args;

    @Override
    public String getHelp() {
        StringBuilder helpTxt = new StringBuilder();
        helpTxt.append("help: Provides help with a command.\n");
        helpTxt.append("USAGE: !help [COMMAND]\n");
        helpTxt.append("PARAMS:\n");
        helpTxt.append(" - list\t\tLists commands available.\n");
        helpTxt.append(" - COMMAND\t\tCommand requiring help.\n");
        return helpTxt.toString();
    }

    @Override
    public void addArguments(ArrayList<String> arguments) {
        this._args = arguments;
    }

    @Override
    public void execute(ClientThread client, ChatServer server) {
        if(_args.size() == 0){
            client.write(new Frame(Frame.Type.HELP, this.getHelp()));
            return;
        }
        if(_args.get(0).equals("list")){

            return;
        }
        try {
            Command command = server.getCommand(_args.get(0));
            client.write(new Frame(Frame.Type.HELP, command.getHelp()));
        } catch (Exception ex){
            String help = "Command not found: " + _args.get(0) + "\n";
            client.write(new Frame(Frame.Type.HELP, help + this.getHelp()));
        }
    }
}
