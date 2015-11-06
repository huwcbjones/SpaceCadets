package com.huwcbjones.spiro;

import com.huwcbjones.helper.ComboItem;
import com.huwcbjones.helper.Pair;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Huw on 17/10/2015.
 */
public class SpirographDrawer extends JApplet {
    private JPanel panel_window;
    private JSlider slider_outerRadius;
    private JSlider slider_innerRadius;
    private JLabel label_outerRadius;
    private JLabel label_innerRadius;
    private JLabel label_speed;
    private JLabel label_detail;
    private JSlider slider_speed;
    private JButton btn_draw;
    private JButton btn_stop;
    private JButton btn_clear;
    private JPanel panel_menu;
    private JPanel panel_status;
    private JComboBox<ComboItem> combo_detail;
    private ImagePanel panel_spiro;
    private JProgressBar progressBar_draw;

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

    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    _buildGUI(); // initialize the GUI
                }
            });
        } catch (Exception exc) {
            System.out.println("Can't create because of " + exc);
            return;
        }

    }

    public void start() {
        _init();
    }

    private void _init() {
        _updateValues();
        _initX2buff();
    }

    private void _buildGUI() {
        this.panel_window = new JPanel(new BorderLayout());
        this.panel_menu = new JPanel(new FlowLayout());
        this.panel_window.add(panel_menu, BorderLayout.PAGE_START);

        this.panel_spiro = new ImagePanel();
        this.panel_spiro.setBackground(new Color(255, 255, 255));
        this.panel_window.add(panel_spiro, BorderLayout.CENTER);

        this.panel_status = new JPanel(new FlowLayout());
        this.panel_window.add(panel_status, BorderLayout.PAGE_END);

        this.label_innerRadius = new JLabel("Inner Radius");
        this.label_innerRadius.setVerticalAlignment(JLabel.CENTER);
        this.panel_menu.add(label_innerRadius);

        this.slider_innerRadius = new JSlider();
        this.slider_innerRadius.setMinimum(-10000);
        this.slider_innerRadius.setMaximum(10000);
        this.slider_innerRadius.setMajorTickSpacing(2000);
        this.slider_innerRadius.setMinorTickSpacing(500);
        this.slider_innerRadius.setPaintTicks(true);
        this.slider_innerRadius.setValue(-1000);
        this.panel_menu.add(slider_innerRadius);

        this.label_outerRadius = new JLabel("Outer Radius");
        this.label_outerRadius.setVerticalAlignment(JLabel.CENTER);
        this.panel_menu.add(label_outerRadius);

        this.slider_outerRadius = new JSlider();
        this.slider_outerRadius.setMinimum(-10000);
        this.slider_outerRadius.setMaximum(10000);
        this.slider_outerRadius.setMajorTickSpacing(2000);
        this.slider_outerRadius.setMinorTickSpacing(500);
        this.slider_outerRadius.setPaintTicks(true);
        this.slider_outerRadius.setValue(700);
        this.panel_menu.add(slider_outerRadius);

        this.label_speed = new JLabel("Speed");
        this.label_speed.setVerticalAlignment(JLabel.CENTER);
        this.panel_menu.add(label_speed);

        this.slider_speed = new JSlider();
        this.slider_speed.setMinimum(1);
        this.slider_speed.setMaximum(240);
        this.slider_speed.setMajorTickSpacing(40);
        this.slider_speed.setMinorTickSpacing(10);
        this.slider_speed.setPaintTicks(true);
        this.slider_speed.setSnapToTicks(true);
        this.slider_speed.setValue(80);
        this.slider_speed.addChangeListener(new slider_speed_Event());
        this.panel_menu.add(slider_speed);

        this.label_detail = new JLabel("Detail");
        this.label_detail.setVerticalAlignment(JLabel.CENTER);
        this.panel_menu.add(label_detail);

        this.combo_detail = new JComboBox<ComboItem>();
        this.combo_detail.addItem(new ComboItem(10000, "Low"));
        this.combo_detail.addItem(new ComboItem(50000, "Medium"));
        this.combo_detail.addItem(new ComboItem(100000, "High"));
        this.combo_detail.addItem(new ComboItem(500000, "Ultra"));
        this.combo_detail.addItem(new ComboItem(1000000, "Insane"));
        this.combo_detail.addItem(new ComboItem(5000000, "Apeshit"));
        this.combo_detail.addItem(new ComboItem(10000000, "Apocalypse"));
        this.combo_detail.addItem(new ComboItem(50000000, "Just stop"));
        this.combo_detail.addItem(new ComboItem(100000000, "Pray to Gaben"));
        this.combo_detail.addItem(new ComboItem(500000000, "Good CPU?"));
        this.combo_detail.addItem(new ComboItem(1000000000, "Why?"));
        this.combo_detail.setSelectedIndex(2);
        this.panel_menu.add(combo_detail);

        this.btn_draw = new JButton("Draw");
        this.btn_draw.setVerticalAlignment(JButton.CENTER);
        this.btn_draw.addActionListener(new btn_draw_Event());
        this.panel_menu.add(btn_draw);

        this.btn_stop = new JButton("STOP");
        this.btn_stop.setVerticalAlignment(JButton.CENTER);
        this.btn_stop.addActionListener(new btn_stop_Event());
        this.panel_menu.add(btn_stop);

        this.btn_clear = new JButton("Clear");
        this.btn_clear.setVerticalAlignment(JButton.CENTER);
        this.btn_clear.addActionListener(new btn_clear_Event());
        this.panel_menu.add(btn_clear);

        this.progressBar_draw = new JProgressBar();
        this.progressBar_draw.setMinimum(0);
        this.progressBar_draw.setMaximum(100);
        this.progressBar_draw.setValue(0);
        this.panel_status.add(progressBar_draw);

        getContentPane().add(this.panel_window);
    }

    private void _initX2buff() {
        _imageBuffer = (BufferedImage) panel_spiro.createImage(_dimension.width, _dimension.height);
        _frameBuffer = (Graphics2D) _imageBuffer.getGraphics();
    }

    private void _updateValues() {
        if (
                !(panel_spiro.getSize().getWidth() == 0
                        || panel_spiro.getSize().getHeight() == 0)
                ) {
            this._dimension = panel_spiro.getSize();
        } else {
            this._dimension = new Dimension(1, 1);
        }

        this._pixelsPerTick = _getPixelsPerTick();
        this._outerRadius = _getOuterRadius();
        this._innerRadius = _getInnerRadius();
        this._maxIts = _getMaxIts();
    }

    /**
     * @return Int: pixels painted per tick
     */
    private int _getPixelsPerTick() {
        //return this.slider_speed.getValue();

        int ppt = 1;
        double power = this.slider_speed.getValue() / 10d;

        double result = Math.pow(2, (double) power);

        ppt = (int) Math.floor(result);

        return ppt;
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

    private int _getMaxIts() {
        ComboItem item = (ComboItem) combo_detail.getSelectedItem();
        System.out.println(item.getValue());
        return item.getValue();
    }

    private class slider_speed_Event implements ChangeListener {
        /**
         * Invoked when the target of the listener has changed its state.
         *
         * @param e a ChangeEvent object
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            _pixelsPerTick = _getPixelsPerTick();
        }
    }

    private class btn_draw_Event implements ActionListener {
        @Override
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

    private class btn_stop_Event implements ActionListener {
        @Override
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
        }
    }

    private class btn_clear_Event implements ActionListener {
        @Override
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
            _init();
            panel_spiro.repaint();
        }
    }

    private void _drawSpiro() {
        this._init();
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

        private void _killCalc() {
            spiro.stop();
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

                // Clear HashMap and copy values into our one to prevent concurrent exceptions
                points.clear();
                points.putAll(spiro.get_points());

                //
                for (Map.Entry<Integer, Pair<Integer, Integer>> coOrd : points.entrySet()) {
                    if (_shouldStop) {
                        _killCalc();
                        break;
                    }
                    i++;
                    _paintPixel(coOrd);
                    try {
                        Thread.sleep((long) Math.floor(FPS / _pixelsPerTick));
                    } catch (Exception ex) {

                    }
                    if (i == _pixelsPerTick) {
                        panel_spiro.repaint();
                        i = 0;
                    }
                }
                progressBar_draw.setValue((int) Math.round((double) n / spiro.get_maxIt() * 100));
                if (((double) n / spiro.get_maxIt() * 100) % 0.5 == 0) {
                    System.out.println("Completed " + ((double) n / spiro.get_maxIt() * 100) + "%...");
                }

                if (_shouldStop) {
                    _killCalc();
                    break;
                }
            }
            panel_spiro.repaint();
            _shouldStop = false;
            _isSpiroDrawing = false;
            synchronized (this) {
                try {
                    notify();
                } catch (Exception ex) {

                }
            }
            progressBar_draw.setValue(0);
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
                //System.out.println("t: " + coOrd.getKey() + " / x: " + x + " / y: " + y);
            }
        }
    }

    class ImagePanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            //super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(_imageBuffer, 0, 0, this);
        }

    }

}
