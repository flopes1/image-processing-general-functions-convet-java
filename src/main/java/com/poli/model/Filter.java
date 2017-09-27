package com.poli.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.ietf.jgss.Oid;

import com.poli.model.EnumFilterType.EnumFilter;
import com.poli.model.EnumFilterType.Type;

public class Filter
{
    private Mask mask;
    private ActivationFunction activationFunction;
    private BufferedImage originalImage;
    private BufferedImage newImage;

    public Filter(BufferedImage image)
    {
        this.originalImage = image;
        this.newImage = this.cloneImage();
    }

    private BufferedImage cloneImage()
    {
        return this.originalImage.getSubimage(0, 0, this.originalImage.getWidth(), this.originalImage.getHeight());
    }

    public BufferedImage applyMedianFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MEDIAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public BufferedImage applyMeanFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MEAN);
        this.applyImageFilter();

        return this.newImage;
    }

    @Deprecated
    public BufferedImage applyLaplaceFilter(int rate)
    {
        this.mask = new Mask(rate, EnumFilter.LAPLACIAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public BufferedImage applyKuwaharaFilter(int maskRate)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.KUWAHARA);
        this.applyImageFilter();

        return this.newImage;
    }

    public BufferedImage applyHighBoostFilter(double increaseRate, EnumFilter lowPassFilter)
    {
        if (lowPassFilter.equals(EnumFilter.MEDIAN))
        {
            this.applyMedianFilter(3);
        }
        else if (lowPassFilter.equals(EnumFilter.KUWAHARA))
        {
            this.applyKuwaharaFilter(3);
        }
        else if (lowPassFilter.equals(EnumFilter.MEAN))
        {
            this.applyMeanFilter(3);
        }

        int[][] highPassFilter = this.getHighPassFilter(increaseRate);

        List<Integer> list = this.convert2List(highPassFilter);
        double max = list.stream().mapToInt(i -> i).max().getAsInt();
        double min = list.stream().mapToInt(i -> i).min().getAsInt();

        for (int row = 0; row < this.newImage.getHeight(); row++)
        {
            for (int col = 0; col < this.newImage.getWidth(); col++)
            {
                int original = this.originalImage.getRGB(col, row);
                original = (original & 0x000000ff);

                int highValue = highPassFilter[col][row];

                int resultPixel = (int) ((increaseRate) * original - highValue);

                resultPixel = this.mask.normalizeValue(resultPixel, list, max, min);

                Color color = new Color(resultPixel, resultPixel, resultPixel);
                this.newImage.setRGB(col, row, color.getRGB());

            }
        }

        return this.newImage;
    }

    private List<Integer> convert2List(int[][] highPassFilter)
    {

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < highPassFilter.length; i++)
        {
            for (int j = 0; j < highPassFilter[i].length; j++)
            {
                list.add(highPassFilter[i][j]);
            }
        }

        return list;
    }

    private int[][] getHighPassFilter(double increaseRate)
    {
        int[][] highPass = new int[this.originalImage.getWidth()][this.originalImage.getHeight()];

        for (int row = 0; row < this.newImage.getHeight(); row++)
        {
            for (int col = 0; col < this.newImage.getWidth(); col++)
            {
                int original = this.originalImage.getRGB(col, row);
                original = (original & 0x000000ff);

                int lowPass = this.newImage.getRGB(col, row);
                lowPass = (lowPass & 0x000000ff);

                int value = (int) (lowPass - original);
                highPass[col][row] = value;
            }
        }

        return highPass;
    }

    private void applyImageFilter()
    {
        int rate = this.mask.getRate();
        int rateMid = (rate - 1) / 2;

        BufferedImage padding = this.getPeddingImg();

        for (int row = 0; row < padding.getHeight(); row++)
        {
            for (int col = 0; col < padding.getWidth(); col++)
            {
                if (this.mask.isValidRegion(row, col, padding.getHeight(), padding.getWidth()))
                {
                    int result = this.mask
                            .calculateMaskResult(padding.getSubimage(col - rateMid, row - rateMid, rate, rate));
                    Color color = new Color(result, result, result);
                    padding.setRGB(col, row, color.getRGB());
                }
            }
        }

        this.newImage = this.removePadding(padding);
    }

    private BufferedImage removePadding(BufferedImage padding)
    {
        int paddingRate = this.mask.getRate();
        paddingRate = (paddingRate - 1) / 2;

        return padding.getSubimage(paddingRate, paddingRate, this.newImage.getWidth(), this.newImage.getHeight());
    }

    private BufferedImage getPeddingImg()
    {
        int rate = this.mask.getRate();
        int rateMid = (rate - 1) / 2;

        BufferedImage padding = new BufferedImage(this.newImage.getWidth() + rate - 1,
                this.newImage.getHeight() + rate - 1, BufferedImage.TYPE_BYTE_GRAY);

        for (int row = 0; row < this.newImage.getHeight() + rate - 1; row++)
        {
            for (int col = 0; col < this.newImage.getWidth() + rate - 1; col++)
            {
                if (this.mask.isValidRegion(row, col, this.newImage.getHeight() + rate - 1,
                        this.newImage.getWidth() + rate - 1))
                {
                    int pixel = this.newImage.getRGB(col - rateMid, row - rateMid);
                    pixel = (pixel & 0x000000ff);
                    // pixel = 150;
                    Color color = new Color(pixel, pixel, pixel);
                    padding.setRGB(col, row, color.getRGB());
                }
                else
                {
                    Color color = new Color(0, 0, 0);
                    padding.setRGB(col, row, color.getRGB());
                }

            }
        }

        return padding;
    }

    public BufferedImage applyIdealHighPassFilter(int diameter)
    {
        ComplexNumber[][] fourierTransform = FourierTransform.discretTransform(this.originalImage);

        this.applyActivationFunction(fourierTransform, diameter);

        ComplexNumber[][] inverseFourierTransform = FourierTransform.discretInverseTransform(fourierTransform);

        BufferedImage highPassImage = this.buildImage(inverseFourierTransform);

        this.apply(highPassImage);

        return this.newImage;
    }

    private void apply(BufferedImage highPassImage)
    {

        for (int row = 0; row < this.newImage.getHeight(); row++)
        {
            for (int col = 0; col < this.newImage.getWidth(); col++)
            {
                int original = this.originalImage.getRGB(col, row);
                original = FourierTransform.parsePixelValue(original);

                int highValue = highPassImage.getRGB(col, row);
                highValue = FourierTransform.parsePixelValue(highValue);

                int resultPixel = original - highValue;

                resultPixel = resultPixel > 255 ? 255 : resultPixel;
                resultPixel = resultPixel < 0 ? 0 : resultPixel;

                Color color = new Color(resultPixel, resultPixel, resultPixel);
                this.newImage.setRGB(col, row, color.getRGB());

            }
        }

    }

    private void applyActivationFunction(ComplexNumber[][] fourierTransform, int diameter)
    {

        this.activationFunction = new ActivationFunction(EnumFilter.DIAMETER, Type.HIGH_PASS);

        double m = fourierTransform.length;
        double n = fourierTransform[0].length;

        int centerRow = (int) (m / (double) 2);
        int centerCol = (int) (n / (double) 2);

        for (int u = 0; u < m; u++)
        {
            for (int v = 0; v < n; v++)
            {
                double distance = Math.hypot(u - centerRow, v - centerCol);
                ComplexNumber resultActivation = this.activationFunction.activate(fourierTransform[u][v], distance,
                        diameter);
                fourierTransform[u][v] = resultActivation;
            }
        }

    }

    private BufferedImage buildImage(ComplexNumber[][] fourierTransform)
    {
        BufferedImage image = new BufferedImage(fourierTransform[0].length, fourierTransform.length,
                BufferedImage.TYPE_BYTE_GRAY);

        for (int row = 0; row < fourierTransform.length; row++)
        {
            for (int col = 0; col < fourierTransform[0].length; col++)
            {
                ComplexNumber number = fourierTransform[row][col];
                double real = number.real();
                int intValue = (int) real;
                double floatValue = real - intValue;

                double magnitude = floatValue > 0.5 ? (intValue + 1) : intValue;
                // int intPart = (int) Math.log10(1 + magnitude);
                magnitude *= Math.pow(-1, col + row);

                int intPart = (int) (magnitude > 255 ? 255 : magnitude);
                intPart = intPart < 0 ? 0 : intPart;

                Color color = new Color(intPart, intPart, intPart);
                image.setRGB(col, row, color.getRGB());
            }
        }

        return image;
    }

}
