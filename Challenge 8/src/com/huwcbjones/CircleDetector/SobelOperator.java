package com.huwcbjones.CircleDetector;

import java.awt.image.BufferedImage;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 24/11/2015
 */
public class SobelOperator {

    int[][] Gx;
    int[][] Gy;
    int[][] G;
    BufferedImage image;

    public SobelOperator(BufferedImage image){
        this.image = image;
        Gx = new int[image.getWidth()][];
        Gy = new int[image.getWidth()][];
        G = new int[image.getWidth()][];
        for (int i = 0; i < image.getWidth(); i++) {
            Gx[i] = new int[image.getHeight()];
            Gy[i] = new int[image.getHeight()];
            G[i] = new int[image.getHeight()];
        }
    }

    public void process(){
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int Gx = getGx(x, y);
                int Gy = getGy(x, y);
            }
        }
    }

    private int getGx (int x, int y) {
        int gx = 0;
        // Gx Sobel Operator
        // -1  0 -1
        // -2  0 -2
        // -1  0 -1

        // (-1, -1)                         (0, -1)                     (1, -1)
        gx += -1 * getCoord(x - 1, y - 1) + -1 * getCoord(x + 1, y - 1);

        // (-1, 0)                          (0, 0)                      (0, 1)
        gx += -2 * getCoord(x - 1, y) + -2 * getCoord(x + 1, y);

        // (-1, 1)                          (0, 1)                      (1, 1)
        gx += -1 * getCoord(x - 1, y + 1) + -1 * getCoord(x + 1, y + 1);

        return gx;
    }

    private int getGy (int x, int y) {
        int gy = 0;
        // Gy Sobel Operator
        // -1 -2 -1
        //  0  0  0
        // -1 -2 -1

        // (-1, -1)                         (0, -1)                     (1, -1)
        gy += -1 * getCoord(x - 1, y - 1) + -2 * getCoord(x, y - 1) + -1 * getCoord(x + 1, y - 1);

        // (-1, 0)                          (0, 0)                      (0, 1)

        // (-1, 1)                          (0, 1)                      (1, 1)
        gy += -1 * getCoord(x - 1, y + 1) + -2 * getCoord(x, y + 1) + -1 * getCoord(x + 1, y + 1);

        return gy;
    }

    public int getCoord (int x, int y) {
        if (
                ( x <= 0 || y <= 0 )
                        || ( x > Gx.length || y > Gy.length )
                ) {
            return 0;
        }
        return image.getRGB(x, y);
    }

}
