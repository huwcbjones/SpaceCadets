package com.huwcbjones.chat;

import com.huwcbjones.chat.server.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

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
                chatd.run(_getPortNumber(argList));
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void help(){
        System.out.println("Usage: chatd [OPTION]...");
        System.out.println("Java chat server.\n");
        System.out.println("Arguments:");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -p, --port\t\tSpecifies port to listen on, otherwise listens on 60666.");
        System.out.println("  -v, --version\t\tPrints version.");
    }

    public static void version() {
        System.out.println("chatd 0.1");
        System.out.println("Written by Huw Jones");
    }

    public static void run(int port)
    {
        Server server = new Server(port);
        server.run();
    }

    private static int _getPortNumber(ArrayList<String> argList) throws Exception {
        int port = 60666;
        if (argList.contains("-p") || argList.contains("--port")) {
            int index = (argList.contains("-p")) ? argList.indexOf("-p") : argList.indexOf("--port");
            String intStr = argList.get(index + 1);
            if (!intStr.matches("\\d")) {
                throw new Exception("Invalid port number!");
            }
            port = Integer.parseInt(intStr);
        }
        return port;
    }

}
