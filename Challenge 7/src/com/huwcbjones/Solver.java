package com.huwcbjones;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Euler 78 Solver
 *
 * @author Huw Jones
 * @since 12/11/2015
 */
public class Solver {

    private boolean print = false;
    private final double sqrt3x4 = Math.sqrt(3) * 4;
    private final double two3rds = 2d / 3;
    private HashMap<Integer, HashSet<ArrayList<Integer>>> _counts = new HashMap<>();

    private HashMap<Long, BigInteger> _pValues = new HashMap<>();
    private HashMap<Long, Long> _pentaPValues = new HashMap<>();

    private HashMap<Integer, Long> startTimes = new HashMap<>();
    private HashMap<Integer, Long> endTimes = new HashMap<>();

    public Solver() {

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

    public void runPenta(int numberOfTimes) {
        for (int i = 1; i <= numberOfTimes; i++) {
            System.out.println("Run " + i + "...");

            this.startTimes.put(i, System.nanoTime());
            this._runPenta();
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
            System.out.println("| " + String.format("%14s", new Double(d / 1000000000).longValue()) + " |");
        }
        if (numberOfTimes != 1) {
            long a = t / numberOfTimes;
            System.out.print("|   AVG ");
            System.out.println("| " + String.format("%14s", new Double(a / 1000000000).longValue()) + " |");
        }
    }

    public void runApprox(int numberOfTimes) {
        for (int i = 1; i <= numberOfTimes; i++) {
            System.out.println("Run " + i + "...");

            this.startTimes.put(i, System.nanoTime());
            this._runApprox();
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
            System.out.println("| " + String.format("%14s", new Double(d / 1000000000).longValue()) + " |");
        }
        if (numberOfTimes != 1) {
            long a = t / numberOfTimes;
            System.out.print("|   AVG ");
            System.out.println("| " + String.format("%14s", new Double(a / 1000000000).longValue()) + " |");
        }
    }

    private void _run() {
        BigInteger pValue;
        long i = 0;
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
        BigInteger modResult = value.mod(BigInteger.valueOf(1000000));
        return modResult.compareTo(BigInteger.valueOf(0)) == 0;
    }

    private BigInteger _calculatePValue(long n) {

        BigInteger sign, pValue = BigInteger.ZERO;
        long aIndex, bIndex;

        for (long k = n; k >= 0; k--) {
            sign = (k % 4 > 1) ? BigInteger.valueOf(-1) : BigInteger.ONE;

            aIndex = new Double(n - ((k * (3 * k - 1)) / 2)).longValue();
            bIndex = new Double(n - ((k * (3 * k + 1)) / 2)).longValue();

            pValue = pValue.add(sign.multiply(this.getPValue(aIndex).add(this.getPValue(bIndex))));
        }

        return pValue;
    }

    private BigInteger getPValue(long n) {
        if (n >= 0) {
            if (this._pValues.containsKey(n)) {
                return this._pValues.get(n);
            }
        }
        return BigInteger.valueOf(0);
    }

    private void _runApprox() {
        int i = 0;
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

    private BigInteger getPApprox(long n) {
        Double frac = n * this.sqrt3x4;
        Double power = Math.PI * Math.sqrt(n * this.two3rds);
        BigDecimal result = BigDecimal.valueOf(Math.E);
        result = result.pow(power.intValue());
        result = result.divide(BigDecimal.valueOf(frac), BigDecimal.ROUND_HALF_UP);

        return result.toBigInteger();
    }

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
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }


    private long _calculatePentaPValue(long n) {

        long pentaIndex, sign, pValue = 0;

        // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers
        // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

        for (long k = n; k >= 0; k--) {
            pentaIndex = (k % 2 == 0) ? k / 2 + 1 : -(k / 2 + 1);
            sign = (k % 4 > 1) ? -1 : 1;
            pValue += sign * getPPenta(n - getPentagonal(pentaIndex));
            pValue %= 1000000;
        }

        return pValue;
    }

    private long getPPenta(long n) {
        if (n >= 0) {
            if (this._pentaPValues.containsKey(n)) {
                return this._pentaPValues.get(n);
            }
        }
        return 0;
    }

    public void runStupidMode() {
        long startTime = System.nanoTime();
        this._runStupidMode();
        long endTime = System.nanoTime();
        System.out.println("Took " + (endTime - startTime) + "ns");
    }

    private void _runStupidMode() {
        HashSet<ArrayList<Integer>> set;
        int i = 1;
        do {
            // System.out.print("Calculating: " + i);
            set = countN(i);
            //System.out.print(": " + set.size() + "\n");
            this._counts.put(i, set);
            i++;
        } while (set.size() % 1000000 != 0);

        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
    }

    private HashSet<ArrayList<Integer>> countN(int n) {
        HashSet<ArrayList<Integer>> results = new HashSet<>();

        for (int i = n; i != 0; i--) {
            // If _counts contains n, add already calculated sets to set
            if (_counts.containsKey(i)) {
                for (ArrayList<Integer> intArray : this.getSet(i, n)) {
                    Collections.sort(intArray);
                    results.add(intArray);
                }
            } else {
                ArrayList<Integer> newArrayList = new ArrayList<>();
                newArrayList.add(n);
                results.add(newArrayList);
            }
        }

        return results;
    }

    private HashSet<ArrayList<Integer>> getSet(int i, int target) {
        HashSet<ArrayList<Integer>> results = new HashSet<>();

        // Loop through set
        for (ArrayList<Integer> sub : _counts.get(i)) {

            ArrayList<Integer> intSet = new ArrayList<>(sub);

            int sum = 0;

            for (int num : intSet) {
                sum += num;
            }

            if (sum != target) {
                intSet.add(target - sum);
            } else {
                intSet.add(1);
                // Add new set of ints to results

            }
            results.add(intSet);
        }
        return results;
    }

    private long getPentagonal(long n) {
        return Double.valueOf(n * (3 * n - 1) / 2).longValue();
    }
}
