package com.huwcbjones.CircleDetector;

import java.awt.image.BufferedImage;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 24/11/2015
 */
public class SobelOperator {

    int[] Gx;
    int[] Gy;
    int[] G;
    BufferedImage image;

    public SobelOperator(BufferedImage image){
        this.image = image;
        Gx = new int[image.getWidth()];
        Gy = new int[image.getHeight()];
    }

    public void process(){

    }

}
