package com.huwcbjones.chat.core;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class Log {

    public enum Level {
        MSG,
        INFO,
        WARN,
        FATAL,
        NONE
    }

    public static void Console(Level level, String message){
        switch(level){
            case FATAL:
                System.out.print("[ ERR  ] ");
                break;
            case WARN:
                System.out.print("[ WARN ] ");
                break;
            case INFO:
                System.out.print("[ INFO ] ");
                break;
            case MSG:
                System.out.print("[ MSG  ] ");
                break;
            case NONE:
                break;
        }
        System.out.println(message);
    }
}
