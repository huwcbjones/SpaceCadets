package com.huwcbjones.chat.server.commands;

import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.server.ChatServer;
import com.huwcbjones.chat.server.ClientThread;

import java.util.ArrayList;

/**
 * Client Command
 *
 * @author Huw Jones
 * @since 11/11/2015
 */
public class client implements Command {
    private ArrayList<String> _args;
    @Override
    public String getHelp() {
        StringBuilder helpTxt = new StringBuilder();
        helpTxt.append("client: Get's information about clients\n");
        helpTxt.append("USAGE: !client [ARG]\n");
        helpTxt.append("PARAMS:\n");
        helpTxt.append(" - ARG\t\tClient command.\n");
        helpTxt.append("ARGS:\n");
        helpTxt.append(" - list\t\tLists connected clients.\n");
        helpTxt.append(" - whois [CLIENT]\tGets information about CLIENT.\n");
        return helpTxt.toString();
    }

    @Override
    public void addArguments(ArrayList<String> arguments) throws Exception {
        this._args = arguments;
    }

    @Override
    public void execute(ClientThread client, ChatServer server) throws Exception {
        if(_args.size() == 0){
            client.write(new Frame(Frame.Type.HELP, this.getHelp()));
            return;
        }

        if(_args.get(0).equals("list")){

        }
    }
}
