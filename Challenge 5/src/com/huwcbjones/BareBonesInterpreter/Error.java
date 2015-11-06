package com.huwcbjones.BareBonesInterpreter;

/**
 * Created by Huw on 24/10/2015.
 */
public class Error {
    public static void Fatal(String message){
        System.out.println("FATAL: " + message);
        System.exit(-1);
    }
    public static void Fatal(String message, Exception stacktrace){
        System.out.println("FATAL: " + message);
        stacktrace.printStackTrace();
        System.exit(-1);
    }
    public static void Fatal(String message, Lexer lexer){
        System.out.println("FATAL: " + message + " On line " + lexer.getLineNumber() + ", pos " + lexer.getLinePosition());
        System.exit(-1);
    }
    public static void Fatal(String message, Lexer lexer, Exception stackTrace){
        System.out.println("FATAL: " + message + " On line " + lexer.getLineNumber() + ", pos " + lexer.getLinePosition());
        stackTrace.printStackTrace();
        System.exit(-1);
    }

    public static void Warning(String message, Lexer lexer){
        System.out.println("WARN: " + message + " On line " + lexer.getLineNumber() + ", pos " + lexer.getLinePosition());
    }

    public static void Information(String message, Lexer lexer){
        System.out.println("INFO: " + message + " On line " + lexer.getLineNumber() + ", pos " + lexer.getLinePosition());
    }

}
