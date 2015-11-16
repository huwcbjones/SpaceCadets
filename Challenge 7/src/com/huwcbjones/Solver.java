package com.huwcbjones;

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

    private HashMap<Integer, HashSet<ArrayList<Integer>>> _counts = new HashMap<>();

    private HashMap<Long, BigInteger> _pValues = new HashMap<>();

    private HashMap<Integer, Long> startTimes = new HashMap<>();
    private HashMap<Integer, Long> endTimes = new HashMap<>();

    private long startTime;
    private long endTime;

    public Solver() {

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
            long a = t/numberOfTimes;
            System.out.print("|   AVG ");
            System.out.println("| " + String.format("%14s", new Double(a / 1000000000).longValue()) + " |");
        }
    }

    private void _run() {
        BigInteger pValue;
        long i = 0;
        this._pValues.put(0L, BigInteger.valueOf(1));

        do {
            i++;
            //System.out.print("Calculating: " + i);
            pValue = _calculatePValue(i);
            //System.out.println(": " + pValue);
            _pValues.put(i, pValue);
        } while (!_checkExitConditions(pValue));
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") = " + pValue);
    }

    private boolean _checkExitConditions(BigInteger value) {
        BigInteger modResult = value.mod(BigInteger.valueOf(1000000));
        return modResult.compareTo(BigInteger.valueOf(0)) == 0;
    }

    private BigInteger _calculatePValue(long n) {

        BigInteger pValueD, pValue = BigInteger.valueOf(0);
        long aIndex, bIndex;

        for (long k = n; k >= 0; k--) {
            pValueD = BigInteger.valueOf(((Double) Math.pow(-1, k + 1)).longValue());

            aIndex = new Double(n - ((k * (3 * k - 1)) / 2)).longValue();
            bIndex = new Double(n - ((k * (3 * k + 1)) / 2)).longValue();

            pValue = pValue.add(pValueD.multiply(this.getPValue(aIndex).add(this.getPValue(bIndex))));
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

    public void runStupidMode() {
        this.startTime = System.nanoTime();
        this._runStupidMode();
        this.endTime = System.nanoTime();
        System.out.println("Took " + (this.endTime - this.startTime) + "ns");
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

    private int getPentagonal(int n) {
        return ((Double) ((3 * Math.pow(n, 2) - 2) / 2)).intValue();
    }
}
