package com.huwcbjones.CircleDetector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 20/11/2015
 */
public class Detector {

    BufferedImage image;
    HashMap<Integer, Integer> radii;

    public Detector (){ }
    public Detector (BufferedImage image) {
        this.setImage(image);
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }

    public BufferedImage getImage(){
        return this.image;
    }

    public void process(){
        convertToGreyscale();
        edgeDetection();
        radiiDetection();
        drawCircles();
    }
    private void convertToGreyscale () {
        BufferedImage greyscale = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = greyscale.getGraphics();
        g.drawImage(this.image, 0, 0, null);
        g.dispose();
        this.image = greyscale;
    }

    private void edgeDetection () {

    }

    private void radiiDetection () {

    }

    private void drawCircles () {

    }
}
