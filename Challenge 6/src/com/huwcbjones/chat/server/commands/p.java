package com.huwcbjones.chat.server.commands;

import com.huwcbjones.chat.core.Client;
import com.huwcbjones.chat.core.Frame;
import com.huwcbjones.chat.core.Log;
import com.huwcbjones.chat.core.Message;
import com.huwcbjones.chat.server.ChatServer;
import com.huwcbjones.chat.server.ClientThread;

import java.util.ArrayList;

/**
 * Private message a user
 *
 * @author Huw Jones
 * @since 11/11/2015
 */
public class p implements Command {

    private ArrayList<String> _args;

    @Override
    public String getHelp() {
        StringBuilder helpTxt = new StringBuilder();
        helpTxt.append("p: Private (p) messages a user\n");
        helpTxt.append("USAGE: !p [USER] [MSG]\n");
        helpTxt.append("PARAMS:\n");
        helpTxt.append(" - USER\t\tNickname of user you'd like to message.\n");
        helpTxt.append(" - MSG\t\tMessage you'd like to message.\n");
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
        ArrayList<String> messageContents = new ArrayList<>(_args);
        messageContents.remove(0);

        // If Java had implode(Array, String), I'd be sorted and living the high life.
        // But it does not :( Java 0 : PHP 1
        StringBuilder msg = new StringBuilder();
        for(int i = 0; i < messageContents.size(); i++){
            msg.append(messageContents.get(i));
            if(i != messageContents.size() - 1){
                msg.append(' ');
            }
        }
        try {
            Client target = server.getClient(_args.get(0));

            Message message = new Message(client.getClientID(), 0, msg.toString());
            message.setUser(client.getClient().getNickname() + " to " + target.getNickname());
            server.write(target.getClientID(), new Frame(Frame.Type.P_MESSAGE, message));
            server.write(client.getClientID(), new Frame(Frame.Type.P_MESSAGE, message));
            Log.Console(Log.Level.INFO, "P_Message({from:" + client.getClient().getNickname() + "}, {to:"
                    + target.getNickname() + "}, {msg:"
                    + message.getMessage() + "})");
        } catch (Exception ex){
            Message message = new Message(0, 0, "p Error: client \""+ _args.get(0)+"\" was not found!");
            message.setUser(server.getClient(0).getNickname());
            client.write(new Frame(Frame.Type.P_MESSAGE, message));
        }

    }
}
