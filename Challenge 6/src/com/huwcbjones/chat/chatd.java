package com.huwcbjones.chat;

import com.huwcbjones.chat.core.cli;
import com.huwcbjones.chat.server.Server;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Chat Server
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class chatd {

    public static void main(String args[]){
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        if (argList.contains("-h") || argList.contains("--help")) {
            chatd.help();
        } else if (argList.contains("-v") || argList.contains("--version")) {
            chatd.version();
        } else {
            try {
                chatd.run(cli.getPortNumber(argList));
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void help(){
        System.out.println("Usage: chatd [OPTION]...");
        System.out.println("Java chat server.\n");
        System.out.println("Arguments:");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -p, --port\t\tSpecifies port to listen on, otherwise listens on 60666.");
        System.out.println("  -v, --version\t\tPrints version.");
    }

    private static void version() {
        System.out.println("chatd 0.2");
        System.out.println("Written by Huw Jones");
    }

    private static void run(int port)
    {
        Server server = new Server(port);
        chatd.version();
        server.run();
    }


}
