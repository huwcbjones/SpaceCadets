package com.huwcbjones.CircleDetector;

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
        for (int x = 0; x < img.length - 1; x++) {
            for (int y = 0; y < img[0].length - 1; y++) {
                Gx[x][y] = getGx(x, y);
                Gy[x][y] = getGy(x, y);
                G[x][y] = Double.valueOf(Math.sqrt((Gx[x][y] * Gx[x][y]) + (Gy[x][y] * Gy[x][y]))).intValue();
            }
        }
        int max = 0;
        for (int x = 0; x < img.length; x++) {
            for (int y = 0; y < img[0].length; y++) {
                if(max < G[x][y]){
                    max = G[x][y];
                }
            }
        }
        System.out.println(max);
        double ratio = max / 255;
        for (int x = 0; x < img.length; x++) {
            for (int y = 0; y < img[0].length; y++) {
                int shade = Double.valueOf(G[x][y] / ratio).intValue();
                image.setRGB(x, y, 0xff000000 | shade << 16 | shade << 8 | shade);
            }
        }
    }

    private int getGx (int x, int y) {
        return getG(x, y, true);
    }

    private int getGy (int x, int y) {
        return getG(x, y, false);
    }

    private int getG(int x, int y, boolean isX) {
        int sum = 0;
        int i = 0;
        int[] m = isX ? this.Mx : this.My;
        System.out.print( x + ", " + y +":\n");
        for (int My = y - 1; My <= y + 1; My++) {
            System.out.print("|");
            for (int Mx = x - 1; Mx <= x + 1; Mx++) {
                int value = getImgCoord(Mx, My);
                System.out.print(value + "|");
                sum += m[i] * value;
                i++;
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        return sum;
    }

    public int getImgCoord(int x, int y) {
        if (
                (x < 0 || y < 0)
                        || (x >= img.length || y >= img[0].length)
                ) {
            return 0;
        }
        return img[x][y];
    }

    public int getCoord (int x, int y) {
        if (
                (x < 0 || y < 0)
                        || (x >= img.length || y >= img[0].length)
                ) {
            return 0;
        }
        return image.getRGB(x, y) & 0xff;
    }

}
