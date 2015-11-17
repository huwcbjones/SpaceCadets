package com.huwcbjones;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Euler Challenge 78
 *
 * @author Huw Jones
 * @since 12/11/2015
 */
public class Euler78 {

    public static void main(String args[]){
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        if (argList.contains("-h") || argList.contains("--help")) {
            Euler78.help();
        } else if (argList.contains("-v") || argList.contains("--version")) {
            Euler78.version();
        } else{
            boolean print = (argList.contains("-p") || argList.contains("--print"));
            Solver solver = new Solver(print);
            solver.run(getNumber(argList));
        }
    }

    private static void help(){
        System.out.println("Usage: Euler78 [OPTION]...");
        System.out.println("Java chat server.\n");
        System.out.println("Arguments:");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -n, --number\t\tNumber of times to run tests.");
        System.out.println("  -p, --print\t\tPrints calculation information.");
        System.out.println("  -v, --version\t\tPrints version.");
    }

    private static void version() {
        System.out.println("Euler78 0.1");
        System.out.println("Written (poorly) by Huw Jones");
    }

    public static int getNumber(ArrayList<String> argList) {
        int number = 1;
        if (argList.contains("-n") || argList.contains("--number")) {
            int index = (argList.contains("-n")) ? argList.indexOf("-n") : argList.indexOf("--number");
            String intStr = argList.get(index + 1);
            if (!intStr.matches("\\d")) {
                return 1;
            }
            number = Integer.parseInt(intStr);
        }
        return number;
    }
}
