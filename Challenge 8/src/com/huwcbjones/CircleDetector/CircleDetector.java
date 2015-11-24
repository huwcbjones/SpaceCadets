package com.huwcbjones.CircleDetector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Circle Detector GUI
 *
 * @author Huw Jones
 * @since 20/11/2015
 */
public class CircleDetector extends JFrame {

    private JPanel pane_menuBar;
    private JButton btn_load;
    private JButton btn_webcam;
    private ImagePanel pane_image;

    private Detector detector;

    public CircleDetector () {
        try {
            SwingUtilities.invokeAndWait(() -> {
                _buildGUI();
            });
            detector = new Detector();
        } catch (Exception ex) {
            System.err.println("Failed to start program. " + ex.getMessage());
        }
    }

    private void _buildGUI () {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Circle Detection");

        this.pane_menuBar = new JPanel(new FlowLayout());

        this.btn_load = new JButton("Load");
        this.btn_load.setMnemonic('l');
        this.btn_load.addActionListener(new btn_load_Click());

        this.pane_menuBar.add(this.btn_load);

        this.btn_webcam = new JButton("Webcam");
        this.btn_webcam.setMnemonic('w');

        this.pane_menuBar.add(this.btn_webcam);

        this.add(pane_menuBar, BorderLayout.NORTH);

        this.pane_image = new ImagePanel();
        this.pane_image.setBackground(new Color(255, 255, 255));
        this.add(this.pane_image, BorderLayout.CENTER);

        this.setSize(1024, 768);
        this.setVisible(true);
    }

    public static void main (String args[]) {
        CircleDetector circleDetector = new CircleDetector();
    }

    protected void processImage (File file) {
        try {
            int height = this.getHeight();
            int width = this.getWidth();
            Graphics2D g2d;
            BufferedImage tempImage = ImageIO.read(file);
            /* g2d = tempImage.createGraphics();
            g2d.drawImage(tempImage, 0, 0, null);
            double aspectRatio = tempImage.getWidth() / tempImage.getHeight();

            int height;
            int width;
            if (Double.valueOf(this.getHeight() * aspectRatio).intValue() < this.getWidth()) {

                height = this.getHeight();
            } else {
                height = Double.valueOf(this.getWidth() / aspectRatio).intValue();
            }
            if (Double.valueOf(this.getWidth() / aspectRatio) < this.getHeight()) {
                width = this.getWidth();
            } else{
                width = Double.valueOf(this.getHeight() * aspectRatio).intValue();
            }*/

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = image.createGraphics();
            g2d.drawImage(tempImage, 0, 0, null);
            g2d.dispose();

            detector.setImage(image);
            detector.process();
            pane_image.setImage(detector.getImage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Failed to open file", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class btn_load_Click implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed (ActionEvent e) {
            JFileChooser openFile = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
            openFile.setFileFilter(filter);
            openFile.setMultiSelectionEnabled(false);
            openFile.setDialogTitle("Open Image");
            openFile.setDialogType(JFileChooser.FILES_ONLY);
            if (openFile.showDialog(CircleDetector.this, "Open") != JFileChooser.APPROVE_OPTION) {
                return;
            }
            CircleDetector.this.processImage(openFile.getSelectedFile());
        }
    }
}
