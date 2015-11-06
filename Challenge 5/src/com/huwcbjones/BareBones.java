package com.huwcbjones;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lexemes.Lexeme;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * BareBones Interpreter
 *
 * @author Huw Jones
 * @since 23/10/2015
 */
public class BareBones {
    public static void main(String[] args) {
        BareBones interpreter = new BareBones();

        ArrayList<String> argList = new ArrayList<String>(Arrays.asList(args));
        if (argList.contains("-h") || argList.contains("--help")) {
            interpreter.help();
        } else if (argList.contains("--list-cmd")) {
            interpreter.listCMD();
        } else if (argList.contains("-f") || argList.contains("--file")) {
            try {
                interpreter.fromFile(argList.get(1));
            } catch (Exception ex) {
                interpreter.fromFile("");
            }
        } else if (argList.contains("-c") || argList.contains("--cmd")) {
            interpreter.command(argList.get(1));
        } else if (argList.contains("-i") || argList.contains("--interactive")) {
            interpreter.run();
        } else if (argList.contains("-v") || argList.contains("--version")) {
            interpreter.version();
        } else {
            interpreter.help();
        }
    }

    private void fromFile(String file) {
        if (file.equals("")) {
            System.out.println("Could not open file. No file specified.");
            return;
        }
        File f = new File(file);
        if (!f.exists()) {
            System.out.println("Could not open file. File, \"" + file + "\", was not found.");
            return;
        }
        if (!f.isFile()) {
            System.out.println("Could not open file. File, \"" + file + "\", was invalid.");
            return;
        }
        try {
            byte[] encodedFile = Files.readAllBytes(Paths.get(file));
            Charset encoding = StandardCharsets.US_ASCII;
            String fileContents = new String(encodedFile, encoding);

            Interpreter interpreter = new Interpreter(fileContents);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void help() {
        System.out.println("Usage: BareBones [OPTION]...");
        System.out.println("BareBones interpreter that takes input from FILE.\n");
        System.out.println("Arguments:");
        System.out.println("  -c, --command [CMD]\tPrints help for a command, [CMD].");
        System.out.println("  -f, --file\t\tSpecifies file to interpret.");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -i, --interactive\tRuns in interactive mode.");
        System.out.println("      --list-cmd\tLists available commands.");
        System.out.println("  -v, --version\t\tPrints version.");
    }

    private void command(String cmd) {
        Interpreter interpreter = new Interpreter();
        try {
            Lexeme lex = (Lexeme) interpreter.getLexeme(cmd);
            this.version();
            System.out.println("================================");
            System.out.println("Command help for \"" + cmd + "\":");
            System.out.println("********************************");
            System.out.print(lex.getHelp());
            System.out.println("********************************");
        } catch (Exception ex) {
            this.version();
            System.out.println("Command help for \"" + cmd + "\" was not found.");
        }
    }

    private void listCMD() {
        Interpreter interpreter = new Interpreter();
        Iterator<String> iterator = interpreter.getLexemes().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    private void version() {
        System.out.println("BareBones 0.7");
        System.out.println("Written by Huw Jones");
    }

    private void run() {
        Console c = System.console();
        if (c == null) {
            System.out.println("Fatal error! Console not found.");
            System.exit(-1);
        }
        Interpreter interpreter = new Interpreter();
        while (true) {
            try {
                String input = c.readLine("barebones> ");
                interpreter.executeLine(input);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}