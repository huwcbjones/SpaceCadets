package com.huwcbjones.CircleDetector;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 24/11/2015
 */
public class SobelOperator {

    private final int[] Mx =
            {
                    -1, 0, 1,
                    -2, 0, 2,
                    -1, 0, 1
            };
    private final int[] My =
            {
                    -1, -2, -1,
                    0, 0, 0,
                    1, 2, 1
            };
    private int[][] img;
    private int[][] Gx;
    private int[][] Gy;
    private int[][] G;
    private BufferedImage image;

    public SobelOperator(BufferedImage image){
        this.image = image;
        img = new int[image.getWidth()][];
        Gx = new int[image.getWidth()][];
        Gy = new int[image.getWidth()][];
        G = new int[image.getWidth()][];
        for (int i = 0; i < image.getWidth(); i++) {
            img[i] = new int[image.getHeight()];
            Gx[i] = new int[image.getHeight()];
            Gy[i] = new int[image.getHeight()];
            G[i] = new int[image.getHeight()];
        }
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                img[x][y] = getCoord(x, y);
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void process(){
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Gx[x][y] = getGx(x, y);
                Gy[x][y] = getGy(x, y);
                G[x][y] = Double.valueOf(Math.sqrt(Gx[x][y] * Gx[x][y] + Gy[x][y] * Gy[x][y])).intValue();
            }
        }
        int max = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if(max < G[x][y]){
                    max = G[x][y];
                }
            }
        }
        double scaleFactor = max/255d;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int shade = Double.valueOf(G[x][y] / scaleFactor).intValue();
                image.setRGB(x, y, 0xff000000 | shade << 16 | shade << 8 | shade);
            }
        }
    }

    private int getGx (int x, int y) {
        int gx = 0;
        // Gx Sobel Operator
        // -1  0  1
        // -2  0  2
        // -1  0  1

        for(int i : Mx){
            gx += i * img[x][y];
        }

        return gx;
    }

    private int getGy (int x, int y) {
        int gy = 0;
        // Gy Sobel Operator
        // -1 -2 -1
        //  0  0  0
        //  1  2  1

        for(int i : My){
            gy += i * img[x][y];;
        }
        return gy;
    }

    public int getCoord (int x, int y) {
        if (
                ( x <= 0 || y <= 0 )
                        || ( x > Gx.length || y > Gy.length )
                ) {
            return 0;
        }
        return image.getRGB(x, y) & 0xff;
    }

}
