package com.huwcbjones.spiro;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.huwcbjones.helper.*;

/**
 * Created by hjone on 15/10/2015.
 */
public class Spirograph {

    private double _exRadius;
    private double _intRadius;
    private int _t = 0;
    private int _maxIt = 100000;
    private double _scale = 1;
    private Calculator _calc;

    private ConcurrentHashMap<Integer, Pair<Integer, Integer>> _points = new ConcurrentHashMap<Integer, Pair<Integer, Integer>>();
    private ConcurrentHashMap<Integer, Pair<Integer, Integer>> _currentSet = new ConcurrentHashMap<Integer, Pair<Integer, Integer>>();

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    public Spirograph(double exRadius, double intRadius) {
        this._exRadius = exRadius;
        this._intRadius = intRadius;
    }

    public Spirograph(double exRadius, double intRadius, int maxIt) {
        this._exRadius = exRadius;
        this._intRadius = intRadius;
        this._maxIt = maxIt;
    }

    public Spirograph(double exRadius, double intRadius, int maxIt, double scale) {
        this._exRadius = exRadius;
        this._intRadius = intRadius;
        this._maxIt = maxIt;
        this._scale = scale;
    }

    public void setScale(double scale) {
        this._scale = scale;
    }

    public void calculateX(int n) {
        _calc = new Calculator(n, _t);
        _calc.start();
        synchronized (_calc) {
            try {
                // Wait for calc thread to finish
                wait();
            } catch (Exception ex) {

            }
            synchronized (this) {
                // Notify threads waiting on us that we're finished
                notify();
            }
        }
        _t = _t + n;
    }

    public void stop(){
        _calc.quit();
    }

    public ConcurrentHashMap<Integer, Pair<Integer, Integer>> get_points() {
        return _points;
    }

    public ConcurrentHashMap<Integer, Pair<Integer, Integer>> get_currentSet() {
        return _currentSet;
    }

    public int get_maxIt() {
        return _maxIt;
    }


    public Pair<Integer, Integer> getPoint(int t) {
        return _points.get(t);
    }

    class Calculator extends Thread {

        private int n;
        private int s;
        private boolean _shouldStop = false;

        public Calculator(int n, int s) {
            this.setName("Spirograph.Calculator");
            this.n = n;
            this.s = s;
        }

        public void quit(){
            this._shouldStop = true;
        }

        public void run() {

            // Starting a new set, so clear old ones
            _currentSet.clear();

            //System.out.println("Calculating " + n + " values (from " + s + " to " + (s + n) + ")");
            for (int t = s; (t < s + n) && (t < _maxIt); t++) {
                if(this._shouldStop){
                    break;
                }
                _calcPair(t);
            }
            this._shouldStop = false;
            // Notify other threads waiting on us that we're done
            synchronized (this) {
                notify();
            }
        }
    }

    private void _calcPair(int t) {
        int x = (int)Math.floor(this._calcX(t / 500d));
        int y = (int)Math.floor(this._calcY(t / 500d));

        Pair<Integer, Integer> pair = new Pair<Integer, Integer>(x, y);
//        System.out.println("x: " + x + ", y: " + y);

        _points.put(t, pair);
        _currentSet.put(t, pair);
    }

    private double _calcX(double t) {
        double R = this._exRadius;
        double r = this._intRadius;
        return (R - r) * Math.cos(t) + r * Math.cos(((R - r) / r) * t);
    }

    private double _calcY(double t) {
        double R = this._exRadius;
        double r = this._intRadius;
        return (R - r) * Math.sin(t) - r * Math.sin(((R - r) / r) * t);
    }
}
