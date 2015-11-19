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

        // Create arrays, we know the max is 55375, so we'll make the array 55375 long
        _pModCache = new long[55375];
        _pentagonalNumbers = new int[55375];

        // Loop through 0 to 55375 to calculate generalised pentagonal numbers
        for (int k = 0; k < 55375; k++) {
            // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers

            // This produces
            // 0, 1, -1, 2, -2, 3, -3, etc
            int n = (k % 2 == 0) ? k / 2 + 1 : -(k / 2 + 1);

            // This then produces generalised pentagonal numbers
            // 0, 1, 2, 5, 7, 12, 15, 22, etc
            _pentagonalNumbers[k] = Double.valueOf(n * (3 * n - 1) / 2).intValue();
        }
    }

    @Override
    public void run() {
        System.out.println("Run " + number + "...");

        // Quicker to have a method that prints, and a method that doesn't rather than checking whether we should
        // print on before every print statement
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
        int n = 0;
        long pValue;

        // p(0) = 1, by convention
        this._pModCache[0] = 1;
        do {

            n++;
            pValue = 0;

            // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

            // The generating formula is:
            // p(n) = p(n - 1) + p(n - 2) - p(n - 5) - p(n - 7) + ...
            // Where 1, 2, 5, 7 are the generalised pentagonal numbers

            // Also found that
            // for (int k = i; k >= 0; k--)
            // was quicker than
            // for (int k = 0; k <= i; k++)
            // Don't know why...
            // If you know why, add the explanation below, and send me a pull request
            //
            //

            for (int k = n; k >= 0; k--) {

                // To get + + - - + + - -, we use
                // (k % 4 > 1) ? -1 : 1 to get sign
                // Then we get p(
                pValue += ((k % 4 > 1) ? -1 : 1) * getPPenta(n - _pentagonalNumbers[k]);

                // We only need to store last 6 digits, so modulus now
                // Also saves having to use BigInt or longs
                pValue %= 1000000;
            }

            this._pModCache[n] = pValue;

        } while (pValue != 0);
        System.out.println("n = " + n + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + n + ") % 1,000,000 = " + pValue);
    }

    private long getPPenta(int n) {
        // By convention, any non natural number's (negative integer) p value is 0
        // So return 0 for any out of bounds
        if(n < 0 || n > 55375) return 0;
        return _pModCache[n];
    }

}
