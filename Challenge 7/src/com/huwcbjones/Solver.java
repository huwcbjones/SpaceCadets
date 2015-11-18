package com.huwcbjones;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Euler 78 Solver
 *
 * @author Huw Jones
 * @since 12/11/2015
 */
public class Solver implements Runnable {

    private int number;
    private boolean print;
    private long[] _pModCache;
    private int[] _pentagonalNumbers;

    public Solver() {
        this(false);
    }

    public Solver(boolean print) {
        this(print, 1);
    }

    public Solver(boolean print, int number) {
        this.number = number;
        this.print = print;
        _pModCache = new long[55375];
        _pentagonalNumbers = new int[55375];

        for (int k = 0; k < 55375; k++) {
            int n = (k % 2 == 0) ? k / 2 + 1 : -(k / 2 + 1);
            _pentagonalNumbers[k] = Double.valueOf(n * (3 * n - 1) / 2).intValue();
        }
    }

    @Override
    public void run() {
        System.out.println("Run " + number + "...");
        if(print){
            _run();
        } else{
            _runNoPrint();
        }
    }

    private void _run() {
        int i = 0;
        long pValue;
        this._pModCache[0] = 1;
        do {

            i++;
            System.out.print("Calculating: " + i);
            pValue = 0;

            // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers
            // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

            for (int k = i; k >= 0; k--) {
                pValue += ((k % 4 > 1) ? -1 : 1) * getPPenta(i - _pentagonalNumbers[k]);
                pValue %= 1000000;
            }

            this._pModCache[i] = pValue;
            System.out.println(": " + pValue);

        } while (pValue != 0);
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }

    private void _runNoPrint(){
        int i = 0;
        long pValue;
        this._pModCache[0] = 1;
        do {

            i++;
            pValue = 0;

            // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers
            // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

            for (int k = i; k >= 0; k--) {
                pValue += ((k % 4 > 1) ? -1 : 1) * getPPenta(i - _pentagonalNumbers[k]);
                pValue %= 1000000;
            }

            this._pModCache[i] = pValue;

        } while (pValue != 0);
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }

    private long getPPenta(int n) {
        if(n < 0 || n > 55375) return 0;
        return _pModCache[n];
    }

}
