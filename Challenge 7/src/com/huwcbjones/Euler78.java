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

    public static void main(String args[]) {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        if (argList.contains("-h") || argList.contains("--help")) {
            Euler78.help();
        } else if (argList.contains("--version")) {
            Euler78.version();
        } else if (argList.contains("--stupid-mode")) {
            Solver solver = new Solver(argList.contains("-v"));
            solver.runStupidMode();
        } else if (argList.contains("-a") || argList.contains("--approx")) {
            Solver solver = new Solver(argList.contains("-v"));
            solver.runApprox(getNumber(argList));
        } else if (argList.contains("-p") || argList.contains("--penta")) {
            Solver solver = new Solver(argList.contains("-v"));
            solver.runPenta(getNumber(argList));
        } else {
            Solver solver = new Solver(argList.contains("-v"));
            solver.run(getNumber(argList));
        }
    }

    private static void help() {
        System.out.println("Usage: Euler78 [OPTION]...");
        System.out.println("Java chat server.\n");
        System.out.println("Arguments:");
        System.out.println("  -a, --approx\t\tRuns using approximation algorithm.");
        System.out.println("  -h, --help\t\tPrints this help message.");
        System.out.println("  -n, --number\t\tNumber of times to run tests.");
        System.out.println("  -p, --penta\t\tRuns using pentagonal calculations.");
        System.out.println("      --stupid-mode\tRuns stupidly slowly, and east all your RAM! xD");
        System.out.println("  -v\t\t\tVerbose mode.");
        System.out.println("      --version\t\tPrints version.");
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
            if (intStr.matches("[\\d]+")) {
                number = Integer.parseInt(intStr);
            }
        }
        return number;
    }
}
