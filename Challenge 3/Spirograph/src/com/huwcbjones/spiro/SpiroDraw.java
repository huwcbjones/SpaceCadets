package com.huwcbjones.spiro;

import com.huwcbjones.helper.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Huw on 16/10/2015.
 */
public class SpiroDraw extends JApplet {

    private JPanel panel_window;
    private JSlider slider_outerRadius;
    private JSlider slider_innerRadius;
    private JLabel label_outerRadius;
    private JLabel label_innerRadius;
    private JLabel label_speed;
    private JSlider slider_speed;
    private JButton btn_draw;
    private JPanel panel_menu;
    private JPanel panel_spiro;
    private JSplitPane splitPane_container;

    private Spirograph spiro;
    private int _pixelsPerTick;
    private int FPS = (1000 / 60);

    private Dimension _dimension;
    private BufferedImage _imageBuffer;
    private Graphics2D _frameBuffer;

    private double _outerRadius;
    private double _innerRadius;
    private int _maxIts;

    private PixelPainter _painter;

    private boolean _isSpiroDrawing = false;
    private boolean _shouldStop = false;

    public SpiroDraw() {
        btn_draw.addActionListener(new btn_draw_Event());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SpiroDraw");
        frame.setContentPane(new SpiroDraw().panel_window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 748);
        frame.setVisible(true);
    }

    @Override
    public void init() {
        JFrame _frame = new JFrame("SpiroDraw");
        _frame.setContentPane(new SpiroDraw().panel_window);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(1366, 748);
        _frame.setVisible(true);

        //_init();
    }

    private void _init() {
        _updateValues();
        _initX2buff();
    }

    private void _initX2buff() {
        _imageBuffer = (BufferedImage) panel_spiro.createImage(_dimension.width, _dimension.height);
        _frameBuffer = (Graphics2D) _imageBuffer.getGraphics();
    }

    private void _updateValues() {
        this._dimension = panel_spiro.getSize();
        this._pixelsPerTick = _getPixelsPerTick();
        this._outerRadius = _getOuterRadius();
        this._innerRadius = _getInnerRadius();
        this._maxIts = 1000;
    }

    /**
     * @return Int: pixels painted per tick
     */
    private int _getPixelsPerTick() {
        return this.slider_speed.getValue();
        /*
        int ppt = 1;
        double power = this.slider_speed.getValue() / 10d;

        double result = Math.pow(10, (double) power);

        ppt = (int) Math.floor(result);

        return ppt;*/
    }

    /**
     * @return Double: outer spiro radius
     */
    private double _getOuterRadius() {
        int value = this.slider_outerRadius.getValue();
        return value / 10;
    }

    /**
     * @return Double: inner spiro radius
     */
    private double _getInnerRadius() {
        int value = this.slider_innerRadius.getValue();
        return value / 10;
    }

    private class btn_draw_Event implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (_isSpiroDrawing) {
                _shouldStop = true;
                synchronized (_painter) {
                    try {
                        wait();
                    } catch (Exception ex) {

                    }
                }
            }

            _drawSpiro();
        }
    }

    private void _drawSpiro() {
        this._init();
        this._updateValues();
        this.spiro = new Spirograph(this._outerRadius, this._innerRadius, this._maxIts);
        this._painter = new PixelPainter();
        this._painter.start();
    }

    class PixelPainter extends Thread {

        public void run() {
            this.setName("PixelPainter");
            System.out.println("Painting pixels...");
            _paintPixels();
        }

        private void _paintPixels() {

            int n;
            int i = 0;

            // Set a concurrent hashmap to allow other threads to alter
            ConcurrentHashMap<Integer, Pair<Integer, Integer>> points = new ConcurrentHashMap<Integer, Pair<Integer, Integer>>();
            System.out.println("PpT: " + _pixelsPerTick);
            // Loop over all t values
            for (n = 0; n < spiro.get_maxIt(); n = n + _pixelsPerTick) {
                _isSpiroDrawing = true;

                // Calculate this set of values
                spiro.calculateX(_pixelsPerTick);

                // Wait for calculate thread to finish
                synchronized (spiro) {
                    try {
                        wait();
                    } catch (Exception ex) {

                    }
                }

                // Clear hashmap and copy values into our one to prevent concurrent exceptions
                points.clear();
                points.putAll(spiro.get_points());

                //
                for (Map.Entry<Integer, Pair<Integer, Integer>> coOrd : points.entrySet()) {
                    if (_shouldStop) {
                        break;
                    }
                    i++;
                    _paintPixel(coOrd);
                    try {
                        Thread.sleep((long) Math.floor(FPS / _pixelsPerTick));
                    } catch (Exception ex) {

                    }
                    if (i == _pixelsPerTick) {
                        drawBuffer();
                        i = 0;
                    }
                }
                if (((double) n / spiro.get_maxIt() * 100) % 0.5 == 0) {
                    System.out.println("Completed " + ((double) n / spiro.get_maxIt() * 100) + "%...");
                }

                if (_shouldStop) {
                    break;
                }
            }
            _shouldStop = false;
            _isSpiroDrawing = false;
            synchronized (this) {
                try {
                    notify();
                } catch (Exception ex) {

                }
            }
            System.out.println("Completed 100%!");
        }

        private void _paintPixel(Map.Entry<Integer, Pair<Integer, Integer>> coOrd) {
            int centre_x = (int) (_dimension.getWidth() / 2);
            int centre_y = (int) (_dimension.getHeight() / 2);
            Pair<Integer, Integer> point = (Pair<Integer, Integer>) coOrd.getValue();
            int x = (int) Math.floor(point.getLeft()) + centre_x;
            int y = (int) Math.floor(point.getRight()) + centre_y;

            try {
                _imageBuffer.setRGB(x, y, new Color(0f, 0f, 0f).getRGB());
            } catch (Exception ex) {
                System.out.println("t: " + coOrd.getKey() + " / x: " + x + " / y: " + y);
            }
        }
    }

    private void drawBuffer() {
        panel_spiro.update(_frameBuffer);
        panel_spiro.paint(_frameBuffer);
    }
}

