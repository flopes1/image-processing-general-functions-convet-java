package com.poli.model;

import java.awt.image.BufferedImage;

public class FourierTransform
{

    public static ComplexNumber[][] discretTransform(BufferedImage fxy)
    {
        System.err.println("------------------Inicio da transformada discreta--------------------");
        ComplexNumber[][] result = new ComplexNumber[fxy.getHeight()][fxy.getWidth()];

        double m = fxy.getHeight();
        double n = fxy.getWidth();

        for (int u = 0; u < m; u++)
        {
            for (int v = 0; v < n; v++)
            {
                double sumReal = 0;
                double sumImg = 0;

                for (int x = 0; x < m; x++)
                {
                    for (int y = 0; y < n; y++)
                    {

                        int functionInput = parsePixelValue(fxy.getRGB(y, x));
                        functionInput *= Math.pow(-1, x + y);

                        double imgArgs = 2 * Math.PI
                                * (((double) u * (double) x / (double) m) + (((double) v * (double) y) / (double) n));

                        double real = Math.cos(imgArgs);
                        double img = -Math.sin(imgArgs);

                        ComplexNumber complexNumber = new ComplexNumber(real, img);

                        complexNumber = complexNumber.mult(functionInput);
                        sumReal += complexNumber.real();
                        sumImg += complexNumber.imaginary();
                    }
                }
                // System.out.println("For externos: " + u + "X" + v);
                ComplexNumber finalPixel = new ComplexNumber(sumReal, sumImg);
                finalPixel = finalPixel.div(Math.sqrt(m * n));
                result[u][v] = finalPixel;
            }
        }

        return result;
    }

    public static int parsePixelValue(int rgb)
    {
        return (rgb & 0x000000ff);
    }

    public static ComplexNumber[][] discretInverseTransform(ComplexNumber[][] Fuv)
    {
        System.err.println("------------------Inicio da transformada discreta inversa --------------------");
        double m = Fuv.length;
        double n = Fuv[0].length;

        // BufferedImage result = new BufferedImage((int) n, (int) m, BufferedImage.TYPE_BYTE_GRAY);
        ComplexNumber[][] result = new ComplexNumber[(int) m][(int) n];

        for (int u = 0; u < m; u++)
        {
            for (int v = 0; v < n; v++)
            {
                double sumReal = 0;
                double sumImg = 0;

                for (int x = 0; x < m; x++)
                {
                    for (int y = 0; y < n; y++)
                    {
                        ComplexNumber input = Fuv[x][y];

                        double imgArgs = 2.0 * Math.PI
                                * (((double) u * (double) x / m) + (((double) v * (double) y) / n));

                        double real = Math.cos(imgArgs);
                        double img = Math.sin(imgArgs);

                        ComplexNumber complexNumber = new ComplexNumber(real, img);
                        ComplexNumber complexResult = complexNumber.mult(input);

                        sumReal += complexResult.real();
                        sumImg += complexResult.imaginary();
                    }
                }
                // System.out.println("For externos: " + u + "X" + v);
                ComplexNumber finalPixel = new ComplexNumber(sumReal, sumImg);
                finalPixel = finalPixel.div(Math.sqrt(m * n));
                result[u][v] = finalPixel;
            }
        }

        return result;
    }

}
