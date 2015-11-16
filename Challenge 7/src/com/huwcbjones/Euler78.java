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
        } else if (argList.contains("--stupid-mode")) {
            Solver solver = new Solver();
            solver.runStupidMode();
        } else {
            Solver solver = new Solver();
            solver.run(10);
        }
    }

    private static void help(){
        System.out.println("Usage: Euler78 [OPTION]...");
        System.out.println("Java chat server.\n");
        System.out.println("Arguments:");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("      --stupid-mode\tRuns stupidly slowly, and east all your RAM! xD");
        System.out.println("  -v, --version\t\tPrints version.");
    }

    private static void version() {
        System.out.println("Euler78 0.1");
        System.out.println("Written (poorly) by Huw Jones");
    }
}
