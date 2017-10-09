package com.poli.model.filter;

import java.awt.image.BufferedImage;

import com.poli.model.Image;
import com.poli.model.filter.EnumFilterType.EnumFilter;
import com.poli.model.filter.EnumFilterType.Type;
import com.poli.model.util.math.ComplexNumber;
import com.poli.model.util.math.FourierTransform;

public class Filter
{
    private Mask mask;
    private ActivationFunction activationFunction;
    private Image originalImage;
    private Image newImage;

    public Filter(Image original, Image newImage)
    {
        this.originalImage = original;
        this.newImage = newImage;
    }

    public Image applyMedianFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MEDIAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyMaxFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MAX);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyMinFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MIN);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyMeanFilter(int maskRate)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MEAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyHarmonicMeanFilter(int maskRate)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.HARMONIC_MEAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyContraHarmonicMeanFilter(int maskRate, double q)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.CONTRA_HARMONIC_MEAN, q);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyGeometricMeanFilter(int maskRate)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.GEOMETRIC_MEAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyPointMeanFilter(int maskRate)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.POINT_MEAN);
        this.applyImageFilter();

        return this.newImage;
    }

    @Deprecated
    public Image applyLaplaceFilter(int rate)
    {
        this.mask = new Mask(rate, EnumFilter.LAPLACIAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyKuwaharaFilter(int maskRate)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.KUWAHARA);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyGaussianFilter(int maskRate)
    {
        if (maskRate != 3 && maskRate != 5)
        {
            throw new IllegalArgumentException("A mascara deve ter uma altra/largura igual a 3 ou 5");
        }

        this.mask = new Mask(maskRate, EnumFilter.GAUSSIAN);
        this.applyImageFilter();

        return this.newImage;
    }

    public Image applyHighBoostFilter(double increaseRate, EnumFilter lowPassFilter)
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

        int[][] highPassFilter = this.getHighPassFilter();

        for (int row = 0; row < this.originalImage.getRows(); row++)
        {
            for (int col = 0; col < this.originalImage.getCols(); col++)
            {
                int original = this.originalImage.getPixel(row, col);

                int highValue = highPassFilter[col][row];

                int resultPixel = (int) ((increaseRate) * original - highValue);

                resultPixel = this.mask.normalizeValue(resultPixel);

                this.newImage.setPixel(row, col, resultPixel);

            }
        }

        return this.newImage;
    }

    private int[][] getHighPassFilter()
    {
        int[][] highPass = new int[this.originalImage.getCols()][this.originalImage.getRows()];

        for (int row = 0; row < this.originalImage.getRows(); row++)
        {
            for (int col = 0; col < this.originalImage.getCols(); col++)
            {
                int original = this.originalImage.getPixel(row, col);

                int lowPass = this.newImage.getPixel(row, col);

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

        // Image padding = this.getPeddingImg();

        for (int row = 0; row < this.originalImage.getRows(); row++)
        {
            for (int col = 0; col < this.originalImage.getCols(); col++)
            {
                if (this.mask.isValidRegion(row, col, this.originalImage.getRows(), this.originalImage.getCols()))
                {
                    int result = this.mask.calculateMaskResult(
                            this.originalImage.getSubimage(row - rateMid, col - rateMid, rate, rate));
                    // int result = this.originalImage.getPixel(row, col);
                    this.newImage.setPixel(row, col, result);
                }
            }
        }

        // this.newImage = this.removePadding(padding);
    }

    @Deprecated
    public Image removePadding(Image padding)
    {
        int paddingRate = this.mask.getRate();
        paddingRate = (paddingRate - 1) / 2;

        return padding.getSubimage(paddingRate, paddingRate, this.newImage.getCols(), this.newImage.getRows());
    }

    @Deprecated
    public Image getPeddingImg()
    {
        int rate = this.mask.getRate();
        int rateMid = (rate - 1) / 2;

        BufferedImage pad = new BufferedImage(this.newImage.getCols() + rate - 1, this.newImage.getRows() + rate - 1,
                BufferedImage.TYPE_BYTE_GRAY);
        Image padding = new Image(pad);

        for (int row = 0; row < this.newImage.getRows() + rate - 1; row++)
        {
            for (int col = 0; col < this.newImage.getCols() + rate - 1; col++)
            {
                if (this.mask.isValidRegion(row, col, this.newImage.getRows() + rate - 1,
                        this.newImage.getCols() + rate - 1))
                {
                    int pixel = this.newImage.getPixel(row - rateMid, col - rateMid);
                    padding.setPixel(row, col, pixel);
                }
                else
                {
                    padding.setPixel(row, col, 0);
                }

            }
        }

        return padding;
    }

    public Image applyIdealHighPassFilter(int diameter)
    {
        this.applyFrequencyFilter(diameter, Type.HIGH_PASS, EnumFilter.DIAMETER);
        return this.newImage;
    }

    public Image applyButterworthHighPassFilter(int diameter, int n)
    {
        this.applyFrequencyFilter(diameter, Type.LOW_PASS, EnumFilter.BUTTERWORTH);

        int[][] highPassFilter = this.getHighPassFilter();

        for (int row = 0; row < this.originalImage.getRows(); row++)
        {
            for (int col = 0; col < this.originalImage.getCols(); col++)
            {
                int original = this.originalImage.getPixel(row, col);

                int highValue = highPassFilter[col][row];

                int resultPixel = (int) (original - highValue);

                resultPixel = resultPixel > 255 ? 255 : resultPixel;
                resultPixel = resultPixel < 0 ? 0 : resultPixel;

                this.newImage.setPixel(row, col, resultPixel);

            }
        }

        return this.newImage;

    }

    public Image applyButterworthLowPassFilter(int diameter)
    {
        this.applyFrequencyFilter(diameter, Type.LOW_PASS, EnumFilter.BUTTERWORTH);

        return this.newImage;
    }

    private void applyFrequencyFilter(int diameter, Type type, EnumFilter filter)
    {
        ComplexNumber[][] fourierTransform = FourierTransform.discretTransform(this.originalImage);

        this.applyActivationFunction(fourierTransform, diameter, type, filter);

        ComplexNumber[][] inverseFourierTransform = FourierTransform.discretInverseTransform(fourierTransform);

        // this.newImage = this.buildImage(inverseFourierTransform, true);

        this.newImage = this.buildImage(inverseFourierTransform, true);

        // this.applyResultInOriginal(highPassImage);

    }

    private void applyActivationFunction(ComplexNumber[][] fourierTransform, int diameter, Type type, EnumFilter filter)
    {
        System.err.println("------------------Aplicando função de ativação--------------------");
        this.activationFunction = new ActivationFunction(filter, type);

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

    private Image buildImage(ComplexNumber[][] fourierTransform, boolean inverse)
    {
        BufferedImage bfImage = new BufferedImage(fourierTransform[0].length, fourierTransform.length,
                BufferedImage.TYPE_BYTE_GRAY);

        Image image = new Image(bfImage);

        for (int row = 0; row < fourierTransform.length; row++)
        {
            for (int col = 0; col < fourierTransform[0].length; col++)
            {
                ComplexNumber number = fourierTransform[row][col];
                double real = inverse ? number.real() : number.getAbsolutValue();
                int intValue = (int) real;
                double floatValue = real - intValue;

                double magnitude = floatValue > 0.5 ? (intValue + 1) : intValue;
                magnitude *= Math.pow(-1, col + row);

                int intPart = (int) (magnitude > 255 ? 255 : magnitude);
                intPart = intPart < 0 ? 0 : intPart;

                image.setPixel(row, col, intPart);
            }
        }

        return image;
    }

}
