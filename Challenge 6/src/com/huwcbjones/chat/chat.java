package com.huwcbjones.chat;

import com.huwcbjones.chat.client.ChatClient;
import com.huwcbjones.chat.client.GUI;
import com.huwcbjones.chat.core.cli;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Chat ChatClient
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class chat {

    public static void main(String args[]) {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        if (argList.contains("-h") || argList.contains("--help")) {
            chat.help();
        } else if (argList.contains("-v") || argList.contains("--version")) {
            chat.version();
        } else {
            try {
                URI url = cli.getServer(argList);
                String username = cli.getUsername(argList);
                String name = cli.getName(argList);
                if (argList.contains("-g")) {
                    chat.GUI(cli.getPortNumber(argList), url);
                    return;
                }
                if (username != null) {
                    if (name != null) {
                        chat.run(cli.getPortNumber(argList), url, username, name);
                    } else {
                        chat.run(cli.getPortNumber(argList), url, username, username);
                    }
                } else {
                    chat.run(cli.getPortNumber(argList), url);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    private static void help() {
        System.out.println("Usage: chat [OPTION]... SERVER");
        System.out.println("Java chat client.\n");
        System.out.println("Arguments:");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -g\t\t\tRun GUI.");
        System.out.println("  -n, --name\t\tName of user connecting.");
        System.out.println("  -p, --port\t\tSpecifies port to connect to, otherwise connects to 60666.");
        System.out.println("  -u, --username\tUsername of user connecting.");
        System.out.println("  -v, --version\t\tPrints version.");
    }

    private static void version() {
        System.out.println("chat 0.1");
        System.out.println("Written by Huw Jones");
    }

    private static void run(int port, URI uri, String username, String name) {
        ChatClient chatClient = new ChatClient(port, uri);
        chat.version();
        chatClient.setUsername(username);
        chatClient.setName(name);
        chatClient.run();
    }

    private static void GUI(int port, URI uri) {
        GUI chatClient = new GUI();
        chat.version();
        chatClient.run(port, uri);
    }

    private static void run(int port, URI uri) {
        ChatClient chatClient = new ChatClient(port, uri);
        chat.version();
        chatClient.run();
    }
}
