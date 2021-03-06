package com.huwcbjones.chat.core;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 07/11/2015
 */
public class cli {

    public static String getNickname(ArrayList<String> argList) throws Exception {
        if (argList.contains("-n") || argList.contains("--name")) {
            int index = (argList.contains("-n")) ? argList.indexOf("-n") : argList.indexOf("--name");
            return argList.get(index + 1);
        }
        return null;
    }

    public static int getPortNumber(ArrayList<String> argList) throws Exception {
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

    public static URI getServer(ArrayList<String> argList) throws Exception {
        if(argList.size() == 0){
            throw new Exception("No server specified.");
        }
        return new URI("chat://" + argList.get(argList.size() - 1));
    }
}
