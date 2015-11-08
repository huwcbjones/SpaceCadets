package com.huwcbjones.chat.core;

/**
 * Base Class for Server/Client
 *
 * @author Huw Jones
 * @since 07/11/2015
 */
public abstract class base {

    public enum ErrorLevel {
        INFO,
        WARN,
        ERROR
    }

    public void LogMessage(ErrorLevel level, String message){
        switch(level){
            case ERROR:
                System.out.print("[ ERR  ] ");
                break;
            case WARN:
                System.out.print("[ WARN ] ");
                break;
            case INFO:
                System.out.print("[ INFO ] ");
                break;
        }
        System.out.println(message);
    }
}
