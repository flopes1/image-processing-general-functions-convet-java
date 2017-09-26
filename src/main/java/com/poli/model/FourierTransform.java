package com.poli.model;

import java.awt.image.BufferedImage;

public class FourierTransform
{

    public static ComplexNumber[][] discretTransform(BufferedImage fxy)
    {

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
                        // functionInput *= Math.pow(-1, x + y);

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

                result[u][v] = new ComplexNumber(sumReal, sumImg);
            }
        }

        return result;
    }

    private static int parsePixelValue(int rgb)
    {
        return (rgb & 0x000000ff);
    }

    public static ComplexNumber[][] discretInverseTransform(ComplexNumber[][] Fwu)
    {

        double m = Fwu.length;
        double n = Fwu[0].length;

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

                        // ??
                        int functionInput = (int) (Fwu[x][y].real() + Fwu[x][y].imaginary());

                        double imgArgs = 2.0 * Math.PI
                                * (((double) u * (double) x / m) + (((double) v * (double) y) / n));

                        double real = Math.cos(imgArgs);
                        double img = Math.sin(imgArgs);

                        ComplexNumber complexNumber = new ComplexNumber(real, img);
                        complexNumber = complexNumber.mult(functionInput);
                        complexNumber = complexNumber.div(m * n);
                        
                        sumReal += complexNumber.real();
                        sumImg += complexNumber.imaginary();
                    }
                }

                result[u][v] = new ComplexNumber(sumReal, sumImg);
            }
        }

        return result;
    }

}
