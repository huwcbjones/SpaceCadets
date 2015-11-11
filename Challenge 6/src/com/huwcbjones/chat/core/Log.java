package com.huwcbjones.chat.core;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class Log {

    public static enum Level {
        MSG,
        INFO,
        WARN,
        FATAL,
        NONE
    }

    public static void Console(Level level, String message){
        switch(level){
            case FATAL:
                System.err.print("[ ERR  ] ");
                System.err.println(message);
                break;
            case WARN:
                System.err.print("[ WARN ] ");
                System.err.println(message);
                break;
            case INFO:
                System.err.print("[ INFO ] ");
                System.err.println(message);
                break;
            case MSG:
                System.out.println(message);
                break;
            case NONE:
                break;
        }
    }
}
