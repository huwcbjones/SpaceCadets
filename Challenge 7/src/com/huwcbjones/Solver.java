package com.huwcbjones;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Euler 78 Solver
 *
 * @author Huw Jones
 * @since 12/11/2015
 */
public class Solver implements Runnable {

    private boolean print = false;
    private final double sqrt3x4 = Math.sqrt(3) * 4;
    private final double two3rds = 2d / 3;
    private HashMap<Integer, HashSet<ArrayList<Integer>>> _counts = new HashMap<>();

    private HashMap<Long, BigInteger> _pValues = new HashMap<>();
    private HashMap<Long, Long> _pentaPValues = new HashMap<>();

    private HashMap<Integer, Long> startTimes = new HashMap<>();
    private HashMap<Integer, Long> endTimes = new HashMap<>();

    private int number;
    private long[] _pModCache;
    private int[] _pentagonalNumbers;

    public Solver() {
        this(false);
    }

    public Solver(boolean print){
        this.print = print;
    }

    public void run(int numberOfTimes) {
        for (int i = 1; i <= numberOfTimes; i++) {
            _pValues = new HashMap<>();
            System.out.println("Run " + i + "...");

            this.startTimes.put(i, System.nanoTime());
            this._run();
            this.endTimes.put(i, System.nanoTime());
        }

        // Print run times/calculate average
        long t = 0;
        System.out.println("| RUN # | TIME TAKEN (s) |");
        for (int i = 1; i <= numberOfTimes; i++) {
            long s, f, d;
            s = this.startTimes.get(i);
            f = this.endTimes.get(i);
            d = f - s;
            t += d;
            System.out.print("| " + String.format("%5s", i) + " ");
            System.out.println("| " + String.format("%14s", new Double(d / 1000000000).longValue()) + " |");
        }
        if (numberOfTimes != 1) {
            long a = t / numberOfTimes;
            System.out.print("|   AVG ");
            System.out.println("| " + String.format("%14s", new Double(a / 1000000000).longValue()) + " |");
        }
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

    private void _run() {
        BigInteger pValue;
        long i = 0;

        // Put p(0) = 1, by convention
        this._pValues.put(0L, BigInteger.valueOf(1));

        if(print){
            do {
                i++;
                System.out.print("Calculating: " + i);
                pValue = _calculatePValue(i);
                System.out.println(": " + pValue);
                _pValues.put(i, pValue);
            } while (!_checkExitConditions(pValue));
        } else{
            do {
                i++;
                pValue = _calculatePValue(i);
                _pValues.put(i, pValue);
            } while (!_checkExitConditions(pValue));
        }

        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }

    private boolean _checkExitConditions(BigInteger value) {
        // Return true if value % 1,000,000 = 0
        BigInteger modResult = value.mod(BigInteger.valueOf(1000000));
        return modResult.compareTo(BigInteger.valueOf(0)) == 0;
    }

    private BigInteger _calculatePValue(long n) {

        BigInteger sign, pValue = BigInteger.ZERO;
        long aIndex, bIndex;

        for (long k = n; k >= 0; k--) {
            sign = ( k % 4 > 1 ) ? BigInteger.valueOf(-1) : BigInteger.ONE;

            aIndex = new Double(n - ( ( k * ( 3 * k - 1 ) ) / 2 )).longValue();
            bIndex = new Double(n - ( ( k * ( 3 * k + 1 ) ) / 2 )).longValue();

            pValue = pValue.add(sign.multiply(this.getPValue(aIndex).add(this.getPValue(bIndex))));
        }
    }
    @Override
    public void run() {

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
<<<<<<< HEAD
        BigInteger pValue;
        if(print){
            do {
                i++;
                System.out.print("Calculating: " + i);
                pValue = getPApprox(i);
                System.out.println(": " + pValue);
              } while (!_checkExitConditions(pValue));
        } else{
            do {
                i++;
                pValue = getPApprox(i);
            } while (!_checkExitConditions(pValue));
        }
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }
=======
        long pValue;
        this._pModCache[0] = 1;
        do {

            i++;
            System.out.print("Calculating: " + i);
            pValue = 0;
>>>>>>> rev2

            // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

            for (int k = i; k >= 0; k--) {
                pValue += ((k % 4 > 1) ? -1 : 1) * getPPenta(i - _pentagonalNumbers[k]);
                pValue %= 1000000;
            }

<<<<<<< HEAD
    private void _runPenta() {
        long i = 0;
        long pValue;
        this._pentaPValues.put(0L, 1L);
        if(print){
            do {
                i++;
                System.out.print("Calculating: " + i);
                pValue = _calculatePentaPValue(i);
                System.out.println(": " + pValue);
                _pentaPValues.put(i, pValue);
            } while (pValue != 0);
        } else{
            do {
                i++;
                pValue = _calculatePentaPValue(i);
                _pentaPValues.put(i, pValue);
            } while (pValue != 0);
        }
=======
            this._pModCache[i] = pValue;
            System.out.println(": " + pValue);

        } while (pValue != 0);
>>>>>>> rev2
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
