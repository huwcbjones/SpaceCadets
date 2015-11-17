package com.huwcbjones;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Euler 78 Solver
 *
 * @author Huw Jones
 * @since 12/11/2015
 */
public class Solver implements Runnable {

    private int number;
    private boolean print;
    private HashMap<Integer, Integer> _pentagonalNumbers;
    private HashMap<Integer, Long> _pModCache;

    public Solver() {
        this(false);
    }

    public Solver(boolean print) {
        this(print, 1);
    }

    public Solver(boolean print, int number) {
        this.number = number;
        this.print = print;
        _pentagonalNumbers = new HashMap<>();
        _pModCache = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("Run " + number + "...");
        _calculateGeneralisedPentagonals();
        if(print){
            _run();
        } else{
            _runNoPrint();
        }
    }

    private void _run() {
        int i = 0;
        long pValue;
        this._pModCache.put(0, 1L);
        do {

            i++;
            System.out.print("Calculating: " + i);
            pValue = 0;

            // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers
            // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

            for (int k = i; k >= 0; k--) {
                pValue += ((k % 4 > 1) ? -1 : 1) * getPPenta(i - _pentagonalNumbers.get(k));
                pValue %= 1000000;
            }

            this._pModCache.put(i, pValue);
            System.out.println(": " + pValue);

        } while (pValue != 0);
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }

    private void _runNoPrint(){
        int i = 0;
        long pValue;
        this._pModCache.put(0, 1L);
        do {

            i++;
            pValue = 0;

            // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers
            // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

            for (int k = 0; k <= i; k++) {
                pValue += ((k % 4 > 1) ? -1 : 1) * getPPenta(i - _pentagonalNumbers.get(k));
                pValue %= 1000000;
            }

            this._pModCache.put(i, pValue);

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
