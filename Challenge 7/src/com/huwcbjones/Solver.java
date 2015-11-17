package com.huwcbjones;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Euler 78 Solver
 *
 * @author Huw Jones
 * @since 12/11/2015
 */
public class Solver {

    private boolean print;
    private HashMap<Integer, Integer> _pentagonalNumbers;
    private HashMap<Integer, Long> _pModCache;

    private HashMap<Integer, Long> startTimes;
    private HashMap<Integer, Long> endTimes;

    public Solver() {
        this(false);
    }

    public Solver(boolean print) {
        this.print = print;
        _pentagonalNumbers = new HashMap<>();
        _pModCache = new HashMap<>();
        startTimes = new HashMap<>();
        endTimes = new HashMap<>();
    }

    public void run(int numberOfTimes) {
        _calculateGeneralisedPentagonals();
        for (int i = 1; i <= numberOfTimes; i++) {
            System.out.println("Run " + i + "...");

            this.startTimes.put(i, System.nanoTime());
            this._run();
            this.endTimes.put(i, System.nanoTime());
        }

        long t = 0;
        System.out.println("| RUN # | TIME TAKEN (s) |");
        for (int i = 1; i <= numberOfTimes; i++) {
            long s, f, d;
            s = this.startTimes.get(i);
            f = this.endTimes.get(i);
            d = f - s;
            t += d;
            System.out.print("| " + String.format("%5s", i) + " ");
            System.out.println("| " + String.format("%14s", d / 1000000000d) + " |");
        }
        if (numberOfTimes != 1) {
            long a = t / numberOfTimes;
            System.out.print("|   AVG ");
            System.out.println("| " + String.format("%14s", a / 1000000000d) + " |");
        }
    }

    private void _run() {
        int i = 0;
        long pValue;
        this._pModCache.put(0, 1L);
        do {

            i++;
            if (print) System.out.print("Calculating: " + i);
            pValue = 0;

            // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers
            // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

            for (int k = i; k >= 0; k--) {
                pValue += ((k % 4 > 1) ? -1 : 1) * getPPenta(i - _pentagonalNumbers.get(k));
                pValue %= 1000000;
            }

            this._pModCache.put(i, pValue);
            if (print) System.out.println(": " + pValue);

        } while (pValue != 0);
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }

    private void _calculateGeneralisedPentagonals(){
        for (int k = 0; k < 60000; k++) {
            int n = (k % 2 == 0) ? k / 2 + 1 : -(k / 2 + 1);
            _pentagonalNumbers.put(k, Double.valueOf(n * (3 * n - 1) / 2).intValue());
        }
    }
    private long getPPenta(int n) {
        if (n >= 0) {
            return this._pModCache.containsKey(n) ? _pModCache.get(n) : 0;
        }
        return 0;
    }
}
