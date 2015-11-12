package com.huwcbjones;

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

    private long startTime;
    private long endTime;

    public Solver() {

    }

    public void run(int numberOfTimes) {
        this.startTime = System.nanoTime();
        for (int i = 0; i < numberOfTimes; i++) {
            this._runStupidMode();
        }
        this.endTime = System.nanoTime();
        System.out.println("Took " + (this.endTime - this.startTime) + "ns");
    }

    private void _runStupidMode() {
        HashSet<ArrayList<Integer>> set;
        int i = 1;
        do{
            System.out.print("Calculating: " + i);
            set = countN(i);
            System.out.print(": " + set.size() + "\n");
            this._counts.put(i, set);
            i++;
        } while(set.size() % 1000000 != 0);
        System.out.println("end");
    }

    private HashSet<ArrayList<Integer>> countN(int n) {
        HashSet<ArrayList<Integer>> results = new HashSet<>();

        for (int i = n; i != 0; i--) {
            // If _counts contains n, add already calculated sets to set
            if (_counts.containsKey(i)) {
                for(ArrayList<Integer> intArray : this.getSet(i, n)){
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

}
