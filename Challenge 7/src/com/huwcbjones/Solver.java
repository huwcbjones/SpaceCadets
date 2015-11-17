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
    private HashMap<Long, Long> _pentagonalNumbers;
    private HashMap<Long, Long> _pModCache;

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
        Pentagen pentagen = new Pentagen();
        pentagen.run();
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
        long i = 0;
        long pValue;
        this._pModCache.put(0L, 1L);
        do {
            i++;
            if (print) System.out.print("Calculating: " + i);
            pValue = _calculatePentaPValue(i);
            this._pModCache.put(i, pValue);
            if (print) System.out.println(": " + pValue);
        } while (pValue != 0);
        System.out.println("n = " + i + " is the least value for p(n) % 1,000,000 = 0.");
        System.out.println("p(" + i + ") % 1,000,000 = " + pValue);
    }

    private long _calculatePentaPValue(long n) {

        long sign, pValue = 0;

        // See https://en.wikipedia.org/wiki/Pentagonal_number for calculating pentagonal numbers
        // See https://en.wikipedia.org/wiki/Partition_(number_theory)#Generating_function for generating function

        for (long k = n; k >= 0; k--) {
            sign = (k % 4 > 1) ? -1 : 1;
            pValue += sign * getPPenta(n - getPentagonal(k));
            pValue %= 1000000;
        }

        return pValue;
    }

    private long getPPenta(long n) {
        if (n >= 0) {
            if (this._pModCache.containsKey(n)) {
                return this._pModCache.get(n);
            }
        }
        return 0;
    }

    private long getPentagonal(long n) {
        if (_pentagonalNumbers.containsKey(n)) {
            return _pentagonalNumbers.get(n);
        } else {
            long k = (n % 2 == 0) ? n / 2 + 1 : -(n / 2 + 1);
            return calcPentagonalNumber(k);
        }
    }

    public long calcPentagonalNumber(long n) {
        return Double.valueOf(n * (3 * n - 1) / 2).longValue();
    }

    class Pentagen extends Thread {
        public Pentagen() {
            this.setName("Pentagonal Number Generator Thread.");
        }

        public void run() {
            for (long k = 0; k < 60000; k++) {
                long n = (k % 2 == 0) ? k / 2 + 1 : -(k / 2 + 1);
                _pentagonalNumbers.put(k, calcPentagonalNumber(n));
            }
        }
    }
}
